import src.perceptron.PerceptronBuilder;
import src.perceptron.PerceptronInstance;
import src.utils.CsvReader;
import src.utils.LogisticRegression;
import src.utils.math.Vector;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Map<String, String>> data = CsvReader.readCsv("./acath.csv", ",");

        Double[][] valuesWithNull = new Double[data.size()][];
        double[] Y = new double[data.size()];

        for (int i=0 ; i<data.size(); i++){
            Y[i] = Double.parseDouble(data.get(i).get("sigdz"));

            valuesWithNull[i] = new Double[]{
                    toDoubleOrNull(data.get(i).get("sex")),
                    toDoubleOrNull(data.get(i).get("age")),
                    toDoubleOrNull(data.get(i).get("cad.dur")),
                    toDoubleOrNull(data.get(i).get("choleste"))
            };

        }

        Double[][] X = valuesWithNullToMean(valuesWithNull);

        PerceptronInstance perceptronInstance = (new PerceptronBuilder())
                .setActivationFunction(PerceptronBuilder.SIGMOID_ACTIVATION_FUNCTION)
                .setBias(1)
                .setLearningRate(0.01)
                .setDimension(X[0].length)
                .create();

        int steps = 10000;
        int maxErrors = 500;
        int errors = maxErrors + 1;
        while(steps != 0 && errors > maxErrors){
            errors = 0;
            for(int i=0; i<X.length; i++){
                src.utils.math.Vector sample = new src.utils.math.Vector(Arrays.asList(X[i]));

                errors += perceptronInstance.train(sample, Y[i]) ? 1 : 0;
            }
            steps--;
        }

        for(int i=0; i<X.length; i++){
            src.utils.math.Vector sample = new src.utils.math.Vector(Arrays.asList(X[i]));

            double result = perceptronInstance.classify(sample);

//            System.out.printf("%s -- %s\n", result, Y[i]);
        }

        double res = 0;
        res += perceptronInstance.classify(new Vector(Arrays.asList(1.0, 60.0, 2.0, 199.0)));
        res += perceptronInstance.classify(new Vector(Arrays.asList(0.0, 60.0, 2.0, 199.0)));

        System.out.println(res);
    }

    private static Double toDoubleOrNull(String value){
        return Optional.ofNullable(value)
                .filter(s -> !s.equals(""))
                .map(Double::parseDouble)
                .orElse(null);
    }

    private static Double[][] valuesWithNullToMean(Double[][] values){
        Double[][] result = new Double[values.length][values[0].length];

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
}
