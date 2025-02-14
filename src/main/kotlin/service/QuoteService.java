package service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import exception.QuoteNotFoundException;
import exception.QuotesFileAccessException;
import model.Quote;
import repository.QuoteRepository;

public class QuoteService {
    private final QuoteRepository quoteRepository;
    private static final Logger logger = Logger.getLogger(QuoteService.class.getName());
    
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }
    
    public int addQuote(String author, String content)  {
        int id = getNextId();
        try {
            quoteRepository.insertQuote(new Quote(id, author, content));
        } catch (IOException e) {
            throw new QuotesFileAccessException("insert");
        }
        return id;
    }
    
    public void deleteQuote(Quote quote) {
        try {
            quoteRepository.deleteQuote(quote.getId());
        } catch (IOException e) {
            throw new QuotesFileAccessException("delete");
        }
    }
    
    public void updateQuote(Quote quote, String author, String content) {
        try {
            quoteRepository.updateQuote(quote, author, content);
        } catch (IOException e) {
            throw new QuotesFileAccessException("update");
        }
    }
    
    public List<String> listQuotes() {
        try {
            return quoteRepository.selectAllQuotes().stream()
                    .map(Quote::getInfo)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuotesFileAccessException("list");
        }
    }
    
    public void buildQuotes() {
        try {
            quoteRepository.buildFile();
        } catch (IOException e) {
            throw new QuotesFileAccessException("build");
        }
    }
    
    public Quote getQuoteById(int targetId) {
        Quote quote = null;
        try {
            quote = quoteRepository.selectQuoteById(targetId);
        } catch (IOException e) {
            throw new QuotesFileAccessException("select");
        }
        if (quote == null) {
            throw new QuoteNotFoundException(targetId);
        }
        return quote;
    }
    
    private int getNextId() {
        try {
            return quoteRepository.getLastId() + 1;
        } catch (IOException e) {
            throw new QuotesFileAccessException("get last id");
        }
    }
    
}