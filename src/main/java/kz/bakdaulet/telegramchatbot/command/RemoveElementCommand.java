package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RemoveElementCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    public final static String ANSWER_MESSAGE_FOR_REMOVING = "Товар удален.\n" +
                                                              "Вы можете посмотреть через /viewTree";

    public RemoveElementCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }
    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), ANSWER_MESSAGE_FOR_REMOVING);
    }
}
