package net.youssef.chatbot.web;

import net.youssef.chatbot.agents.AIAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@CrossOrigin("*")
public class ChatController {
    private AIAgent aiAgent;
    private net.youssef.chatbot.integration.BankingCommandHandler bankingCommandHandler;

    public ChatController(AIAgent aiAgent, net.youssef.chatbot.integration.BankingCommandHandler bankingCommandHandler) {
        this.aiAgent = aiAgent;
        this.bankingCommandHandler = bankingCommandHandler;
    }
    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam(name = "query") String query) {
        String commandResult = bankingCommandHandler.handle(query);
        if (commandResult != null) return commandResult;
        return aiAgent.askAgent(query);
    }


}
