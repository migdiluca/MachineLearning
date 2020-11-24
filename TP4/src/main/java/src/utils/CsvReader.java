package src.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {
    public static List<Map<String, String>> readCsv(String path, String separator) throws IOException {
        String[] headers;

        List<Map<String, String>> values = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String firstLine = br.readLine();
            headers = firstLine.split(separator);

            for(String line = br.readLine(); line != null; line = br.readLine()){
                Map<String, String> newValues = new HashMap<>();
                String[] lineValues = line.split(separator);

                for(int i = 0; i < headers.length; i++){
                    if(i < lineValues.length)
                        newValues.put(headers[i], lineValues[i]);
                    else
                        newValues.put(headers[i], null);
                }
                values.add(newValues);
            }
        }

        return values;
    }
}
