package ejercicios;

import KNN.KNNInstance;
import utils.CsvReader;
import utils.DivideTrainAndTest;
import utils.Vector;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Ejercicio1 {
    private static class Review {
        String reviewTitle;
        String reviewText;
        Double wordCount;
        Double titleSentiment;
        Double textSentiment;
        Double sentimentValue;
        Integer starRating;

        static Function<Double, Double> wordCountNormalizer,
                titleSentimentNormalizer,
                sentimentValueNormalizer,
                titleSentiment_SentimentValueNormalizer;

        List<Double> toList(){
            List<Double> values = new ArrayList<>();
            values.add(wordCountNormalizer.apply(wordCount));
            values.add(titleSentimentNormalizer.apply(titleSentiment));
            values.add(sentimentValueNormalizer.apply(sentimentValue));
            return values;
        }

        public static void setWordCountNormalizer(Function<Double, Double> wordCountNormalizer) {
            Review.wordCountNormalizer = wordCountNormalizer;
        }

        public static void setTitleSentimentNormalizer(Function<Double, Double> titleSentimentNormalizer) {
            Review.titleSentimentNormalizer = titleSentimentNormalizer;
        }

        public static void setSentimentValueNormalizer(Function<Double, Double> sentimentValueNormalizer) {
            Review.sentimentValueNormalizer = sentimentValueNormalizer;
        }
    }

    private static final List<Review> data = new ArrayList<>();

    public static void run(){
        System.out.println("Ejercicio 1 ########");

        System.out.println("\na)");
        a();
        System.out.println("\nb)");
        DivideTrainAndTest.Division<Review> division = b();
        System.out.println("\nc/d)");
        c(division);
    }

    private static Review rowMapper(Map<String, String> row){
        Review newReview = new Review();

        newReview.reviewTitle = row.get("Review Title");
        newReview.reviewText = row.get("Review Text");
        newReview.wordCount = Double.valueOf(row.get("wordcount"));

        String titleSentiment = row.get("titleSentiment");
        switch (titleSentiment){
            case "negative":
                newReview.titleSentiment = -1.0;
                break;
            case "positive":
                newReview.titleSentiment = 1.0;
                break;
            default:
                newReview.titleSentiment = null;
        }

        String textSentiment = row.get("textSentiment");
        switch (textSentiment) {
            case "negative":
                newReview.textSentiment = -1.0;
                break;
            case "positive":
                newReview.textSentiment = 1.0;
                break;
        }

        newReview.starRating = Integer.valueOf(row.get("Star Rating"));
        newReview.sentimentValue = Double.valueOf(row.get("sentimentValue"));

        return newReview;
    }

    private static void loadCsv(String path) throws IOException {
        List<Map<String, String>> values = CsvReader.readCsv(path, ";");

        for(Map<String, String> row : values){
            data.add(rowMapper(row));
        }

        replaceTitleSentimentNanWithMean();
    }

    private static void a(){
        try{
            loadCsv("./data/reviews_sentiment.csv");
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        Double avg = data.stream()
                .filter(v->v.starRating.equals(1))
                .mapToInt(v->v.wordCount.intValue())
                .average()
                .orElseThrow(IllegalArgumentException::new);
        System.out.printf("Los comentarios valorados con 1 estrella tienen en promedio %.2f palabras.\n", avg);
    }

    private static DivideTrainAndTest.Division<Review> b(){
        double trainRate = 0.8;

        DivideTrainAndTest.Division<Review> division = DivideTrainAndTest.divide(data, trainRate);

        System.out.printf("Divided data in %.2f%% train data and %.2f%% test data.\n", trainRate * 100,
                (1 - trainRate) * 100);

        return division;
    }

    private static void c(DivideTrainAndTest.Division<Review> division){
        KNNInstance<Integer> knnInstance = new KNNInstance<>();

        //Train

        double maxWordCount = division.train.stream()
                .mapToDouble(v->v.wordCount).max().orElseThrow(IllegalArgumentException::new);
        double minWordCount = division.train.stream()
                .mapToDouble(v->v.wordCount).min().orElseThrow(IllegalArgumentException::new);

        double maxTitleSentiment = division.train.stream()
                .mapToDouble(v->v.titleSentiment).max().orElseThrow(IllegalArgumentException::new);
        double minTitleSentiment = division.train.stream()
                .mapToDouble(v->v.titleSentiment).min().orElseThrow(IllegalArgumentException::new);

        double maxSentimentValue = division.train.stream()
                .mapToDouble(v->v.sentimentValue).max().orElseThrow(IllegalArgumentException::new);
        double minSentimentValue = division.train.stream()
                .mapToDouble(v->v.sentimentValue).min().orElseThrow(IllegalArgumentException::new);

        Review.setWordCountNormalizer(normalize(maxWordCount, minWordCount, 100.0));
        Review.setTitleSentimentNormalizer(normalize(maxTitleSentiment, minTitleSentiment, 100.0));
        Review.setSentimentValueNormalizer(normalize(maxSentimentValue, minSentimentValue, 100.0));

        for(Review review : division.train){
            List<Double> values = review.toList();

            knnInstance.train(values, review.starRating);
        }

        //Test
        Map<Integer, Map<Integer, Integer>> testResults;

        testResults = new HashMap<>();
        for(Review review : division.test){
            List<Double> values = review.toList();

            Integer classifyResult = knnInstance.classify(values, 5, false);

            Map<Integer, Integer> map =
                    Optional.ofNullable(testResults.get(review.starRating)).orElse(new HashMap<>());
            map.put(classifyResult, 1 + Optional.ofNullable(map.get(classifyResult)).orElse(0));
            testResults.put(review.starRating, map);
        }

        System.out.println("\nKNN confusion matrix");
        plotConfusionMatrix(testResults);

        testResults = new HashMap<>();
        for(Review review : division.test){
            List<Double> values = review.toList();

            Integer classifyResult = knnInstance.classify(values, 5, true);

            Map<Integer, Integer> map =
                    Optional.ofNullable(testResults.get(review.starRating)).orElse(new HashMap<>());
            map.put(classifyResult, 1 + Optional.ofNullable(map.get(classifyResult)).orElse(0));
            testResults.put(review.starRating, map);
        }

        System.out.println("\nKNN weighted confusion matrix");
        plotConfusionMatrix(testResults);
    }

    private static <T> void plotConfusionMatrix(Map<T, Map<T, Integer>> results){
        List<T> categories = new LinkedList<>(results.keySet());

        categories.forEach(v-> System.out.printf("%s\t", v));
        System.out.print("\n");

        System.out.println("-------------------");

        for(T a : categories){
            for(T b : categories){
                System.out.printf("%s", Optional.ofNullable(results.get(a).get(b)).orElse(0));
                System.out.print("\t");
            }
            System.out.print("\n");
        }

        System.out.println();

        for(T a : categories){
            int tp = Optional.ofNullable(results.get(a)).map(v->v.get(a)).orElse(0);

            int p = results.values().stream()
                    .map(v->Optional.ofNullable(v.get(a)).orElse(0))
                    .mapToInt(Integer::intValue)
                    .sum();

            System.out.printf("Precision for %s: %.2f\n", a, Double.compare(p, 0) == 0 ? '-' : (double) tp / p);
        }
    }

    private static void replaceTitleSentimentNanWithMean(){
        double mean = data.stream()
                .filter(review -> review.titleSentiment != null)
                .mapToDouble(review -> review.titleSentiment)
                .average()
                .orElseThrow(IllegalArgumentException::new);

        data.forEach(review -> {
            review.titleSentiment = Optional.ofNullable(review.titleSentiment).orElse(mean);
        });
    }

    private static Function<Double, Double> normalize(Double max, Double min, Double scale){
        return a -> scale * (a - min) / (max - min);
    }
}
