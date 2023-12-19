package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.bakdaulet.telegramchatbot.command.CommandName.*;

public class HelpCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    public static final String HELP_MESSAGE = String.format("""
                    Доступные команды

                    %s - просмотр дерева
                    %s - добавить элемент: <название элемента> или <родительский элемент> <дочерний элемент>
                    %s - удалить элемент: <название элемента>
                    %s - cкачивает Excel документ с деревом категорий
                    %s - принимает Excel документ с деревом категорий и сохраняет все элементы в базе данных

                    %s - приостановить работу со мной
                    
                    %s - получить помощь в работе со мной
                    """,
            VIEW_TREE.getCommandName(),ADD_ELEMENT.getCommandName(),
            REMOVE_ELEMENT.getCommandName(),DOWNLOAD.getCommandName(),
            UPLOAD.getCommandName(), STOP.getCommandName(),
            HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
