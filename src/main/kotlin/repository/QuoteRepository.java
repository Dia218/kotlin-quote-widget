package repository;

import java.io.IOException;
import java.util.List;

import model.Quote;

public interface QuoteRepository {
    void insertQuote(Quote quote) throws IOException;
    void deleteQuote(int quoteId) throws IOException;
    void updateQuote(Quote quote, String author, String content) throws IOException;
    List<Quote> selectAllQuotes() throws IOException;
    Quote selectQuoteById(int quoteId) throws IOException;
    void buildFile() throws IOException;
    int getLastId() throws IOException;
}
