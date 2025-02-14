package repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Quote;

public class MapQuoteRepository implements QuoteRepository {
    private final Map<Integer, Quote> quotes = new LinkedHashMap<>();
    
    
    @Override
    public void insertQuote(Quote quote) {
        quotes.put(quote.getId(), quote);
    }
    
    @Override
    public void deleteQuote(int quoteId) {
        if(!quotes.containsKey(quoteId)) {
            return;
        }
        quotes.remove(quoteId);
    }
    
    @Override
    public void updateQuote(Quote quote, String author, String content) {
        quote.update(author, content);
    }
    
    @Override
    public List<Quote> selectAllQuotes() {
        return quotes.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
    @Override
    public Quote selectQuoteById(int quoteId) {
        if(!quotes.containsKey(quoteId)) {
            return null;
        }
        return quotes.get(quoteId);
    }
    
    @Override
    public void buildFile() {
    }
    
    @Override
    public int getLastId() {
        return quotes.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
    }
}
