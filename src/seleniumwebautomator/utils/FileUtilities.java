/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumwebautomator.utils;

/**
 *
 * @author amoi
 */
import java.io.File;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static seleniumwebautomator.utils.CommonJavaUtilities.*;

/**
 * @author alexander
 */
public class FileUtilities {

    private static final Props properties = null;
    private static Logging log = null;
    private static final String classname = "FileUtilities | ";
    private static final String logseparator = "| -1 | ";
    private SeleniumUtilities Utils = null;

    //file path to the test data file
    public static String test_data_file = "";

    //file path to the locators data file
    private static String locator_data_file = "";

    static {
        log = new Logging();
    }

    public FileUtilities() {

    }

    /**
     * fetching row data from the excel file.
     *
     * @param filPath data file absolute path.
     * @param sheetnumber excel data sheet, counting left-right from 0.
     * @param rownumber excel data row in the sheet counting top-down from 0.
     * @param fromcolumn column counted left-right, from which to start fetch.
     * @param numberofcolumns number of data columns to fetch.
     * @return excel row columns data set.
     */
    public static ArrayList getExcelRowData(String filPath, int sheetnumber,
            int rownumber, int fromcolumn, int numberofcolumns) {
        String prestr = classname + "getExcelRowData()" + logseparator
                + "@sheet-" + sheetnumber + ", @row-" + rownumber + ":- ";

        log.info(prestr + "started fetching data, sheet-"
                + ", column range: " + fromcolumn + "-" + (fromcolumn
                + numberofcolumns) + "\n");

        ArrayList<String> return_list = new ArrayList<String>();

        log.info(prestr + "Data file path:" + filPath + "\n");

        try {
            File file = new File(filPath);

            FileInputStream inputstrm = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(inputstrm);//excel workbook

            Sheet sheet = workbook.getSheetAt(sheetnumber);//excel sheet

            for (int i = fromcolumn; i < fromcolumn + numberofcolumns; ++i) {

                try {
                    return_list.add(getExcelCellData(sheet, rownumber, i));

                } catch (java.lang.NullPointerException npe) {
                    return_list.add("");
                    log.info(prestr + ",Column- " + i + " is empty.");
                } catch (Exception | Error e) {
                    return_list.add("");
                    log.error(prestr + "Error on processing data, column:" + i
                            + ", error:" + e);
                }
            }
            inputstrm.close();

            FileOutputStream out = new FileOutputStream(new File(filPath));
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            log.error(prestr + "File not found:- " + filPath + ",error:" + e);
        } catch (IOException e) {
            log.error(prestr + "Can't read file data:-" + filPath + ",error:" + e);
        } catch (java.lang.NullPointerException e) {
            log.error(prestr + "Null/empty value exception @row-"
                    + rownumber + ",error:-" + e);
        } catch (Exception | Error e) {
            log.error(prestr + "General exception/error @row- "
                    + rownumber + ",error:-" + e);
        }
        log.info(prestr + "fetch row data complete, size:-" + return_list.size()
                + ",file:- " + filPath );
        return return_list;
    }

    /**
     * get excel cell data
     *
     * @param sheet sheet number counting left-right from 0.
     * @param row row number counting up-down from 0.
     * @param column column number counting left-right from 0.
     * @return data from cell.
     * @throws java.lang.Exception
     */
    public static String getExcelCellData(Sheet sheet,
            int row, int column) throws Exception {

        Cell status = sheet.getRow(row).getCell(column);

        status.setCellType(1);

        String cellData = "";

        if (status.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            
            String excelrawdata = String.valueOf(sheet.getRow(row).
                    getCell(column).getNumericCellValue()).toUpperCase();
            
            
            setSleepTime(1);//Wait for row data to be fetched from excel file.
            
            //if the fetched numeric data has secientific notation E
            if (!excelrawdata.equalsIgnoreCase(excelrawdata.replace("E", ""))) {
                
                String[] getnumericvalue = excelrawdata.split("E");
                //2.54728067006E11
                
                String decimal = getnumericvalue[0].replace(".", "");
                
                cellData = processScientificNumbertoString(
                        decimal, getnumericvalue[1]);
                
            } else if (excelrawdata.endsWith(".0")) {
                
                cellData = excelrawdata.substring(0, excelrawdata.length() - 2);
            }
            
        } else if (status.getCellType() == XSSFCell.CELL_TYPE_STRING) {
            
            cellData = sheet.getRow(row).getCell(column).
                    getStringCellValue();
            
        } else if (status.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            
            cellData
                    = String.valueOf(sheet.getRow(row).getCell(column).
                            getBooleanCellValue());
            
        } else if (status.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
            cellData = "";
            
        }

        return cellData.trim();
    }

