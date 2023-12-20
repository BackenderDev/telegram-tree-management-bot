package kz.bakdaulet.telegramchatbot.contoller;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
@Component
public class DownloadFile {
    private final String botToken;
    private final UpdateController updateController;

    @Autowired
    public DownloadFile(@Value("${bot.token}") String botToken, UpdateController updateController) {
        this.botToken = botToken;
        this.updateController = updateController;
    }

    @SneakyThrows
    public void upload(TelegramBot telegramBot, GetFile file){
        File file1 = telegramBot.execute(file);
        if (isExcelFile(file1)){
            downloadExcelFile(telegramBot.execute(file));
        }else
            updateController.sendText("Вы ввели файл другого формата. Я принимаю файл только в формате \"xlsx\".");
    }
    public boolean isExcelFile(File file){
        String fileName = file.getFilePath();
        return fileName != null && fileName.toLowerCase().endsWith(".xlsx");
    }

    private void downloadExcelFile(File file){
        try {
            InputStream fileInputStream = downloadFileByFilePath(file.getFilePath());
            Workbook workbook = new XSSFWorkbook(Objects.requireNonNull(fileInputStream));
            readCategoriesFromWorkbook(workbook);
        } catch (IOException e) {
            log.error(e);
        }
    }
    private InputStream downloadFileByFilePath(String filePath) {
        try {
            java.io.File downloadedFile = downloadFileFromTelegram(filePath);
            return new FileInputStream(Objects.requireNonNull(downloadedFile));
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
    private java.io.File downloadFileFromTelegram(String filePath) {
        try {
            URL url = new URL("https://api.telegram.org/file/bot" + botToken + "/" + filePath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());

            java.io.File downloadedFile = new java.io.File("src/main/resources/file/downLoadFiles/"+generateFileName());
            FileOutputStream out = new FileOutputStream(downloadedFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();
            in.close();
            connection.disconnect();

            return downloadedFile;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
    public void readCategoriesFromWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, List<String>> map = new LinkedHashMap<>();
        if (sheet != null) {
            for (Row row : sheet) {
                Cell rootCell = row.getCell(0);
                Cell childCell = row.getCell(1);
                if(rootCell != null && childCell != null){
                    String root = rootCell.getStringCellValue();
                    String child = childCell.getStringCellValue();
                    List<String> children;
                    if(!map.containsKey(root)){
                        children = new ArrayList<>();
                    }else{
                        children = map.get(root);
                    }
                    children.add(child);
                    map.put(root, children);
                }else if(rootCell != null){
                    String root = rootCell.getStringCellValue();
                    if(!map.containsKey(root)){
                        map.put(root, new ArrayList<>());
                    }
                }
            }
            updateController.add(map);
        }
    }
    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateFormat.format(new Date());
        return "download_excel_file_" + timeStamp + ".xlsx";
    }
}
