package ejercicios;

import perceptron.PerceptronBuilder;
import perceptron.PerceptronInstance;
import utils.CsvReader;
import utils.TP31ValuesGenerator;
import utils.Vector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Ejercicio1 {
    static String TP31_PATH = "./data/TP3-1.csv";

    private static List<Map<String, String>> TP31_values;

    public static void run() throws IOException {
        TP31ValuesGenerator.generate(100, TP31_PATH);

        TP31_values = CsvReader.readCsv(TP31_PATH, ",");

        a();
    }

    public static void a() throws IOException {
        PerceptronInstance perceptronInstance = (new PerceptronBuilder())
                .setBias(1)
                .setDimension(2)
                .setLearningRate(0.01)
                .create();

        //Train
        for(int i=0; i<100000; i++){
            for(Map<String, String> line : TP31_values){
                Vector input = new Vector(Arrays.asList(
                        Double.valueOf(line.get("x")),
                        Double.valueOf(line.get("y"))
                ));

                perceptronInstance.train(input, Double.parseDouble(line.get("c")));
            }
        }

        //Print weights
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("./data/ej1a.csv"))){
            String separator = "";
            for(Double weight : perceptronInstance.getWeights().getValues()){
                bw.write(separator);
                bw.write(weight.toString());

                separator = ",";
            }
        }
    }
}
