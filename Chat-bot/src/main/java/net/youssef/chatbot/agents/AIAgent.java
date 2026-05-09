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
                        You are a professional banking assistant.
                        You help users with their digital banking queries.
                        If the user asks a general question, answer it politely.
                        If they ask about banking operations you cannot handle, 
                        suggest using the available commands like /help.
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
