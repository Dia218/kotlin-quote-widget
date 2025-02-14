package service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.QuoteNotFoundException;
import model.Quote;
import repository.MapQuoteRepository;
import repository.QuoteRepository;

class QuoteServiceTest {
    private static final String AUTHOR_1 = "작가명1";
    private static final String QUOTE_CONTENT_1 = "명언내용1";
    private static final String AUTHOR_2 = "작가명2";
    private static final String QUOTE_CONTENT_2 = "명언내용2";
    private static final int QUOTE_ID_1 = 1;
    private static final int QUOTE_ID_2 = 2;
    
    private QuoteService quoteService;
    private QuoteRepository quoteRepository;
    
    @BeforeEach
    void setUp() {
        quoteRepository = new MapQuoteRepository();
        quoteService = new QuoteService(quoteRepository);
    }
    
    @Test
    void testAddQuote() throws IOException {
        int id = quoteService.addQuote(AUTHOR_1, QUOTE_CONTENT_1);
        Quote actualQuote = quoteRepository.selectQuoteById(id);
        
        String expectedQuoteInfo = QUOTE_ID_1 + " / " + AUTHOR_1 + " / " + QUOTE_CONTENT_1;
        
        assertNotNull(actualQuote, "저장된 명언이 존재하지 않음");
        assertEquals(expectedQuoteInfo, actualQuote.getInfo(), "명언 객체가 다름");
    }
    
    @Test
    void testDeleteQuote() throws IOException {
        int id = quoteService.addQuote(AUTHOR_1, QUOTE_CONTENT_1);
        Quote addedQuote = quoteRepository.selectQuoteById(id);
        quoteService.deleteQuote(addedQuote);
        
        Quote deletedQuote = quoteRepository.selectQuoteById(id);
        assertNull(deletedQuote, "삭제된 명언이 존재함");
    }
    
    @Test
    void testUpdateQuote() throws IOException {
        int id = quoteService.addQuote(AUTHOR_1, QUOTE_CONTENT_1);
        Quote addedQuote = quoteRepository.selectQuoteById(id);

        String newAuthor = "새로운 작가명";
        String newContent = "새로운 명언 내용";
        quoteService.updateQuote(addedQuote, newAuthor, newContent);
        Quote updatedQuote = quoteRepository.selectQuoteById(id);
        String expectedQuoteInfo = QUOTE_ID_1 + " / " + newAuthor + " / " + newContent;
        
        assertNotNull(updatedQuote, "업데이트된 명언이 존재하지 않음");
        assertEquals(expectedQuoteInfo, updatedQuote.getInfo(), "내용이 잘못 업데이트됨");
    }
    
    
    @Test
    void testListQuotes() {
        quoteService.addQuote(AUTHOR_1, QUOTE_CONTENT_1);
        quoteService.addQuote(AUTHOR_2, QUOTE_CONTENT_2);
        
        List<String> quotesList = quoteService.listQuotes();
        String expectedQuote1Info = QUOTE_ID_1 + " / " + AUTHOR_1 + " / " + QUOTE_CONTENT_1;
        String expectedQuote2Info = QUOTE_ID_2 + " / " + AUTHOR_2 + " / " + QUOTE_CONTENT_2;
        
        assertNotNull(quotesList, "명언 목록이 비어 있음");
        assertEquals(2, quotesList.size(), "명언 개수가 맞지 않음");
        assertTrue(quotesList.contains(expectedQuote1Info), "목록에 첫 번째 명언이 없음");
        assertTrue(quotesList.contains(expectedQuote2Info), "목록에 두 번째 명언이 없음");
    }
    
    @Test
    void testSaveQuotes() {
    }
    
    @Test
    void testGetQuoteById() {
        int id = quoteService.addQuote(AUTHOR_1, QUOTE_CONTENT_1);

        Quote retrievedQuote = quoteService.getQuoteById(id);
        assertNotNull(retrievedQuote, "명언을 찾을 수 없음");
        assertEquals(id, retrievedQuote.getId(), "저장된 ID 정보가 다름");

        assertThrows(QuoteNotFoundException.class, () -> quoteService.getQuoteById(999),
                     "존재하지 않는 객체 리턴");
    }
}