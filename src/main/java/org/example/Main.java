package org.example;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputFile = "C:/Users/KEVIN/Desktop/Input JK.csv";
        String outputFile = "C:/Users/KEVIN/Desktop/OutputCSV.csv";
        FileReader filereader = new FileReader(inputFile);
        HashMap<Integer, String> headerIndexes = new HashMap<>();

        CSVReader csvReader = new CSVReaderBuilder(filereader).build();
        List<String[]> allData = csvReader.readAll();

        HashMap<Integer, Date> employeeIdVsCurrentDate = getEmployeeIdVsCurrentDate(allData);
        HashMap<Integer, Integer> employeeIdVsCurrentSalary = getEmployeeIdVsCurrentSalary(allData, employeeIdVsCurrentDate);

        setCurrentSalary(outputFile, allData, employeeIdVsCurrentSalary);
    }

    public static HashMap<Integer, Date> getEmployeeIdVsCurrentDate(List<String[]> allData) throws ParseException {
        HashMap<Integer, String> headerIndexes = new HashMap<>();
        HashMap<Integer, Date> employeeIdVsCurrentDate = new HashMap<>();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        int rowCouter = 0;
        for (String[] row : allData) {
            int colCounter = 0;
            int employeeId = -1;
            Date date = df.parse("01-01-0001");
            Date prevDate = df.parse("01-01-0001");
            for (String cell : row) {
                if (rowCouter == 0) {
                    headerIndexes.put(colCounter, cell);
                } else {
                    if (headerIndexes.getOrDefault(colCounter, "").equals("Employee ID")) {
                        employeeId = Integer.valueOf(cell);
                    }
                    if (headerIndexes.getOrDefault(colCounter, "").equals("Date")) {
                        date = df.parse(cell);
                        break;
                    }
                }
                colCounter++;
            }
            if (rowCouter == 0) {
                rowCouter++;
                continue;
            }
            prevDate = employeeIdVsCurrentDate.getOrDefault(employeeId, prevDate);
            date = prevDate.compareTo(date) > 0 ? prevDate : date;
            employeeIdVsCurrentDate.put(employeeId, date);
            rowCouter++;
        }

        return employeeIdVsCurrentDate;
    }

    public static HashMap<Integer, Integer> getEmployeeIdVsCurrentSalary(List<String[]> allData, HashMap<Integer, Date> employeeIdVsCurrentDate) throws ParseException {
        HashMap<Integer, String> headerIndexes = new HashMap<>();
        HashMap<Integer, Integer> employeeIdVsCurrentSalary = new HashMap<>();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        int rowCouter = 0;
        for (String[] row : allData) {
            int colCounter = 0;
            int employeeId = -1;
            Date date = df.parse("01-01-0001");
            Date currDate = df.parse("01-01-0001");
            int currSalary = 0;
            for (String cell : row) {
                if (rowCouter == 0) {
                    headerIndexes.put(colCounter, cell);
                } else {
                    if (headerIndexes.getOrDefault(colCounter, "").equals("Employee ID")) {
                        employeeId = Integer.valueOf(cell);
                    }
                    if (headerIndexes.getOrDefault(colCounter, "").equals("Date")) {
                        date = df.parse(cell);
                    }
                    if (headerIndexes.getOrDefault(colCounter, "").equals("Salary")) {
                        currSalary = Integer.valueOf(cell);
                        break;
                    }
                }
                colCounter++;
            }
            if (rowCouter == 0) {
                rowCouter++;
                continue;
            }
            currDate = employeeIdVsCurrentDate.getOrDefault(employeeId, currDate);
            if (date.equals(currDate)) {
                employeeIdVsCurrentSalary.put(employeeId, currSalary);
            }
            rowCouter++;
        }

        return employeeIdVsCurrentSalary;
    }

    public static void setCurrentSalary(String fileName, List<String[]> allData, HashMap<Integer, Integer> employeeIdVsCurrentSalary) throws Exception {
        HashMap<Integer, String> headerIndexes = new HashMap<>();
        FileWriter file = new FileWriter(fileName);
        CSVWriter writer = new CSVWriter(file);

        int rowCouter = 0;
        for (String[] row : allData) {
            int colCounter = 0;
            int employeeId = -1;
            String currSalary = "";
            for (String cell : row) {
                if (rowCouter == 0) {
                    headerIndexes.put(colCounter, cell);
                } else {
                    if (headerIndexes.getOrDefault(colCounter, "").equals("Employee ID")) {
                        employeeId = Integer.valueOf(cell);
                        break;
                    }
                }
                colCounter++;
            }
            if (rowCouter == 0) {
                rowCouter++;
                writer.writeNext(row);
                continue;
            }
            currSalary = employeeIdVsCurrentSalary.getOrDefault(employeeId, 0).toString();
            row[5] = currSalary;
            writer.writeNext(row);
            rowCouter++;
        }

        writer.close();
    }

}
