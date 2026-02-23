package com.orangehrm.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtility {

    public static List<String[]> getSheetData(String filePath, String sheetName){

        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null){
                throw new RuntimeException("Sheet: " + sheetName + " doesn't exist");
            }

            for (Row row : sheet){

                if (row.getRowNum() == 0){
                    continue; // skip header
                }

                List<String> rowData = new ArrayList<>();

                for (Cell cell : row){
                    rowData.add(getCellValue(cell));
                }

                data.add(rowData.toArray(new String[0]));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }


    private static String getCellValue(Cell cell){
        if (cell == null){
            return "";
        }

        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula();

            default:
                return "";
        }
    }
}