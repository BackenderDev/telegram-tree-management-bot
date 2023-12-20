package kz.bakdaulet.telegramchatbot.contoller;

import kz.bakdaulet.telegramchatbot.command.CommandContainer;
import kz.bakdaulet.telegramchatbot.service.ChildrenElementService;
import kz.bakdaulet.telegramchatbot.service.RootElementService;
import kz.bakdaulet.telegramchatbot.service.impl.SendBotMessageServiceImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.*;
import java.util.*;
import static kz.bakdaulet.telegramchatbot.command.CommandName.*;

@Component
@Log4j
public class UpdateController {
    private static final String COMMAND_PREFIX = "/";
    private static final String VIEW_TREE_COMMAND = "/viewTree";
    private static final String ADD_ELEMENT_COMMAND = "/addElement";
    private static final String REMOVE_ELEMENT_COMMAND = "/removeElement";
    private static final String STOP_COMMAND = "/stop";
    private static final String UPLOAD_COMMAND = "/uploadFile";
    private static final String DOWNLOAD_COMMAND = "/downloadFile";
    private static Boolean addStatus = false;
    private static Boolean removeStatus = false;
    private static Boolean stopStatus = false;
    private CommandContainer commandContainer;
    private final RootElementService rootElementService;
    private final ChildrenElementService childrenElementService;
    private SendBotMessageServiceImpl sendBotMessageService;
    private DownloadFile downloadFile;
    private UploadFile uploadFile;
    private Update update;

    @Value("${bot.token}")
    private String botToken;

    private TelegramBot telegramBot;
    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
        this.downloadFile = new DownloadFile(botToken, this);
        this.uploadFile = new UploadFile();
        this.sendBotMessageService = new SendBotMessageServiceImpl(telegramBot);
        this.commandContainer = new CommandContainer(sendBotMessageService);
    }

    @Autowired
    public UpdateController(RootElementService rootElementService,
                            ChildrenElementService childrenElementService){
        this.rootElementService = rootElementService;
        this.childrenElementService = childrenElementService;
    }

    public void processUpdate(Update update){
        this.update = update;
        if(update.hasMessage()){
            if (update.getMessage().hasDocument()){
                upload();
            }else {
                String message = update.getMessage().getText().trim();
                if (addStatus) {
                    if (message.startsWith(COMMAND_PREFIX)) {
                        addStatus = false;
                        sendMessage(message);
                    } else
                        addElement(message);
                } else if (removeStatus) {
                    if (message.startsWith(COMMAND_PREFIX)) {
                        removeStatus = false;
                        sendMessage(message);
                    } else
                        removeElement(message);
                } else if (stopStatus) {
                    if (message.startsWith(COMMAND_PREFIX)) {
                        stopStatus = false;
                        sendMessage(message);
                    } else
                        stop(message);
                } else if (message.startsWith(COMMAND_PREFIX)) {
                    sendMessage(message);
                } else {
                    commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
                }
            }
        }else{
            log.error("qwerty");
        }
    }

    private void sendMessage(String message){
        String commandIdentifier = message.split(" ")[0];
            switch (commandIdentifier){
                case VIEW_TREE_COMMAND -> viewTree();
                case ADD_ELEMENT_COMMAND -> sendMessage("Пишите <название элемента> или <родительский элемент> <дочерний элемент>", 1) ;
                case REMOVE_ELEMENT_COMMAND -> sendMessage("Пишите <название элемента>", 2);
                case UPLOAD_COMMAND -> sendText("Я принимаю файл только в формате \"xlsx\".\nДобавляйте файл:");
                case DOWNLOAD_COMMAND -> download();
                case STOP_COMMAND -> sendMessage("Я удалю все файлы, вы уверены в себе? Если вы уверен себе пишите Да или Нет", 3);
                default -> commandContainer.retrieveCommand(commandIdentifier).execute(update);
            }
    }

    private void stop(String message) {
        if (message.equals("Да")){
            childrenElementService.deleteAll();
            rootElementService.deleteAll();
            sendText("Нажмите /help, чтобы просмотреть команды");
        }else if (message.equals("Нет")){
            stopStatus = false;
            sendText("Нажмите /help, чтобы просмотреть команды");
        }else{
            sendText("Пишите Да или Нет");
        }
    }

    private void sendMessage(String message, int id){
        if(id == 1){
            addStatus = true;
        }else if (id == 2){
            removeStatus = true;
        }else if(id == 3){
            stopStatus = true;
        }
        sendText(message);
    }

    public void sendText(String message){
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), message);
    }
    private void download() {
        if (readData().isEmpty()){
            sendText("Нет товара\nНажмите /help, чтобы просмотреть команды");
        }else {
            String fileName = uploadFile.writeExcel(readData());
            sendBotMessageService.sendFile(update.getMessage().getChatId().toString(), fileName);
            sendText("Нажмите /help, чтобы просмотреть команды");
        }
    }

    private void upload() {
        if(update.getMessage().hasDocument()){
            downloadFile.upload(telegramBot, new GetFile(update.getMessage().getDocument().getFileId()));
        }else {
            log.error("The file did not arrive");
        }
    }
    private void removeElement(String message) {
        String[] removeElements = message.split(" ");
        if(removeElements.length == 1){
            if(childrenElementService.delete(removeElements[0])){
                removeStatus = false;
                commandContainer.retrieveCommand(REMOVE_ELEMENT.getCommandName()).execute(update);
            }else{
                sendMessage("В базе данных нет такого товара", 2);
            }
        }else {
            sendText("Введите в правильном формате:\n" + "<название элемента> или <родительский элемент> <дочерний элемент>");
        }
    }

    private void addElement(String message) {
        String[] addElements = message.split(" ");
        if(addElements.length == 1){
            if (rootElementService.save(addElements[0])) {
                addStatus = false;
                commandContainer.retrieveCommand(ADD_ELEMENT.getCommandName()).execute(update);
            }else{
                sendMessage("В базе данных такого родительского товар есть", 1);
            }
        }else if (addElements.length == 2){
            if(rootElementService.check(addElements[0]) != null){
                if(childrenElementService.save(addElements[1], rootElementService.check(addElements[0]))){
                    addStatus = false;
                    commandContainer.retrieveCommand(ADD_ELEMENT.getCommandName()).execute(update);
                } else {
                    sendMessage("В базе данных такого товар есть", 1);
                }
            }else {
                sendMessage("В базе данных нет такого родительского товара\nВы можете посмотреть через /viewTree", 1);
            }
        }else{
            sendText("Введите в правильном формате:\n" + "<название элемента>");
        }
    }
    public void add(Map<String, List<String>> map){
        childrenElementService.saveAllData(map);
        sendText("Все товары добавлены.\nВы можете посмотреть через /viewTree");
    }

    private Map<String, List<String>> readData(){
        return childrenElementService.readData();
    }

    private void viewTree() {
        Map<String, List<String>> map = readData();
        StringBuilder stringBuilder = new StringBuilder();
        int index = 1;
        for (Map.Entry<String, List<String>> entry : map.entrySet()){
            stringBuilder.append(index).append(". ").append(entry.getKey()).append("\n");
            for (int i = 0; i < entry.getValue().size(); i++) {
                stringBuilder.append("   -").append(index).append(".").append(i+1)
                                .append(" ").append(entry.getValue().get(i)).append("\n");
            }
            index++;
        }
        if (stringBuilder.isEmpty()){
            sendText("Нет товара\nНажмите /help, чтобы просмотреть команды");
        }else {
            stringBuilder.append("\n\n").append("Нажмите /help, чтобы просмотреть команды");
            sendText(stringBuilder.toString());
        }
    }

}
