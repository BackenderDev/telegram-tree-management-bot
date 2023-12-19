package kz.bakdaulet.telegramchatbot.command;

import org.junit.jupiter.api.DisplayName;
import static kz.bakdaulet.telegramchatbot.command.CommandName.REMOVE_ELEMENT;
import static kz.bakdaulet.telegramchatbot.command.RemoveElementCommand.ANSWER_MESSAGE_FOR_REMOVING;
@DisplayName("Unit-level testing for StartCommand")
public class RemoveElementCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return REMOVE_ELEMENT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return ANSWER_MESSAGE_FOR_REMOVING;
    }

    @Override
    Command getCommand() {
        return new RemoveElementCommand(sendBotMessageService);
    }
}
