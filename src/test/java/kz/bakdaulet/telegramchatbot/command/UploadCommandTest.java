package kz.bakdaulet.telegramchatbot.command;

import org.junit.jupiter.api.DisplayName;

import static kz.bakdaulet.telegramchatbot.command.CommandName.UPLOAD;
import static kz.bakdaulet.telegramchatbot.command.UploadCommand.UPLOAD_MESSAGE;

@DisplayName("Unit-level testing for UploadCommand")
public class UploadCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return UPLOAD.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return UPLOAD_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UploadCommand(sendBotMessageService);
    }
}
