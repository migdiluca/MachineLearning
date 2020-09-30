package ID3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RunExercices {
    public static void run() {
        (new File("./results/")).mkdir();

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForest"))) {
            for (double testPercentage = 0.1; testPercentage <= 0.9; testPercentage += 0.1) {
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, 0, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(testPercentage + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3"))) {
            for (double testPercentage = 0.1; testPercentage <= 0.9; testPercentage += 0.1) {
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(testPercentage + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3WithDepthLimit"))) {
            for (int limitNodes = 1; limitNodes <= 10; limitNodes += 1) {
                double testPercentage = 0.2;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage, limitNodes, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForestWithDepthLimit"))) {
            for (int limitNodes = 1; limitNodes <= 10; limitNodes += 1) {
                double testPercentage = 0.2;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, limitNodes, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3WithLimit"))) {
            for (int limitNodes = 10; limitNodes >= 0; limitNodes -= 1) {
                double testPercentage = 0.2;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage, 0, limitNodes);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForestWithLimit"))) {
            for (int limitNodes = 10; limitNodes >= 0; limitNodes -= 1) {
                double testPercentage = 0.2;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, 0, limitNodes);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3WithDepthLimitTraining"))) {
            for (int limitNodes = 1; limitNodes <= 10; limitNodes += 1) {
                double testPercentage = 0;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage, limitNodes, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForestWithDepthLimitTraining"))) {
            for (int limitNodes = 1; limitNodes <= 10; limitNodes += 1) {
                double testPercentage = 0;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, limitNodes, 0);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/ID3WithLimitTraining"))) {
            for (int limitNodes = 10; limitNodes >= 0; limitNodes -= 1) {
                double testPercentage = 0;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = ID3.run(testPercentage, 0, limitNodes);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter("./results/RandomForestWithLimitTraining"))) {
            for (int limitNodes = 10; limitNodes >= 0; limitNodes -= 1) {
                double testPercentage = 0;
                int corrects = 0, incorrects = 0;
                for (int j = 0; j < 10; j++) {
                    int[][] confusionMatrix = RandomForest.run(20, 99, 0, testPercentage, 0, limitNodes);
                    corrects += confusionMatrix[0][0] + confusionMatrix[1][1];
                    incorrects += confusionMatrix[0][1] + confusionMatrix[1][0];
                }
                double precision = (double) corrects / (corrects + incorrects);
                bf.write(limitNodes + " " + precision + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
