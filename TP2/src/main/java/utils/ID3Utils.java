package utils;

import ID3.ID3Instance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ID3Utils {
    public static DataFrame readAndProcessDataFrame() throws IOException {
        DataFrame dataFrame = DataFrame.readCsv("./src/main/resources/titanic.csv", ",");
        dataFrame.remove("PassengerId");
        dataFrame.remove("Name");
        dataFrame.remove("Ticket");
        dataFrame.remove("Cabin");

        for (String columnName : dataFrame.getColumnNames()) {
            dataFrame.replaceNull(dataFrame.mean(columnName), columnName);
        }

        double fareMean = (double) dataFrame.mean("Fare");
        dataFrame.map(item -> (double) item > fareMean ? 1.0 : 0.0, "Fare");
        double ageMean = (double) dataFrame.mean("Age");
        dataFrame.map(item -> (double) item > ageMean ? 1.0 : 0.0, "Age");
        return dataFrame;
    }

    public static int[][] confussionMatrix(DataFrame test, ID3Instance instance) {
        int[][] matrix = new int[2][2];
        List<Map<String, Object>> rows = test.getRows();
        for(Map<String, Object> condition : rows) {
            String resp = instance.classify(condition);
            if(resp.equals("Yes")) {
                if((double)condition.get("Survived") == 1.0) {
                    matrix[0][0] += 1;
                } else {
                    matrix[0][1] += 1;
                }
            }
            else if(resp.equals("No")) {
                if ((double) condition.get("Survived") == 0.0) {
                    matrix[1][1] += 1;
                } else {
                    matrix[1][0] += 1;
                }
            }
        }
        return matrix;
    }
}
