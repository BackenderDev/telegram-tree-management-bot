package kz.bakdaulet.telegramchatbot.contoller;

import kz.bakdaulet.telegramchatbot.exception.UploadFileException;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Log4j
public class UploadFile {
    public String writeExcel(Map<String, List<String>> data) {
        File file = new File("TelegramChatBot");
        String filePath = file.getAbsolutePath().replace("TelegramChatBot\\", "") + "/src/uploadFiles/" + generateFileName();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            int rowNum = 0;
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                row.createCell(colNum++).setCellValue(entry.getKey());
                for (String value : entry.getValue()) {
                    row.createCell(colNum++).setCellValue(value);
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }catch (UploadFileException e){
                log.error(e);
            }
        } catch (IOException e) {
            log.error(e);
        }
        return filePath;
    }
    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateFormat.format(new Date());
        return "upload_excel_file_" + timeStamp + ".xlsx";
    }
}
