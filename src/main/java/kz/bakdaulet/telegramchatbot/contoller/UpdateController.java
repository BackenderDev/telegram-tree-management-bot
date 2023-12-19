package kz.bakdaulet.telegramchatbot.contoller;

import kz.bakdaulet.telegramchatbot.command.CommandContainer;
import kz.bakdaulet.telegramchatbot.model.ChildrenElement;
import kz.bakdaulet.telegramchatbot.model.RootElement;
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
    private static final String VIEW_TREE_ = "/viewTree";
    private static final String ADD_ELEMENT_ = "/addElement";
    private static final String REMOVE_ELEMENT_ = "/removeElement";
    private static final String STOP_ = "/stop";
    private static final String UPLOAD_ = "/uploadFile";
    private static final String DOWNLOAD_ = "/downloadFile";
    private static Boolean addStatus = false;
    private static Boolean removeStatus = false;
    private static Boolean uploadStatus = false;
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
                } else if (uploadStatus) {
                    if (message.startsWith(COMMAND_PREFIX)) {
                        uploadStatus = false;
                        sendMessage(message);
                    } else
                        upload();
                } else if (stopStatus) {
                    if (message.startsWith(COMMAND_PREFIX)) {
                        uploadStatus = false;
                        sendMessage(message);
                    } else
                        stop();
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
                case VIEW_TREE_ -> viewTree();
                case ADD_ELEMENT_ -> sendMessage("Пишите <название элемента> или <родительский элемент> <дочерний элемент>", 1) ;
                case REMOVE_ELEMENT_ -> sendMessage("Пишите <название элемента>", 2);
                case UPLOAD_ -> sendMessage("Добавляйте файл",3);
                case DOWNLOAD_ -> download();
                case STOP_ -> sendMessage("Я удалю все файлы, вы уверены в себе?", 4);
                default -> commandContainer.retrieveCommand(commandIdentifier).execute(update);
            }
    }

    private void stop() {

    }

    private void sendMessage(String message, int id){
        if(id == 1){
            addStatus = true;
        }else if (id == 2){
            removeStatus = true;
        }else if(id == 3){
            uploadStatus = true;
        }else if(id == 4){
            stopStatus = true;
        }
        sendText(message);
    }

    private void sendText(String message){
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), message);
    }
    private void download() {
        String fileName = uploadFile.writeExcel(readData());
        uploadStatus = false;
        sendBotMessageService.sendFile(update.getMessage().getChatId().toString(), fileName);
    }

    private void upload() {
        if(update.getMessage().hasDocument()){
            downloadFile.upload(telegramBot, new GetFile(update.getMessage().getDocument().getFileId()));
        }else {
            log.error("No file");
        }
    }
    private void removeElement(String message) {
        String[] addElements = message.split(" ");
        if(addElements.length == 1){
            Optional<RootElement> rootElement = rootElementService.findByName(addElements[0]);
            if(rootElement.isPresent()){
                List<ChildrenElement> childrenElements = childrenElementService.
                        findAll(rootElement.get().getId());
                childrenElements.forEach((ChildrenElement ch) -> childrenElementService.delete(ch.getId()));
                rootElementService.delete(rootElement.get().getId());
                removeStatus = false;
                commandContainer.retrieveCommand(REMOVE_ELEMENT.getCommandName()).execute(update);
            }else{
                sendMessage("В базе данных нет такого товара", 2);
            }
        }else {
            commandContainer.retrieveCommand(ANSWER_COMMAND.getCommandName()).execute(update);
        }
    }

    private void addElement(String message) {
        String[] addElements = message.split(" ");
        if(addElements.length == 1){
            Optional<RootElement> rootElement = check(addElements[0]);
            if (rootElement.isEmpty()) {
                rootElementService.save(new RootElement(addElements[0]));
                addStatus = false;
                commandContainer.retrieveCommand(ADD_ELEMENT.getCommandName()).execute(update);
            }else{
                sendMessage("В базе данных такого родительского товар есть", 1);
            }
        }else if (addElements.length == 2){
            Optional<RootElement> rootElement = check(addElements[0]);
            if(rootElement.isPresent()){
                Optional<ChildrenElement> childrenElement = childrenElementService.findByName(addElements[1]);
                if(childrenElement.isPresent()){
                    sendMessage("В базе данных такого товар есть", 1);
                }else {
                    childrenElementService.save(new ChildrenElement(addElements[1], rootElement.get()));
                    addStatus = false;
                    commandContainer.retrieveCommand(ADD_ELEMENT.getCommandName()).execute(update);
                }
            }else {
                sendMessage("В базе данных нет такого родительского товара", 1);
            }
        }else{
            commandContainer.retrieveCommand(ANSWER_COMMAND.getCommandName()).execute(update);
        }
    }
    private Optional<RootElement> check(String root){
        return rootElementService.findByName(root);
    }
    public void add(Map<String, List<String>> map){
        for (Map.Entry<String, List<String>> entry : map.entrySet()){
            Optional<RootElement> rootElement = check(entry.getKey());
            if(rootElement.isEmpty()){
                rootElementService.save(new RootElement(entry.getKey()));
            }else
                for (String s : entry.getValue()){
                    Optional<ChildrenElement> childrenElement = childrenElementService.findByName(s);
                    if (childrenElement.isEmpty()) {
                        childrenElementService.save(new ChildrenElement(s, rootElement.get()));
                    }
                }
        }
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Все товары добавлены");
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
        String id = update.getMessage().getChatId().toString();
        if (stringBuilder.isEmpty()){
            sendBotMessageService.sendMessage(id, "Никакой товар нету");
        }else
            sendBotMessageService.sendMessage(id, stringBuilder.toString());
    }
    private Map<String, List<String>> readData(){
        Map<String, List<String>> map = new TreeMap<>();
        List<RootElement> root = rootElementService.findAll();
        List<ChildrenElement> childList;
        List<String> childNames;
        for (RootElement rootElement : root) {
            childList = childrenElementService.findAll(rootElement.getId());
            childNames = new ArrayList<>();
            for(ChildrenElement ch : childList){
                childNames.add(ch.getName());
            }
            map.put(rootElement.getName(), childNames);
        }
        return map;
    }
}
