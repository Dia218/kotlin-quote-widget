package repository;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import model.Quote;

public class JsonQuoteRepository implements QuoteRepository {
    private static final String DB_DIRECTORY = "./db/quotes/";
    private final Gson gson = new Gson();
    
    @Override
    public void insertQuote(Quote quote) throws IOException {
        if(quote.getId() != getLastId()+1) {
            throw new IOException();
        }
        String json = gson.toJson(quote);
        Path path = Paths.get(DB_DIRECTORY + quote.getId() + ".json");
        Files.createDirectories(path.getParent());
        Files.writeString(path, json);
        updateLastId(+1);
    }
    
    @Override
    public void deleteQuote(int quoteId) throws IOException {
        if(quoteId == getLastId()) {
            updateLastId(-1);
        }
        Path path = Paths.get(DB_DIRECTORY + quoteId + ".json");
        Files.deleteIfExists(path);
    }
    
    @Override
    public void updateQuote(Quote quote, String author, String content) throws IOException {
        quote.update(author, content);
        String json = gson.toJson(quote);
        Path path = Paths.get(DB_DIRECTORY + quote.getId() + ".json");
        Files.writeString(path, json);
    }
    
    @Override
    public List<Quote> selectAllQuotes() throws IOException {
        List<Quote> quotes = new ArrayList<>();
        handleQuoteFiles(quotes);
        return quotes;
    }
    
    @Override
    public Quote selectQuoteById(int quoteId) throws IOException {
        Path path = Paths.get(DB_DIRECTORY + quoteId + ".json");
        if (Files.exists(path)) {
            String json = new String(Files.readAllBytes(path));
            return gson.fromJson(json, Quote.class);
        }
        return null;
    }
        
    @Override
    public void buildFile() throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        
        handleQuoteFiles(jsonBuilder);
        
        if (jsonBuilder.length() > 1) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        jsonBuilder.append("]");
        
        Path path = Paths.get(DB_DIRECTORY + "data.json");
        Files.createDirectories(path.getParent());
        Files.writeString(path, jsonBuilder.toString());
    }
    
    @Override
    public int getLastId() throws IOException {
        return Integer.parseInt(new String(Files.readAllBytes(Paths.get(DB_DIRECTORY + "lastId.txt"))));
    }
    
    private void handleQuoteFiles(Object parameter) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(DB_DIRECTORY))) {
            for (Path entry : stream) {
                if (entry.getFileName().toString().matches("\\d+\\.json$")) {
                    String quoteJson = new String(Files.readAllBytes(entry));
                    handleJsonString(parameter, quoteJson);
                }
            }
        }
    }
    
    private void handleJsonString(Object parameter, String quoteJson) {
        if (parameter instanceof List<?>) {
            List<Quote> list = (List<Quote>) parameter;
            list.add(gson.fromJson(quoteJson, Quote.class));
        }
        if(parameter instanceof StringBuilder jsonBuilder) {
            jsonBuilder.append(quoteJson);
            jsonBuilder.append(",");
        }
    }
    
    private void updateLastId(int num) throws IOException {
        Path path = Paths.get(DB_DIRECTORY + "lastId.txt");
        int updatedId = Integer.parseInt(Files.readString(path).trim()) + num;
        Files.writeString(path, Integer.toString(updatedId));
    }

}
