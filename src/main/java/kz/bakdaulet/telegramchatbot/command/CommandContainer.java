package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;

import java.util.Map;

import static kz.bakdaulet.telegramchatbot.command.CommandName.*;

public class CommandContainer {
    public static Map<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService){
        commandMap = Map.of(
                START.getCommandName(), new StartCommand(sendBotMessageService),
                STOP.getCommandName(), new StopCommand(sendBotMessageService),
                HELP.getCommandName(), new HelpCommand(sendBotMessageService),
                NO.getCommandName(), new NoCommand(sendBotMessageService),
                VIEW_TREE.getCommandName(), new ViewTreeCommand(sendBotMessageService),
                REMOVE_ELEMENT.getCommandName(), new RemoveElementCommand(sendBotMessageService),
                DOWNLOAD.getCommandName(), new DownloadCommand(sendBotMessageService),
                UPLOAD.getCommandName(), new UploadCommand(sendBotMessageService)
        );
        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier){
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }


}
