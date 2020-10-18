package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TP31ValuesGenerator {

    public static double MARGIN = 0.5;

    public static void generate(int numberOfValues, String filePath) throws IOException{
        int aClassNumber = numberOfValues / 2;
        int bClassNumber = numberOfValues - aClassNumber;

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(String.format("%s,%s,%s\n", "x", "y", "c"));

            for(int i=0; i<aClassNumber; i++){
                generateValue(bw, 1);
            }

            for(int i=0; i<bClassNumber; i++){
                generateValue(bw, -1);
            }
        }

    }

    private static void generateValue(BufferedWriter bw, int classNumber) throws IOException {
        double x = Math.random() * (5 - MARGIN) + MARGIN;
        double y;
        if(classNumber == 1){
            y = (5 - x - MARGIN) * Math.random() + x + MARGIN;
        }else{
            y = (x - MARGIN) * Math.random();
        }
        bw.write(String.format("%s,%s,%s\n", x, y, classNumber));
    }
}
