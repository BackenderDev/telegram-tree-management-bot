package kz.bakdaulet.telegramchatbot.command;

import org.junit.jupiter.api.DisplayName;
import static kz.bakdaulet.telegramchatbot.command.CommandName.ADD_ELEMENT;
import static kz.bakdaulet.telegramchatbot.command.AddElementCommand.ANSWER_MESSAGE_FOR_ADDING;
@DisplayName("Unit-level testing for AddElementCommand")
public class AddElementCommandTest extends AbstractCommandTest{
    @Override
    String getCommandName() {
        return ADD_ELEMENT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return ANSWER_MESSAGE_FOR_ADDING;
    }

    @Override
    Command getCommand() {
        return new AddElementCommand(sendBotMessageService);
    }
}
