package net.youssef.chatbot.telegram;

import jakarta.annotation.PostConstruct;
import net.youssef.chatbot.agents.AIAgent;
import net.youssef.chatbot.integration.BankingCommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    @Value("${telegram.api.key}")
    private String telegramBotToken;

    @Value("${telegram.bot.username:BankinAIBot}")
    private String telegramBotUsername;

    private AIAgent aiAgent;
    private BankingCommandHandler commandHandler;

    public TelegramBot(AIAgent aiAgent, BankingCommandHandler commandHandler) {
        this.aiAgent = aiAgent;
        this.commandHandler = commandHandler;
    }

    @PostConstruct
    public void registerTelegramBot(){
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiRequestException e) {
            if (e.getMessage() != null && e.getMessage().contains("404")) {
                log.warn("Telegram webhook cleanup returned 404; continuing startup.", e);
            } else {
                throw new RuntimeException(e);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update telegraRequest) {
        try {
            if (!telegraRequest.hasMessage() || telegraRequest.getMessage().getText() == null) {
                return;
            }
            String messageText = telegraRequest.getMessage().getText().trim();
            Long chatId = telegraRequest.getMessage().getChatId();
            sendTypingQuestion(chatId);
            String answer = commandHandler.handle(messageText);
            if (answer == null) {
                answer = aiAgent.askAgent(messageText);
            }
            sendTextMessage(chatId, answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        execute(sendMessage);
    }
    private void sendTypingQuestion(long chatId) throws TelegramApiException {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chatId));
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);
    }
}