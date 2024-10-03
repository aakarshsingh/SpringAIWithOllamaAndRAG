package aakarsh.ollamarag.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaController {


    private final ChatClient chatClient;

    public OllamaController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();
    }


    @GetMapping("/answer")
    public String get(@RequestParam(value = "question", defaultValue = "Where are the 2048 Olympics being held?") String question) {
        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }
}
