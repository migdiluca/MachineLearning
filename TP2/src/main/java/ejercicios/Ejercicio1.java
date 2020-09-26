package ejercicios;

import KNN.KNNInstance;
import utils.CsvReader;
import utils.DivideTrainAndTest;
import utils.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    }

    private static final List<Review> data = new ArrayList<>();

    public static void run(){
        System.out.println("Ejercicio 1 ########");

        System.out.println("a)");
        a();
        System.out.println("b)");
        DivideTrainAndTest.Division<Review> division = b();
        System.out.println("c)");
        c(division);
        System.out.println("d)");
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
                newReview.titleSentiment = 0.0;
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

        System.out.printf("Divided data in %.2f%% train data and %.2f%% test data.\n", trainRate * 100, (1 - trainRate) * 100);

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

        Function<Double, Double> wordCountNormalizer = normalize(maxWordCount, minWordCount);
        Function<Double, Double> titleSentimentNormalizer = normalize(maxTitleSentiment, minTitleSentiment);
        Function<Double, Double> sentimentValueNormalizer = normalize(maxSentimentValue, minSentimentValue);

        for(Review review : division.train){
            List<Double> values = new ArrayList<>();
            values.add(wordCountNormalizer.apply(review.wordCount));
            values.add(titleSentimentNormalizer.apply(review.titleSentiment));
            values.add(sentimentValueNormalizer.apply(review.sentimentValue));

            knnInstance.train(values, review.starRating);
        }

        //Test
        int matches = 0;
        for(Review review : division.test){
            List<Double> values = new ArrayList<>();
            values.add(wordCountNormalizer.apply(review.wordCount));
            values.add(titleSentimentNormalizer.apply(review.titleSentiment));
            values.add(sentimentValueNormalizer.apply(review.sentimentValue));

            matches += knnInstance.classify(values, 5, false).equals(review.starRating) ? 1 : 0;
        }

        System.out.printf("KNN matched %s out of %s.\n", matches, division.test.size());

        matches = 0;
        for(Review review : division.test){
            List<Double> values = new ArrayList<>();
            values.add(sentimentValueNormalizer.apply(review.wordCount));
            values.add(titleSentimentNormalizer.apply(review.titleSentiment));
            values.add(sentimentValueNormalizer.apply(review.sentimentValue));

            matches += knnInstance.classify(values, 5, true).equals(review.starRating) ? 1 : 0;
        }

        System.out.printf("KNN weighted matched %s out of %s.\n", matches, division.test.size());
    }

    private static Function<Double, Double> normalize(Double max, Double min){
        return a -> 100 * (a - min) / (max - min);
    }
}
