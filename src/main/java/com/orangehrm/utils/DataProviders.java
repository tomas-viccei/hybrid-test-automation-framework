package com.orangehrm.utils;

import org.testng.annotations.DataProvider;

import java.util.List;

public class DataProviders {

    private static final String FILE_PATH = System.getProperty("user.dir")+"src/test/resources/testdata/TestData.xlsx";

    @DataProvider(name = "validLogin")
    public static Object[][] validLoginData(){
        return getSheetData("validLogin");
    }


    @DataProvider(name = "invalidLogin")
    public static Object[][] invalidLoginData(){
        return getSheetData("invalidLogin");
    }

    private static Object[][] getSheetData(String sheetName){
        List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

        Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

        for (int i=0 ; i < sheetData.size(); i++) {
            data[i] = sheetData.get(i);
        }
        return data;

    }
}
