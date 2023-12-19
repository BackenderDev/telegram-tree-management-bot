package kz.bakdaulet.telegramchatbot.contoller;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    private final UpdateController updateController;

    @PostConstruct
    private void init(){
        updateController.registerBot(this);
    }

    public TelegramBot(@Value("${bot.token}")String botToken, UpdateController updateController){
        super(botToken);
        this.updateController = updateController;
    }
    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
