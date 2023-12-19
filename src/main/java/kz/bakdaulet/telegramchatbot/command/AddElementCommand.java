package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AddElementCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final static String ANSWER_MESSAGE_FOR_ADDING = "Товар добавлен.\n" +
                                                            "Можете посмотреть через /viewTree";

    public AddElementCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), ANSWER_MESSAGE_FOR_ADDING);
    }
}
