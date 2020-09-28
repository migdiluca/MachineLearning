package ID3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RunExercices {
    public static void run() {
        (new File("./results/")).mkdir();
        (new File("./results/RandomForest")).mkdir();
        (new File("./results/ID3")).mkdir();

        int i = 0;
        for (double testPercentage = 0.1; testPercentage <= 0.9; testPercentage += 0.1) {
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForest/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(testPercentage + " " + precision + '\n');
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        i = 0;
        for (double testPercentage = 0.1; testPercentage <= 0.9; testPercentage += 0.1) {
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(testPercentage + " " + precision + '\n');
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
