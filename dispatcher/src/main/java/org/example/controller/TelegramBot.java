package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Log4j
@Component
public class TelegramBot extends TelegramWebhookBot {    // extends TelegramLongPollingBot

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.uri}")
    private String botUri;

    private UpdateProcessor updateProcessor;

    public TelegramBot(UpdateProcessor updateProcessor) {   //TelegramBot внедряется ссылкой на UpdateController, после чего выполнится метод init() так как отмечен анотацией @PostConstruct
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() {                               //В методе мы передаем ссылку на сам TelegramBot внутрь UpdateController-а
        updateProcessor.registerBot(this);  //Таким образом TelegramBot сможет передать входящее сообщение в контроллер, а контроллер сможет передать ответ обратно в TelegramBot
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            this.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

/*    @Override
    public void onUpdateReceived(Update update) {
        Message originalMessage = update.getMessage();
        updateProcessor.processUpdate(update);
    }*/

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }
}
