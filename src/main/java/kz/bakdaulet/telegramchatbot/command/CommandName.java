package kz.bakdaulet.telegramchatbot.command;

import lombok.Getter;

@Getter
public enum CommandName {
    START("/start"),
    STOP("/stop"),
    NO("nocommand"),
    HELP("/help"),
    VIEW_TREE("/viewTree"),
    ADD_ELEMENT("/addElement"),
    REMOVE_ELEMENT("/removeElement"),
    DOWNLOAD("/downloadFile"),
    UPLOAD("/uploadFile");
    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

}
