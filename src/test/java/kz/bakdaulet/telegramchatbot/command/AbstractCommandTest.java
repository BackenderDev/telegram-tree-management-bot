package kz.bakdaulet.telegramchatbot.command;

import kz.bakdaulet.telegramchatbot.contoller.TelegramBot;
import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import kz.bakdaulet.telegramchatbot.service.impl.SendBotMessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AbstractCommandTest {
    protected TelegramBot telegramBot = Mockito.mock(TelegramBot.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(telegramBot);

    abstract String getCommandName();
    abstract String getCommandMessage();
    abstract Command getCommand();

    @Test
    public void executeCommand_shouldSendExpectedMessage() throws TelegramApiException {
        //given
        Long chatId = 1275434243257L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());

        //when
        getCommand().execute(update);

        //then
        Mockito.verify(telegramBot).execute(sendMessage);
    }

}
