package com.example.mongodemo.service;

import com.example.mongodemo.model.BackerKitList;
import com.example.mongodemo.model.BackerKitNumber;
import com.example.mongodemo.model.HintGroup;
import com.example.mongodemo.repository.BackerKitNumberRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.io.File;

@Component
public class ExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private BackerKitNumberRepository backerKitNumberRepository;

    public static final String SAMPLE_XLSX_FILE_PATH = "C:\\Users\\GOPINATHAN\\Downloads\\Shipment Max Updated.xlsx";

    public static void main(String... args) throws Exception {
        ExcelService excelService = new ExcelService();
        BackerKitList backerKitList = excelService.getBackersList("");
        System.out.println(backerKitList.getBackersList().size());
    }

    public void readAndSaveAll(final String fileName) {
        try {
            BackerKitList backerKitList = this.getBackersList(fileName);
            System.out.println(backerKitList.getBackersList().size());
            backerKitNumberRepository.saveAll(backerKitList.getBackersList());
        } catch (Exception ex) {
            LOGGER.error("readAndSaveAll Error", ex);
        }
    }

    public HintGroup getHints() {
        long totalMovies = backerKitNumberRepository.count();

        return new HintGroup(totalMovies, 0, 0, 0);
    }

    public BackerKitList getBackersList(final String fileName) throws Exception {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        /*
           =============================================================
           Iterating over all the sheets in the workbook
           =============================================================
        */
        System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
        workbook.forEach(sheet -> {
            System.out.println("=> " + sheet.getSheetName());
        });
        /*
           ==================================================================
           Iterating over all the rows and columns in a Sheet
           ==================================================================
        */

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // 3. Or you can use Java 8 forEach loop with lambda
        final BackerKitList backerKitList = new BackerKitList();

        System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
        for (Row row: sheet) {
            if (row.getRowNum() == 0) continue;
            BackerKitNumber backerKitNumber = new BackerKitNumber();
            for(Cell cell: row) {
                String backerNumber = dataFormatter.formatCellValue(cell);
                backerKitNumber.setBackerNumber(backerNumber);
                //System.out.print(backerNumber + "\t");
            }
            //System.out.println();
            backerKitList.getBackersList().add(backerKitNumber);
        }

        /*System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");

        sheet.forEach(row -> {
            BackerKitNumber backerKitNumber = new BackerKitNumber();
            row.forEach(cell -> {
                String backerNumber = dataFormatter.formatCellValue(cell);
                //System.out.print(backerNumber + "\t");
                backerKitNumber.setBackerNumber(backerNumber);
            });
            backerKitList.getBackersList().add(backerKitNumber);
            //System.out.println();
        });*/

        // Closing the workbook
        workbook.close();

        return backerKitList;
    }
}
