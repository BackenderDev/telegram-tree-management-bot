package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DownloadCommand implements Command {
    public static final String DOWNLOAD_MESSAGE = "";

    private final SendBotMessageService sendBotMessageService;

    public DownloadCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), DOWNLOAD_MESSAGE);
    }
}
