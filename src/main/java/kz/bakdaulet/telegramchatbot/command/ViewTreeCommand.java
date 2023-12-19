package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ViewTreeCommand implements Command{
    private final SendBotMessageService sendBotMessageService;

    public ViewTreeCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "");
    }
}
