package cr.ac.una.clinicaunaws.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import cr.ac.una.clinicaunaws.dto.ReportDto;

/**
 *
 * @author arayaroma
 */
public class ExcelGenerator {

    private static ExcelGenerator instance;

    public static ExcelGenerator getInstance() {
        if (instance == null) {
            instance = new ExcelGenerator();
        }
        return instance;
    }

    private ExcelGenerator() {
    }

    public CellStyle createStyleHeader(Workbook workbook, HorizontalAlignment horizontalAlignment, IndexedColors colors, short fontColor) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();

        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(fontColor);

        cellStyle.setFont(headerFont);
        cellStyle.setAlignment(horizontalAlignment);
        cellStyle.setFillForegroundColor(colors.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    public File generateExcelReport(ReportDto report) {
        try {
            List<String> headerNames = QueryManager.extractAlias(report.getQueryManager().getQuery());

            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Report");
            CellStyle style = createStyleHeader(workbook, HorizontalAlignment.CENTER, IndexedColors.BLUE, IndexedColors.WHITE.getIndex());

            Row headerRow = sheet.createRow(0);
            if (headerNames.size() > 1) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerNames.size() - 1));
            }
            Cell cell = headerRow.createCell(0);
            cell.setCellValue(report.getName());
            cell.setCellStyle(style);

            headerRow = sheet.createRow(1);
            style = createStyleHeader(workbook, HorizontalAlignment.CENTER, IndexedColors.BLUE_GREY, IndexedColors.WHITE.getIndex());

            for (int i = 0; i < headerNames.size(); i++) {
                cell = headerRow.createCell(i);
                cell.setCellValue(headerNames.get(i));
                cell.setCellStyle(style);
                sheet.autoSizeColumn(i);
            }
            style = createStyleHeader(workbook, HorizontalAlignment.CENTER, IndexedColors.WHITE, IndexedColors.BLACK.getIndex());
            int rowNum = 2;
            System.out.println(report.getQueryManager().getResult());
            for (Object obj : report.getQueryManager().getResult()) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                try {
                    for (Object element : (Object[]) obj) {
                        cell = row.createCell(colNum++);
                        cell.setCellValue(element != null ? element.toString() : "");
                        cell.setCellStyle(style);
                        sheet.autoSizeColumn(colNum);
                    }
                } catch (ClassCastException e) {
                    cell = row.createCell(colNum++);
                    cell.setCellValue(obj != null ? obj.toString() : "");
                    cell.setCellStyle(style);
                    sheet.autoSizeColumn(colNum);
                }
            }
            int randomNumber = (int) (Math.random() * 1000);
            String randomNumberString = String.valueOf(randomNumber).substring(0, 3);

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//            String projectRoot = System.getProperty("user.dir");
//            String outputPath = "reports/" + report.getName() + randomNumberString + "-" + currentDate + ".xlsx";
//            String filePath = projectRoot + "/" + outputPath;

            File excelFile = new File(report.getName() + randomNumberString + "-" + currentDate + ".xls");
            System.out.println(excelFile);//Se crean dentro del domain1 de payara en la carpeta config
//            excelFile.getParentFile().mkdirs();

            FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.close();
            return excelFile;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
