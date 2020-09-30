package utils;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataFrame {
    private Map<String, List<Object>> columns;
    private List<Map<String, Object>> rows;

    public DataFrame() {
    }

    private static void rowsToColumns(DataFrame df) {
        Map<String, List<Object>> columns = new HashMap<>();
        for(int i = 0; i < df.rows.size(); i++) {
            for(String columnName : df.rows.get(i).keySet()) {
                if(!columns.containsKey(columnName)) {
                    columns.put(columnName, new ArrayList<>());
                }

                columns.get(columnName).add(df.rows.get(i).get(columnName));
            }
        }
        df.setColumns(columns);
    }

    public static DataFrame readCsv(String path, String separator) throws IOException {
        DataFrame df = new DataFrame();
        String[] headers;
        Map<String, List<Object>> columns = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String firstLine = br.readLine();
            headers = firstLine.split(separator);
            for (String header : headers) {
                columns.put(header, new ArrayList<>());
            }

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] lineValues = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < headers.length; i++) {
                    String valueToAdd = lineValues[i];
                    try {
                        columns.get(headers[i]).add(Double.parseDouble(valueToAdd));
                    } catch (NumberFormatException ex2) {
                        if (valueToAdd.equals(""))
                            columns.get(headers[i]).add(null);
                        else
                            columns.get(headers[i]).add(valueToAdd);
                    }
                }
            }
        }

        df.setColumns(columns);
        DataFrame.columnsToRows(df);
        return df;
    }

    private static void updateColumnToRows(DataFrame df, String columnName) {
        List<Object> column = df.columns.get(columnName);
        for(int i = 0; i < column.size(); i++) {
            df.rows.get(i).put(columnName, column.get(i));
        }
    }

    private static void columnsToRows(DataFrame df) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for(String columnName : df.getColumnNames()) {
            List<Object> column = df.getColumn(columnName);
            for(int i = 0; i < column.size(); i++) {
                if(rows.size() <= i) {
                    rows.add(new HashMap<>());
                }

                rows.get(i).put(columnName, column.get(i));
            }
        }
        df.setRows(rows);
    }

    public void map(Function<Object, Object> fun, String columnName) {
        List<Object> list = columns.get(columnName);
        list = list.stream().map(item -> item == null ? null : fun.apply(item)).collect(Collectors.toList());
        columns.put(columnName, list);
        DataFrame.updateColumnToRows(this, columnName);
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public List<Object> getColumn(String name) {
        return columns.get(name);
    }

    public Map<String, List<Object>> getColumns() {
        return columns;
    }

    private void setColumns(Map<String, List<Object>> columns) {
        this.columns = columns;
    }

    public Object mean(String columnName) {
        List<Object> list = columns.get(columnName);
        if (list.get(0) instanceof Double) {
            double sum = (double) list.stream().filter(Objects::nonNull).reduce((acc, item) -> (double) acc + (double) item).get();
            return sum / list.stream().filter(Objects::nonNull).count();
        } else {
            Map<String, Integer> count = new HashMap<>();
            list.forEach(item -> count.put((String) item, count.containsKey(item) ? count.get(item) + 1 : 1));

            Integer maxCount = -1;
            String maxCountValue = null;
            for (String key : count.keySet()) {
                if (count.get(key) > maxCount) {
                    maxCount = count.get(key);
                    maxCountValue = key;
                }
            }
            return maxCountValue;
        }
    }

    public void remove(String columnName) {
        columns.remove(columnName);
        for(int i = 0; i < rows.size(); i++) {
            rows.get(i).remove(columnName);
        }
    }

    public void replaceNull(Object value, String columnName) {
        List<Object> list = columns.get(columnName).stream().map(item -> item != null ? item : value
        ).collect(Collectors.toList());
        columns.put(columnName, list);
        DataFrame.updateColumnToRows(this, columnName);
    }

    public Set<String> getColumnNames() {
        return columns.keySet();
    }

    public double percentage(Object value, String columnName) {
        return (double) columns.get(columnName).stream().filter(item -> item.equals(value)).count() / columns.get(columnName).size();
    }

    public DataFrame filter(Object value, String columnName) {
        DataFrame newDataFrame = new DataFrame();
        List<Map<String, Object>> newRows = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            if (row.get(columnName).equals(value)) {
                newRows.add(new HashMap<>());
                for (String columnKey : row.keySet()) {
                    newRows.get(newRows.size() - 1).put(columnKey, row.get(columnKey));
                }
            }
        }
        newDataFrame.setRows(newRows);
        DataFrame.rowsToColumns(newDataFrame);
        return newDataFrame;
    }

    public DataFrame getShufflePercent(double percent) {
        Collections.shuffle(rows);
        List<Map<String,Object>> rowsForTest = rows.subList(0, (int)(percent *  rows.size()));
        rows = rows.subList((int)(percent *  rows.size()), rows.size());
        DataFrame.rowsToColumns(this);

        DataFrame testDf = new DataFrame();
        testDf.setRows(rowsForTest);
        DataFrame.rowsToColumns(testDf);

        return testDf;
    }

    public DataFrame getRandomValues() {
        DataFrame randomDataset = new DataFrame();
        List<Map<String, Object>> newRows = new ArrayList<>();
        for(int i = 0; i < rows.size(); i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, rows.size());
            newRows.add(rows.get(randomNum));
        }
        randomDataset.setRows(newRows);
        DataFrame.rowsToColumns(randomDataset);

        return randomDataset;
    }

    public Set<Object> columnValues(String columnName) {
        return new HashSet<>(columns.get(columnName));
    }

    public int size() {
        return rows.size();
    }

    public DataFrame clone() {
        DataFrame cloned = new DataFrame();
        cloned.setRows(this.rows);
        cloned.setColumns(this.columns);
        DataFrame.rowsToColumns(cloned);
        DataFrame.columnsToRows(cloned);
        return cloned;
    }
}
