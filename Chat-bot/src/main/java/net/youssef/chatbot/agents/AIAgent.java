package net.youssef.chatbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AIAgent {
    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder,
                   ChatMemory memory, ObjectProvider<ToolCallbackProvider> toolsProvider) {
        ToolCallbackProvider tools = toolsProvider.getIfAvailable();

        if (tools != null) {
            Arrays.stream(tools.getToolCallbacks()).forEach(toolCallback -> {
                System.out.println("----------------------");
                System.out.println(toolCallback.getToolDefinition());
                System.out.println("----------------------");
            });
        }

        ChatClient.Builder chatBuilder = builder
                .defaultSystem("""
                        Vous un assistant qui se charge de répondre aux question
                        de l'utilisateur en fonction du contexte fourni.
                        Si aucun contexte n'est fourni, répond avec JE NE SAIS PAS         
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build());

        if (tools != null) {
            chatBuilder = chatBuilder.defaultToolCallbacks(tools);
        }

        this.chatClient = chatBuilder.build();
    }
    public String askAgent(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .content();
    }
}
