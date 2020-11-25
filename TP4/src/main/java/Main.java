import src.kohonen.KohonenMap;
import src.kohonen.Lattice;
import src.kohonen.SquareLattice;
import src.perceptron.PerceptronBuilder;
import src.perceptron.PerceptronInstance;
import src.utils.CsvReader;
import src.utils.LogisticRegression;
import src.utils.math.Vector;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        Double[][] valuesWithNull = new Double[data.size()][];
        double[] Y = new double[data.size()];

        for (int i=0 ; i<data.size(); i++){
            Y[i] = Double.parseDouble(data.get(i).get("sigdz"));

            valuesWithNull[i] = new Double[]{
//                    toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste"))
            };

        }

        double[][] XMean = valuesWithNullToMean(valuesWithNull);
//        double[][] XRemove = removeNullValues(valuesWithNull);

        PerceptronInstance p1 = new PerceptronBuilder()
                .setActivationFunction(PerceptronBuilder.SIGMOID_ACTIVATION_FUNCTION)
                .setLearningRate(0.00001)
                .setBias(1)
                .setDimension(XMean[0].length)
                .create();

        train(p1, XMean, Y);
    }

    private static void kohonen(double[][] train, double[][] test, double[] Y){
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
            double category = Y[i];
            src.kohonen.Vector vector = new src.kohonen.Vector(sample);
            Lattice.Coord coord = kohonenMap.activate(vector);

            count[coord.getI()][coord.getJ()].inc(category);
        }

        int errors = 0;

        //TEST
        for(int i=0; i<test.length; i++){
            double[] sample = test[i];
            src.kohonen.Vector vector = new src.kohonen.Vector(sample);
            Lattice.Coord coord = kohonenMap.activate(vector);

            double predicted = count[coord.getI()][coord.getJ()].classify();

            errors += Double.compare(predicted, Y[i]) == 0 ? 0 : 1;
        }

        System.out.printf("%s / %s (%s)\n", errors, test.length, errors / (double) test.length);
    }

    private static void train(PerceptronInstance p, double[][] X, double[] Y){
        int times = 1000;
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
}
