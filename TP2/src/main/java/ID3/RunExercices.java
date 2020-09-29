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
        (new File("./results/ID3WithDepthLimit")).mkdir();
        (new File("./results/RandomForestWithDepthLimit")).mkdir();
        (new File("./results/ID3WithLimit")).mkdir();
        (new File("./results/RandomForestWithLimit")).mkdir();

        int i = 0;
        for (double testPercentage = 0.1; testPercentage <= 0.9; testPercentage += 0.1) {
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForest/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, 0, 0);
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

        i = 0;
        for (int limitNodes = 1; limitNodes <= 25; limitNodes += 1) {
            double testPercentage = 0.2;
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3WithDepthLimit/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage, limitNodes, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        i = 0;
        for (int limitNodes = 1; limitNodes <= 25; limitNodes += 1) {
            double testPercentage = 0.2;
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForestWithDepthLimit/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, limitNodes, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        i = 0;
        for (int limitNodes = 25; limitNodes >= 0; limitNodes -= 1) {
            double testPercentage = 0.2;
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3WithDepthLimit/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage, 0, limitNodes);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        i = 0;
        for (int limitNodes = 25; limitNodes >= 0; limitNodes -= 1) {
            double testPercentage = 0.2;
            try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForestWithLimit/run" + i))) {
                int corrects = 0, incorrects = 0;
                for(int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, 0, limitNodes);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
