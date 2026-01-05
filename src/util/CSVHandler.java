package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    
                    for (int i = 0; i < row.length; i++) {
                        String cell = row[i].trim();
                        if (cell.startsWith("\"") && cell.endsWith("\"")) {
                            row[i] = cell.substring(1, cell.length() - 1);
                        } else {
                            row[i] = cell;
                        }
                    }
                    data.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void appendToCSV(String filePath, String csvLine) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(csvLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRaw(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
