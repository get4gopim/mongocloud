package com.example.mongodemo.service;

import com.example.mongodemo.model.*;
import com.example.mongodemo.repository.BackerKitNumberRepository;
import com.example.mongodemo.repository.ContributionNumberRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ContributionNumberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContributionNumberService.class);

    @Autowired
    private ContributionNumberRepository contributionNumberRepository;

    public static final String SAMPLE_XLSX_FILE_PATH = "C:\\Users\\GOPINATHAN\\Downloads\\Shipment Max Updated (2).xlsx";

    public static void main(String... args) throws Exception {
        ContributionNumberService excelService = new ContributionNumberService();
        ContributionNumberList backerKitList = excelService.getBackersList(SAMPLE_XLSX_FILE_PATH);
        System.out.println(backerKitList.getBackersList().size());

        //excelService.readAndSaveAll(SAMPLE_XLSX_FILE_PATH);
    }

    public void readAndSaveAll(final String fileName) {
        try {
            ContributionNumberList backerKitList = this.getBackersList(fileName);
            System.out.println(backerKitList.getBackersList().size());

            for (ContributionNumber backerKitNumber : backerKitList.getBackersList()) {
                try {
                    contributionNumberRepository.save(backerKitNumber);
                } catch (Exception ex) {
                    LOGGER.error("readAndSaveAll Error", ex);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("readAndSaveAll Error", ex);
        }
    }

    public HintGroup getHints() {
        long totalMovies = contributionNumberRepository.count();

        return new HintGroup(totalMovies, 0, 0, 0);
    }

    public ContributionNumberList getBackersList(final String fileName) throws Exception {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(fileName));

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
        final ContributionNumberList backerKitList = new ContributionNumberList();

        System.out.println("\nIterating over Rows and Columns using for-each loop");
        for (Row row: sheet) {
            if (row.getRowNum() == 0) continue;
            ContributionNumber backerKitNumber = new ContributionNumber();
            for(Cell cell: row) {
                String backerNumber = dataFormatter.formatCellValue(cell);
                backerKitNumber.setContributionNumber(backerNumber);
                //System.out.println(backerNumber + "\t");
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
