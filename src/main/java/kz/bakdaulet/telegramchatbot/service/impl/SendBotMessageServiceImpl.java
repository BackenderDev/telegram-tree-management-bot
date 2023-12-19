package kz.bakdaulet.telegramchatbot.service.impl;

import kz.bakdaulet.telegramchatbot.contoller.TelegramBot;
import kz.bakdaulet.telegramchatbot.service.SendBotMessageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Log4j
public class SendBotMessageServiceImpl implements SendBotMessageService {
    private final TelegramBot telegramBot;

    @Autowired
    public SendBotMessageServiceImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e){
            log.error(e);
        }
    }
    public void sendFile(String chatId, String fileName) {
            SendDocument sendDocument = new SendDocument(chatId, new InputFile(new java.io.File(fileName)));
            try {
                telegramBot.execute(sendDocument);
            } catch (TelegramApiException e) {
                log.error("Error sending file", e);
            }
        }
}
