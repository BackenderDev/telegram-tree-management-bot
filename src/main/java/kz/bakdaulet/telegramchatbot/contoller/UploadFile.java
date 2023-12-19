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
        String filePath = file.getAbsolutePath().replace("TelegramChatBot\\", "") + "/src/main/resources/file/uploadFiles/" + generateFileName();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();

            int rowNum = 0;
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    for (String value : entry.getValue()) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(entry.getKey());
                        row.createCell(1).setCellValue(value);
                    }
                }else{
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(entry.getKey());
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }catch (UploadFileException e){
                log.error(e);
                throw new UploadFileException("К сожалению, файл не загружается", e);
            }
        } catch (IOException e) {
            log.error(e);
            throw new UploadFileException(e);
        }
        return filePath;
    }
    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateFormat.format(new Date());
        return "upload_excel_file_" + timeStamp + ".xlsx";
    }
}
