import src.perceptron.PerceptronBuilder;
import src.perceptron.PerceptronInstance;
import src.utils.CsvReader;
import src.utils.LogisticRegression;
import src.utils.math.Vector;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
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
