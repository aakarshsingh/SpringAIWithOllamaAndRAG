package aakarsh.ollamarag.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class VectorDbConfig {
    @Value("vector_db.json")
    private String vectorDb;

    @Value("classpath:/data/faq.txt")
    Resource faqResource;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        File vectorDbFile = getVectorDbFile();
        if (vectorDbFile.exists()) {
            vectorStore.load(vectorDbFile);
        } else {
            TextReader reader = new TextReader(faqResource);
            List<Document> documents = reader.get();
            TextSplitter textSplitter = new TokenTextSplitter();

            List<Document> splitDocuments = textSplitter.apply(documents);
            vectorStore.add(splitDocuments);
            vectorStore.save(vectorDbFile);
        }
        return vectorStore;
    }

    private File getVectorDbFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorDb;
        return new File(absolutePath);
    }
}
