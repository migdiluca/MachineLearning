import src.kmeans.KMeansInstance;
import src.kmeans.KMeansRunner;
import src.kohonen.KohonenMap;
import src.kohonen.Lattice;
import src.kohonen.SquareLattice;
import src.hierarchicalClustering.HierarchicalClusteringInstance;
import src.perceptron.PerceptronBuilder;
import src.perceptron.PerceptronInstance;
import src.utils.CsvReader;
import src.utils.LogisticRegression;
import src.utils.math.Vector;
import src.utils.math.VectorWithBool;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.deepToString(hierarchicalClusteringConfusionMatrix(0.8)));
        //KMeansRun(6, 40, 0.8);
        //System.out.println(Arrays.deepToString(hierarchicalClusteringConfusionMatrix(0.8)));
    }

    private static List<List<Vector>> getDataWithBool(double percentage) throws IOException {
        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        double[] Y = new double[data.size()];
        Double[][] valuesWithNull = new Double[data.size()][];

        for (int i = 0; i < data.size(); i++) {
            Y[i] = Double.parseDouble(data.get(i).get("sigdz"));

            valuesWithNull[i] = new Double[]{
                    toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste")),
                    toDoubleOrNull(data.get(i).get("sigdz"))
            };

        }

        double[][] XMean = valuesWithNullToMean(valuesWithNull);
        normalizeWrapperWrapper(XMean);
        shuffleArray(XMean);

        List<Vector> XMeanList = Arrays.stream(XMean)
                .map(elem -> new VectorWithBool(Arrays.copyOfRange(elem, 0, elem.length - 1), elem[elem.length - 1] == 1))
                .collect(Collectors.toList());

        List<List<Vector>> resp = new ArrayList<>(2);
        resp.add(XMeanList.subList(0, (int) (XMeanList.size() * percentage)));
        resp.add(XMeanList.subList((int) (XMeanList.size() * percentage), XMeanList.size()));
        return resp;
    }

    private static void normalizeWrapperWrapper(double [][] x) {
        for (int i = 0; i < x[0].length; i++) {
            normalizeWrapper(x, i);
        }
    }

    private static void normalizeWrapper(double [][] x, int index) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < x.length; i++) {
            double value = x[i][index];
            if(value < min)
                min = value;
            if(value > max)
                max = value;
        }

        for (int i = 0; i < x.length; i++) {
            x[i][index] = normalize(x[i][index], max, min, 1.0);
        }
    }

    private static double normalize(double x, Double max, Double min, Double scale){
        return scale * (x - min) / (max - min);
    }

    private static void KMeansGroupRun(int maxK, int groupSize, double percentage) throws IOException {
        List<List<Vector>> data = getDataWithBool(percentage);
        List<Vector> train = data.get(0);
        List<Vector> test = data.get(1);

        for (int k = 2; k <= maxK; k++) {
            List<KMeansInstance> kMeansInstances = KMeansRunner.runKMeansGroupSimulation(train, groupSize, k);

            int[][] confusionMatrix = new int[2][2];
            for(Vector v : test) {
                int expected = ((VectorWithBool) v).isBool() ? 1 : 0;
                int predicted = classifyKMeansGroup(kMeansInstances, v);
                confusionMatrix[expected][predicted] += 1;
            }

            System.out.println("k = " + k);
            System.out.println(Arrays.deepToString(confusionMatrix));
            System.out.println(( (double)(confusionMatrix[0][0] + confusionMatrix[1][1])) / (confusionMatrix[0][1] + confusionMatrix[1][0] + confusionMatrix[1][1] + confusionMatrix[0][0]));
        }
    }

    private static int classifyKMeansGroup(List<KMeansInstance> instances, Vector vector) {
        int trueAmounts = 0;
        for (KMeansInstance instance: instances) {
            int predictedClass = instance.classify(vector);
            List<Vector> classPoints = instance.getClassPoints(predictedClass);
            trueAmounts += classPoints.stream().filter(e -> ((VectorWithBool) e).isBool()).count() > ((double)classPoints.size()) / 2 ? 1 : 0;
        }
        return trueAmounts >= ((double)instances.size()) / 2 ? 1 : 0;
    }

    private static void KMeansRun(int maxK, int iterations, double percentage) throws IOException {

        List<List<Vector>> data = getDataWithBool(percentage);
        List<Vector> train = data.get(0);
        List<Vector> test = data.get(1);

        for (int k = 2; k <= maxK; k++) {
            KMeansInstance kMeansInstance = KMeansRunner.runKMeansSimulation(train, iterations, k);

            int[][] confusionMatrix = new int[2][2];
            for(Vector v : test) {
                int predictedClass = kMeansInstance.classify(v);
                int expected = ((VectorWithBool) v).isBool() ? 1 : 0;

                List<Vector> classPoints = kMeansInstance.getClassPoints(predictedClass);
                int predicted = classPoints.stream().filter(e -> ((VectorWithBool) e).isBool()).count() > ((double)classPoints.size()) / 2 ? 1 : 0;
                confusionMatrix[expected][predicted] += 1;
            }

            System.out.println("k = " + k);
            System.out.println(Arrays.deepToString(confusionMatrix));
            System.out.println(( (double)(confusionMatrix[0][0] + confusionMatrix[1][1])) / (confusionMatrix[0][1] + confusionMatrix[1][0] + confusionMatrix[1][1] + confusionMatrix[0][0]));
        }
    }

    private static SetDivision getData(double percentage) throws IOException {
        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        Double[][] valuesWithNull = new Double[data.size()][];
        double[] Y = new double[data.size()];

        for (int i = 0; i < data.size(); i++) {
            Y[i] = Double.parseDouble(data.get(i).get("sigdz"));

            valuesWithNull[i] = new Double[]{
                    toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste"))
            };

        }

        double[][] XMean = valuesWithNullToMean(valuesWithNull);

        normalizeWrapperWrapper(XMean);
        return testAndTrainDivision(XMean, Y, percentage);
    }

    private static void logisticRegression() throws IOException {

        SetDivision sd = getData(0.8);

        PerceptronInstance p1 = new PerceptronBuilder()
                .setActivationFunction(PerceptronBuilder.SIGMOID_ACTIVATION_FUNCTION)
                .setLearningRate(0.0000000001)
                .setBias(1)
                .setDimension(sd.train[0].length)
                .create();

        train(p1, sd.train, sd.Ytrain);
        System.out.println(p1.getWeights().toString());
        int[][] confusionMatrix = new int[2][2];
        for (int i = 0; i < sd.test.length; i++) {
            double predictedProbability = p1.classify(new Vector(sd.test[i]));
            int expected = (int) sd.Ytest[i];
            int predicted = (int) Math.round(predictedProbability);
            confusionMatrix[expected][predicted] += 1;
        }

        System.out.println(Arrays.deepToString(confusionMatrix));
//        double prob0 = p1.classify(new Vector(new double[]{normalize(60.0, 82.0, 17.0, 1.0),
//                normalize(2.0, 416.0, 0.0, 1.0),
//                normalize(199.0,29.0, 576.0, 1.0)}));
//
//        System.out.println(prob0);

    }

    private static class SetDivision {
        public double[][] train;
        public double[][] test;

        public double[] Ytrain;
        public double[] Ytest;

        public SetDivision(double[][] train, double[][] test, double[] ytrain, double[] ytest) {
            this.train = train;
            this.test = test;
            Ytrain = ytrain;
            Ytest = ytest;
        }
    }

    private static SetDivision testAndTrainDivision(double[][] X, double[] Y, double percentage) {
        List<Integer> indexes = IntStream.range(0, X.length).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);
        int trainSize = (int) (indexes.size() * percentage);
        int testSize = X.length - trainSize;
        double[][] Xtrain = new double[trainSize][X[0].length];
        double[][] Xtest = new double[testSize][X[0].length];
        double[] Ytrain = new double[trainSize];
        double[] Ytest = new double[testSize];

        for (int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            if (i < trainSize) {
                Xtrain[i] = X[index];
                Ytrain[i] = Y[index];
            } else {
                Xtest[i - trainSize] = X[index];
                Ytest[i - trainSize] = Y[index];
            }
        }

        return new SetDivision(Xtrain, Xtest, Ytrain, Ytest);
    }

    private static double fieldProbability(double[][] X, int index, double value) {
        int amount = 0;
        for (int i = 0; i < X.length; i++) {
            if (Double.compare(value, X[i][index]) == 0) {
                amount++;
            }
        }
        return amount / (double) X.length;
    }

    private static void kohonenWrapper() throws IOException {
         SetDivision sd = getData(0.8);

        kohonen(sd.train, sd.Ytrain, sd.test, sd.Ytest);
    }

    private static void kohonen(double[][] train, double[] yTrain, double[][] test, double[] yTest){
        int numberOfIterations = 10000;
        int inputDim = train[0].length;
        double timeConstant = numberOfIterations / Math.log(inputDim);
        int dim = 5;

        KohonenMap kohonenMap = new KohonenMap.Builder()
                .setLattice(new SquareLattice(SquareLattice.cellGenerator(inputDim), dim))
                .setTimeConstant(timeConstant)
                .setLearningRateFunction(KohonenMap.Builder::learningRateFunction)
                .setNeighborhoodFunction(KohonenMap.Builder::neighborhoodFunction)
                .create();

        for (int i = 0; i < numberOfIterations; i++) {
            for (double[] sample : train) {
                src.kohonen.Vector vector = new src.kohonen.Vector(sample);
                kohonenMap.step(vector);
            }
        }

        //Frequency in each som cell
        class ClassCount {
            int classA, classB;

            ClassCount() {
                classA = 0;
                classB = 0;
            }

            void inc(Double category) {
                if (Double.compare(category, 1) == 0) {
                    classA++;
                } else {
                    classB++;
                }
            }

            double classify() {
                return classA > classB ? 1.0 : 0.0;
            }
        }

        ClassCount[][] count = new ClassCount[dim][dim];

        for (int i = 0; i < count.length; i++) {
            count[i] = IntStream.range(0, dim)
                    .mapToObj(v -> new ClassCount())
                    .toArray(ClassCount[]::new);
        }

        for (int i = 0; i < train.length; i++) {
            double[] sample = train[i];
            double category = yTrain[i];
            src.kohonen.Vector vector = new src.kohonen.Vector(sample);
            Lattice.Coord coord = kohonenMap.activate(vector);

            count[coord.getI()][coord.getJ()].inc(category);
        }

        int[][] confusionMatrix = new int[2][2];
        Arrays.fill(confusionMatrix[0], 0);
        Arrays.fill(confusionMatrix[1], 0);

        //TEST
        for (int i = 0; i < test.length; i++) {
            double[] sample = test[i];
            double expected = yTest[i];
            src.kohonen.Vector vector = new src.kohonen.Vector(sample);
            Lattice.Coord coord = kohonenMap.activate(vector);

            double predicted = count[coord.getI()][coord.getJ()].classify();

            confusionMatrix[(int) expected][(int) predicted] += 1;
        }

        System.out.printf("%s %s\n", confusionMatrix[0][0], confusionMatrix[0][1]);
        System.out.printf("%s %s\n", confusionMatrix[1][0], confusionMatrix[1][1]);

    }

    private static void train(PerceptronInstance p, double[][] X, double[] Y) {
        int times = 200000;
        for (int t = 0; t < times; t++) {
            for (int i = 0; i < X.length; i++) {
                double[] sample = X[i];
                p.train(new Vector(Arrays.stream(sample).boxed().collect(Collectors.toList())), Y[i]);
            }
        }
    }

    private static Double toDoubleOrNull(String value) {
        return Optional.ofNullable(value)
                .filter(s -> !s.equals(""))
                .map(Double::parseDouble)
                .orElse(null);
    }

    private static double[][] valuesWithNullToMean(Double[][] values) {
        double[][] result = new double[values.length][values[0].length];

        for (int j = 0; j < values[0].length; j++) {
            int count = 0;
            double sum = 0;

            for (Double[] value : values) {
                if (value[j] != null) {
                    count++;
                    sum += value[j];
                }
            }

            double avg = sum / count;

            for (int i = 0; i < values.length; i++) {
                result[i][j] = Optional.ofNullable(values[i][j]).orElse(avg);
            }
        }

        return result;
    }

    private static double[][] removeNullValues(Double[][] values) {
        List<double[]> newArray = new ArrayList<>();

        for (Double[] sample : values) {
            boolean hasNull = Arrays.stream(sample).anyMatch(Objects::isNull);

            if (!hasNull)
                newArray.add(Arrays.stream(sample).mapToDouble(Double::doubleValue).toArray());
        }

        return newArray.toArray(new double[0][0]);
    }

    private static int[][] hierarchicalClusteringConfusionMatrix(double percentage) throws IOException {

        List<List<Vector>> data = getDataWithBool(percentage);
        HierarchicalClusteringInstance.HCNode root = HierarchicalClusteringInstance.formTree(data.get(0));

        int[][] confusionMatrix2 = new int[2][2];

        List<Vector> test = data.get(1);
        for (Vector v: test) {
            int expected = ((VectorWithBool) v).isBool() ? 1 : 0;
            int predicted = root.classify(v, 10) ? 1 : 0;

            confusionMatrix2[expected][predicted]++;
        }
        return confusionMatrix2;
    }

    private static int getSickAmountTree(HierarchicalClusteringInstance.HCNode tree) {
        return tree.getGroup().stream()
                .map(e -> ((VectorWithBool) e).isBool() ? 1 : 0)
                .reduce(0, Integer::sum);
    }

    private static void shuffleArray(double[][] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            double[] a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
