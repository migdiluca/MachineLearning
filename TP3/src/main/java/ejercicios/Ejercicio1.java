package ejercicios;

import perceptron.OptimalHyperplane;
import perceptron.PerceptronBuilder;
import perceptron.PerceptronInstance;
import utils.CsvReader;
import utils.TP31ValuesGenerator;
import utils.math.Vector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Ejercicio1 {
    static String TP31_PATH = "./data/TP3-1.csv";
    static String TP32_PATH = "./data/TP3-2.csv";

    private static List<Map<String, String>> TP31_values;
    private static List<Map<String, String>> TP32_values;

    public static void run() throws IOException {
        TP31_values = CsvReader.readCsv(TP31_PATH, ",");
        TP32_values = CsvReader.readCsv(TP32_PATH, ",");


        Vector weights = a();

        b(weights);

        c();
    }

    public static Vector a() throws IOException {
        PerceptronInstance perceptronInstance = (new PerceptronBuilder())
                .setBias(1)
                .setDimension(2)
                .setLearningRate(0.01)
                .create();

        //Train
        boolean error = true;
        while(error){
            error = false;
            for(Map<String, String> line : TP31_values){
                Vector input = new Vector(Arrays.asList(
                        Double.valueOf(line.get("x")),
                        Double.valueOf(line.get("y"))
                ));

                error |= perceptronInstance.train(input, Double.parseDouble(line.get("c")));
            }
        }

        //Print weights
        printWeights(perceptronInstance.getWeights(), "./data/ej1a.csv");

        return perceptronInstance.getWeights();
    }

    public static void b(Vector weights) throws IOException {
        Vector optimalWeights = OptimalHyperplane.findOptimalHyperplane(weights, TP31_values, 20);

        printWeights(optimalWeights, "./data/ej1b.csv");
    }

    private static void printWeights(Vector weights, String path) throws IOException {
        //Print weights
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            String separator = "";
            for(Double weight : weights.getValues()){
                bw.write(separator);
                bw.write(weight.toString());

                separator = ",";
            }
        }
    }

    private static void c() throws IOException {
        PerceptronInstance perceptronInstance = (new PerceptronBuilder())
                .setBias(1)
                .setDimension(2)
                .setLearningRate(0.01)
                .create();

        //Train
        int remainingIterations = 100;
        boolean error = true;
        while(error && remainingIterations > 0){
            error = false;
            for(Map<String, String> line : TP32_values){
                Vector input = new Vector(Arrays.asList(
                        Double.valueOf(line.get("x")),
                        Double.valueOf(line.get("y"))
                ));

                error |= perceptronInstance.train(input, Double.parseDouble(line.get("c")));
            }
            remainingIterations--;
        }

        //Print weights
        printWeights(perceptronInstance.getWeights(), "./data/ej1c.csv");
    }
}
