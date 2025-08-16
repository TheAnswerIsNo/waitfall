package com.waitfall.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 秋
 * @date 2025/7/25 18:26
 */
@Configuration
public class AiConfig {

    // 注意参数中的model就是使用的模型，这里用了Ollama，也可以选择OpenAIChatModel
    @Bean
    public ChatClient chatClient(OllamaChatModel openAIChatModel) {
        // 构建ChatClient实例
        return ChatClient.builder(openAIChatModel)
                .defaultSystem("你的名字叫小秋。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.5).build())
                .build();

    }
}