    /**
     * Updates excel cell in the data file.
     *
     * @param filePath
     * @param sheetnumber sheet counted left-right from 0.
     * @param rownumber row in the sheet counted top-down from 0.
     * @param column column in the sheet counted left-right from 0.
     * @param data data to update excel with.
     * @return
     */
    public static boolean updateExcelCell(String filePath, int sheetnumber,
            int rownumber, int column, String data) {

        String prestr = classname + "updateExcelCell()" + logseparator;

        log.info(prestr + "updating excel cell;  @sheet: " + sheetnumber
                + ",@row: " + rownumber + ", @cell: " + column + ", with data: " + data);
        try {
            File file = new File(filePath);
            
            FileInputStream inputstrm = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(inputstrm);
            
            XSSFSheet sheet = workbook.getSheetAt(sheetnumber);

            Cell status = sheet.getRow(rownumber).getCell(column);
            
            if (status == null) {
                status = sheet.getRow(rownumber).createCell(column);
            }

            XSSFFont font = workbook.createFont();
            
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            status.setCellStyle(style);
            status.setCellValue(data);

            inputstrm.close();
            FileOutputStream out = new FileOutputStream(new File(filePath));
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException fnfe) {
            log.error(prestr + "Can not find the  file:- " + filePath
                    + ",error message is " + fnfe);
        } catch (IOException ioe) {
            log.error(prestr + "Unable to read data from file:-" + filePath
                    + ", row-" + rownumber + ",details:- " + ioe);
        } catch (java.lang.NullPointerException npe) {
            log.error(prestr + "Null value, row-" + rownumber + ", details:-" + npe);
        } catch (Exception e) {
            log.error(prestr + "Error encounter, row- " + rownumber + ".Details:-" + e);
        }
        log.info(prestr
                + " Excel update complete; data:-" + data + ",file:-" + filePath
                + ", sheet -" + sheetnumber + ",row -" + rownumber 
                + ", from column-" + column);
        return true;
    }

    /**
     * Sets test data file path.
     *
     * @param filePath absolute path to the file.
     */
    public static void setTestDataFile(String filePath) {
        test_data_file = filePath;
    }

    /**
     * Set locators data file path.
     *
     * @param filePath absolute path to the locators file.
     */
    public static void setLocatorsDataFile(String filePath) {
        locator_data_file = filePath;
    }

    /**
     * creates test summery reports. This is yet to be implemented
     */
    public void createTestReport() {

    }

    /**
     * Returns data set as per specified row range and column range.
     *
     * @param filePath path to the data file
     * @param sheetnumber Sheet number.
     * @param fromRow first row to fetch.
     * @param numberOfRows number of rows
     * @param fromColumn first colum to start fetch in each row.
     * @param numberOfColumns number of columns to fetch in each row.
     * @return List of all row records specified.
     */
    public static HashMap getExcelRowsData(String filePath, int sheetnumber,
            int fromRow, int numberOfRows, int fromColumn, int numberOfColumns) {

        String prestr = classname + "getExcelRowsData()" + logseparator;

        String excelStr = " @sheet:" + sheetnumber + ", Rows:" + fromRow
                + "-" + (fromRow + numberOfRows) + ", columns:" + fromColumn
                + "-" + (fromColumn + numberOfColumns);

        log.info(prestr + " Started fetching; " + excelStr + "\n");

        log.info(prestr + "data file is:" + filePath + "\n");

        HashMap<Integer, ArrayList<String>> excelRecords = new HashMap<>();

        for (int rw = fromRow; rw < fromRow + numberOfRows; ++rw) {

            ArrayList<String> row_record = getExcelRowData(filePath,
                    sheetnumber, rw, fromColumn, numberOfColumns);

            if (!(row_record.contains(null) || row_record.contains(""))) {
                int key = rw - fromRow;
                excelRecords.put(key, row_record);
            } else {
                log.info(prestr + ",empty record @sheet-" + sheetnumber
                        + ", row-" + rw + " not added to  list.");
            }
        }

        log.info(prestr + "fetching complete, # of records fetched:-"
                + excelRecords.size() + "from file:- " + filePath);
        
        return excelRecords;
    }
    
    /**
     * Returns column data for set of rows.
     *
     * @param filePath path to the data file
     * @param sheetnumber Sheet number.
     * @param column column number
     * @param fromRow first row to fetch.
     * @param numberOfRows 
     * @return List  of all column data as specified.
     */
    public static ArrayList getExcelColumnData(String filePath, int sheetnumber,
            int column, int fromRow, int numberOfRows) {

        String prestr = classname + "getExcelRowsData()" + logseparator;

        String excelStr = " @sheet:" + sheetnumber + ", Rows:" + fromRow
                + "-" + (fromRow + numberOfRows) + ", column number:" + column;

        log.info(prestr + " Started fetching; " + excelStr + "\n");

        log.info(prestr + "data file is:" + filePath + "\n");

        ArrayList<String> excelRecords = new ArrayList<>();

        for (int rw = fromRow; rw < fromRow + numberOfRows; ++rw) {

            ArrayList<String> row_record = getExcelRowData(filePath,
                    sheetnumber, rw, column, 1);

            if (!(row_record.contains(null) || row_record.contains(""))) {
        
                excelRecords.addAll(row_record);
                
            } else {
                
                log.info(prestr + ",empty record @sheet-" + sheetnumber
                        + ", row-" + rw + " not added to  list.");
            }
        }

        log.info(prestr + "fetching complete, # of records fetched:-"
                + excelRecords.size() + "from file:- " + filePath 
                + "Record:" + excelRecords);
        
        return excelRecords;
    }
}
