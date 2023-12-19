package kz.bakdaulet.telegramchatbot.service;

public interface SendBotMessageService {
    void sendMessage(String chatId, String message);
}
