package repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Quote;

class QuoteRepositoryTest {
    private static final String AUTHOR_3 = "작가명1";
    private static final String AUTHOR_4 = "작가명2";
    private static final String QUOTE_CONTENT_3 = "명언내용1";
    private static final String QUOTE_CONTENT_4 = "명언내용2";
    private static final int QUOTE_ID_3 = 3;
    private static final int QUOTE_ID_4 = 4;
    
    private static final String DB_DIRECTORY = "./db/quotes/";
    
    QuoteRepository quoteRepository;
    
    @BeforeEach
    public void setUp() {
        quoteRepository = new JsonQuoteRepository();
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        cleanUp();
    }
    
    private void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(DB_DIRECTORY + QUOTE_ID_3 + ".json"));
        Files.deleteIfExists(Paths.get(DB_DIRECTORY + QUOTE_ID_4 + ".json"));
        
        Path path = Paths.get(DB_DIRECTORY + "lastId.txt");
        Files.deleteIfExists(path);
        
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.writeString(path, "2");
        }
        
        path = Paths.get(DB_DIRECTORY + "data.json");
        Files.deleteIfExists(path);
        
        if (!Files.exists(path)) {
            quoteRepository.buildFile();
        }
    }
    
    @Test
    void testInsertQuote() throws IOException {
        Quote expectedQuote1 = new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3);
        Quote expectedQuote2 = new Quote(QUOTE_ID_4, AUTHOR_4, QUOTE_CONTENT_4);
        
        quoteRepository.insertQuote(expectedQuote1);
        quoteRepository.insertQuote(expectedQuote2);
        Quote actualQuote1 = quoteRepository.selectQuoteById(QUOTE_ID_3);
        Quote actualQuote2 = quoteRepository.selectQuoteById(QUOTE_ID_4);
        
        assertNotNull(actualQuote1, "3번 저장 안됨");
        assertEquals(expectedQuote1.getInfo(), actualQuote1.getInfo(), "3번 명언 객체가 다름");
        assertNotNull(actualQuote2, "4번 저장 안됨");
        assertEquals(expectedQuote2.getInfo(), actualQuote2.getInfo(), "4번 명언 객체가 다름");
    }
    
    @Test
    void testDeleteQuote() throws IOException {
        Quote quote = new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3);
        quoteRepository.insertQuote(quote);
        quoteRepository.deleteQuote(QUOTE_ID_3);
        
        Quote deletedQuote = quoteRepository.selectQuoteById(QUOTE_ID_3);
        
        assertNull(deletedQuote, "삭제되지 않음");
    }
    
    @Test
    void testUpdateQuote() throws IOException {
        Quote quote = new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3);
        quoteRepository.insertQuote(quote);
        
        String newAuthor = "작가명 변경";
        String newContent = "명언내용 변경";
        quoteRepository.updateQuote(quote, newAuthor, newContent);
        Quote updatedQuote = quoteRepository.selectQuoteById(QUOTE_ID_3);
        String expectedQuoteInfo = QUOTE_ID_3 + " / " + newAuthor + " / " + newContent;
        
        assertNotNull(updatedQuote, "객체를 찾을 수 없음");
        assertEquals(expectedQuoteInfo, updatedQuote.getInfo(), "업데이트 정보가 잘못됨");
    }
    
    @Test
    void testSelectAllQuotes() throws IOException {
        Quote quote3 = new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3);
        Quote quote4 = new Quote(QUOTE_ID_4, AUTHOR_4, QUOTE_CONTENT_4);
        quoteRepository.insertQuote(quote3);
        quoteRepository.insertQuote(quote4);
        
        List<Quote> allQuotes = quoteRepository.selectAllQuotes();
        
        assertNotNull(allQuotes, "목록이 비어있음");
        assertTrue(allQuotes.contains(quote3), "3번 명언이 목록에 포함되지 않음");
        assertTrue(allQuotes.contains(quote4), "4번 명언이 목록에 포함되지 않음");
    }
    
    @Test
    void testSelectQuoteById() throws IOException {
        quoteRepository.insertQuote(new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3));
        
        Quote quote = quoteRepository.selectQuoteById(QUOTE_ID_3);
        
        assertNotNull(quote, "찾지 못함");
        assertEquals(QUOTE_ID_3, quote.getId(), "Id가 일치하지 않음");
        Quote nonExistentQuote = quoteRepository.selectQuoteById(999);
        assertNull(nonExistentQuote, "존재하지 않은 객체 리턴");
    }
    
    @Test
    void testBuildFile() throws IOException {
        Path jsonFilePath = Paths.get(DB_DIRECTORY + "data.json");
        
        Quote quote3 = new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3);
        Quote quote4 = new Quote(QUOTE_ID_4, AUTHOR_4, QUOTE_CONTENT_4);
        quoteRepository.insertQuote(quote3);
        quoteRepository.insertQuote(quote4);
        quoteRepository.buildFile();
        
        assertTrue(Files.exists(jsonFilePath), "data.json 파일이 생성되지 않았습니다.");
        
        String jsonContent = new String(Files.readAllBytes(jsonFilePath));
        assertTrue(jsonContent.contains(QUOTE_CONTENT_3), "명언 내용이 포함되지 않았습니다.");
        assertTrue(jsonContent.contains(QUOTE_CONTENT_4), "명언 내용이 포함되지 않았습니다.");
        assertTrue(jsonContent.startsWith("["), "파일의 시작이 '['가 아닙니다.");
        assertTrue(jsonContent.endsWith("]"), "파일의 끝이 ']'가 아닙니다.");
    }
    
    @Test
    void testGetLastId() throws IOException {
        Quote expectedQuote1 = new Quote(QUOTE_ID_3, AUTHOR_3, QUOTE_CONTENT_3);
        Quote expectedQuote2 = new Quote(QUOTE_ID_4, AUTHOR_4, QUOTE_CONTENT_4);
        
        quoteRepository.insertQuote(expectedQuote1);
        quoteRepository.insertQuote(expectedQuote2);
        
        assertEquals(4, quoteRepository.getLastId());
    }
}