package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TP32ValuesGenerator {

    public static void generate(int wrongValues, String filePath) throws IOException {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            for(int i=0; i<wrongValues; i++){
                generateWrongValue(bw);
            }
        }
    }

    private static void generateWrongValue(BufferedWriter bw) throws IOException {
        double margin = TP31ValuesGenerator.MARGIN;

        double x = Math.random() * 5;
        double y = x + 2 * margin * Math.random() - margin;

        y = Math.max(y, 0);
        y = Math.min(y, 5);

        bw.write(String.format("%s,%s,%s\n", x, y, Math.random() > 0.5 ? -1 : 1));
    }
}
