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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println(Arrays.deepToString(hierarchicalClusteringConfusionMatrix(1)));

        kohonenWrapper();
    }

    private void logisticRegression() throws IOException {

        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        Double[][] valuesWithNull = new Double[data.size()][];
        double[] Y = new double[data.size()];

        for (int i=0 ; i<data.size(); i++){
            Y[i] = Double.parseDouble(data.get(i).get("sigdz"));

            valuesWithNull[i] = new Double[]{
                    //toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste"))
            };

        }

        double[][] XMean = valuesWithNullToMean(valuesWithNull);
        double[][] XRemove = removeNullValues(valuesWithNull);

        SetDivision sd = testAndTrainDivision(XMean, Y, 0.8);

        PerceptronInstance p1 = new PerceptronBuilder()
                .setActivationFunction(PerceptronBuilder.SIGMOID_ACTIVATION_FUNCTION)
                .setLearningRate(0.00000001)
                .setBias(1)
                .setDimension(sd.train[0].length)
                .create();

        train(p1, sd.train, sd.Ytrain);

        System.out.println(p1.getWeights().toString());
        int [][] confusionMatrix = new int[2][2];
        for(int i = 0; i < sd.test.length; i++) {
            double predictedProbability = p1.classify(new Vector(sd.test[i]));
            int expected = (int)sd.Ytest[i];
            int predicted = (int)Math.round(predictedProbability);
            confusionMatrix[expected][predicted] += 1;
        }

        System.out.println(Arrays.deepToString(confusionMatrix));
        double prob0 = p1.classify(new Vector(new double []{0.0, 60.0, 2.0, 199.0}));
        double prob1 = p1.classify(new Vector(new double []{1.0, 60.0, 2.0, 199.0}));


        double probSex0 = fieldProbability(XMean, 0, 0);
        double probSex1 = fieldProbability(XMean, 0, 1);

        System.out.println((probSex0 * prob0) + (prob1 * probSex1));
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

    private static SetDivision testAndTrainDivision(double [][] X, double []Y, double percentage) {
        List<Integer> indexes = IntStream.range(0, X.length).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);
        int trainSize = (int) (indexes.size() * percentage);
        int testSize = X.length - trainSize;
        double [][] Xtrain = new double[trainSize][X[0].length];
        double [][] Xtest = new double[testSize][X[0].length];
        double [] Ytrain = new double[trainSize];
        double [] Ytest = new double[testSize];

        for(int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            if(i < trainSize) {
                Xtrain[i] = X[index];
                Ytrain[i] = Y[index];
            } else {
                Xtest[i - trainSize] = X[index];
                Ytest[i - trainSize] = Y[index];
            }
        }

        return new SetDivision(Xtrain, Xtest, Ytrain, Ytest);
    }

    private static double fieldProbability(double [][] X, int index, double value) {
        int amount = 0;
        for(int i = 0; i < X.length; i++) {
            if(Double.compare(value, X[i][index]) == 0) {
                amount++;
            }
        }
        return amount/(double)X.length;
    }

    private static void kohonenWrapper() throws IOException {
        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        Double[][] valuesWithNull = new Double[data.size()][];
        double[] Y = new double[data.size()];

        for (int i=0 ; i<data.size(); i++){
            Y[i] = Double.parseDouble(data.get(i).get("sigdz"));

            valuesWithNull[i] = new Double[]{
                    //toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste"))
            };

        }

        double[][] XMean = valuesWithNullToMean(valuesWithNull);
        double[][] XRemove = removeNullValues(valuesWithNull);

        SetDivision sd = testAndTrainDivision(XMean, Y, 0.8);

        kohonen(sd.train, sd.Ytrain, sd.test, sd.Ytest);
    }

    private static void kohonen(double[][] train, double[] yTrain, double[][] test, double[] yTest){
        int numberOfIterations = 10000;
        int inputDim = train[0].length;
        double timeConstant = numberOfIterations/Math.log(inputDim);
        int dim = 5;

        KohonenMap kohonenMap = new KohonenMap.Builder()
                .setLattice(new SquareLattice(SquareLattice.cellGenerator(inputDim), dim))
                .setTimeConstant(timeConstant)
                .setLearningRateFunction(KohonenMap.Builder::learningRateFunction)
                .setNeighborhoodFunction(KohonenMap.Builder::neighborhoodFunction)
                .create();

        for(int i=0; i<numberOfIterations; i++){
            for(double[] sample : train){
                src.kohonen.Vector vector = new src.kohonen.Vector(sample);
                kohonenMap.step(vector);
            }
        }

        //Frequency in each som cell
        class ClassCount {
            int classA, classB;

            ClassCount(){
                classA = 0;
                classB = 0;
            }

            void inc(Double category){
                if(Double.compare(category, 1) == 0){
                    classA++;
                }else{
                    classB++;
                }
            }

            double classify(){
                return classA > classB ? 1.0 : 0.0;
            }
        }

        ClassCount[][] count = new ClassCount[dim][dim];

        for (int i=0; i<count.length; i++) {
            count[i] = IntStream.range(0, dim)
                    .mapToObj(v -> new ClassCount())
                    .toArray(ClassCount[]::new);
        }

        for(int i = 0; i< train.length; i++){
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
        for(int i=0; i<test.length; i++){
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

    private static void train(PerceptronInstance p, double[][] X, double[] Y){
        int times = 10000;
        for(int t = 0; t < times; t++){
            for(int i=0; i< X.length; i++){
                double[] sample = X[i];
                p.train(new Vector(Arrays.stream(sample).boxed().collect(Collectors.toList())), Y[i]);
            }
        }
    }

    private static Double toDoubleOrNull(String value){
        return Optional.ofNullable(value)
                .filter(s -> !s.equals(""))
                .map(Double::parseDouble)
                .orElse(null);
    }

    private static double[][] valuesWithNullToMean(Double[][] values){
        double[][] result = new double[values.length][values[0].length];

        for(int j=0; j < values[0].length; j++){
            int count = 0;
            double sum = 0;

            for (Double[] value : values) {
                if (value[j] != null) {
                    count++;
                    sum += value[j];
                }
            }

            double avg = sum / count;

            for(int i=0; i < values.length; i++){
                result[i][j] = Optional.ofNullable(values[i][j]).orElse(avg);
            }
        }

        return result;
    }

    private static double[][] removeNullValues(Double[][] values){
        List<double[]> newArray = new ArrayList<>();

        for(Double[] sample : values){
            boolean hasNull = Arrays.stream(sample).anyMatch(Objects::isNull);

            if(!hasNull)
                newArray.add(Arrays.stream(sample).mapToDouble(Double::doubleValue).toArray());
        }

        return newArray.toArray(new double[0][0]);
    }

    private static int[][] hierarchicalClusteringConfusionMatrix(double percentage) throws IOException {
        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        Double[][] valuesWithNull = new Double[data.size()][];

        for (int i=0 ; i<data.size(); i++){
            valuesWithNull[i] = new Double[]{
                    toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste")),
                    toDoubleOrNull(data.get(i).get("sigdz"))
            };

        }

        double[][] XMean = valuesWithNullToMean(valuesWithNull);
        shuffleArray(XMean);
        XMean = Arrays.copyOfRange(XMean, 0, (int) (XMean.length * percentage));

        HierarchicalClusteringInstance.HCNode root = HierarchicalClusteringInstance.formTree(
                Arrays.stream(XMean)
                        .map(elem -> new VectorWithBool(Arrays.copyOfRange(elem, 0, elem.length - 1), elem[elem.length - 1] == 1))
                        .collect(Collectors.toList()));

        HierarchicalClusteringInstance.HCNode child1 = root.getChild1();
        HierarchicalClusteringInstance.HCNode child2 = root.getChild2();

        int child1SickAmount = getSickAmountTree(child1);
        int child2SickAmount = getSickAmountTree(child2);

        int [][] confusionMatrix = new int[2][2];

        confusionMatrix[0][0] = child1.getGroup().size() - child1SickAmount;
        confusionMatrix[0][1] = child1SickAmount;

        confusionMatrix[1][0] = child2.getGroup().size() - child2SickAmount;
        confusionMatrix[1][1] = child2SickAmount;

        if(child1SickAmount > child2SickAmount) {
            int[] aux = confusionMatrix[0];
            confusionMatrix[0] = confusionMatrix[1];
            confusionMatrix[1] = aux;
        }

        return confusionMatrix;
    }

    private static int getSickAmountTree(HierarchicalClusteringInstance.HCNode tree) {
        return tree.getGroup().stream()
                .map(e -> ((VectorWithBool) e).isBool() ? 1 : 0)
                .reduce(0, Integer::sum);
    }

    private static void shuffleArray(double[][] ar)
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            double [] a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
