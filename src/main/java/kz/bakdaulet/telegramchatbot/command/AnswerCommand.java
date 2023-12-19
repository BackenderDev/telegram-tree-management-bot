package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AnswerCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final static String ANSWER_MESSAGE = """
            Неправильный текст
            Вот таком формате пиши
            /addElement <название элемента>
            /removeElement <название элемента>
            /download cкачивает Excel документ с деревом категорий
            /upload принимает Excel документ с деревом категорий и сохраняет все элементы в базе данных""";

    public AnswerCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }
    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),ANSWER_MESSAGE);
    }
}
