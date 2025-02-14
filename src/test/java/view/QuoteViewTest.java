package view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import constant.Command;

class QuoteViewTest {
    private QuoteView quoteView;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    private final InputStream originalSystemIn = System.in;
    
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor)); //출력 가로채기
    }
    
    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalSystemOut); // 출력 복구
        System.setIn(originalSystemIn); //입력 복구
    }
    
    @Test
    void testShowTitle() {
        String expectedTitle = "== 명언 앱 ==";
        
        new QuoteView().showTitle();
        
        Assertions.assertTrue(outputStreamCaptor.toString().contains(expectedTitle));
    }
    
    @Test
    void testRequestCommand() {
        String expectedCommand = "등록";
        String expectedDisplay = "명령) ";
        System.setIn(new java.io.ByteArrayInputStream(expectedCommand.getBytes()));
        
        String actualCommand = new QuoteView().requestCommand();
        
        assertTrue(outputStreamCaptor.toString().contains(expectedDisplay));
        Assertions.assertEquals(expectedCommand, actualCommand);
    }
    
    @Test
    void testRequestTargetId() {
        int expectedId = 1;
        System.setIn(new java.io.ByteArrayInputStream((expectedId + "\n").getBytes()));
        
        String actualId = new QuoteView().requestTargetId(Command.ADD);
        
        Assertions.assertEquals(String.valueOf(expectedId), actualId);
    }
    
    @Test
    void testRequestRegister() {
        String expectedContent = "새로운 명언";
        String expectedAuthor = "작가명";
        
        System.setIn(new java.io.ByteArrayInputStream((expectedContent + "\n" + expectedAuthor).getBytes()));
        
        QuoteView quoteView = new QuoteView();
        String[] registerResult = quoteView.requestRegister();
        
        Assertions.assertEquals(expectedContent, registerResult[0]);
        Assertions.assertEquals(expectedAuthor, registerResult[1]);
    }
    
    @Test
    void testRequestUpdate() {
        String previousContent = "기존 명언";
        String previousAuthor = "기존 작가";
        String updatedContent = "수정된 명언";
        String updatedAuthor = "수정된 작가";
        
        System.setIn(new java.io.ByteArrayInputStream((updatedContent + "\n" + updatedAuthor).getBytes()));
        
        String[] updateResult = new QuoteView().requestUpdate(new String[]{previousContent, previousAuthor});
        
        Assertions.assertEquals(updatedContent, updateResult[0]);
        Assertions.assertEquals(updatedAuthor, updateResult[1]);
    }
    
    @Test
    void alertSuccess() {
        String expectedMessage = "1번 명언이 등록되었습니다.\n";
        
        new QuoteView().alertSuccess(1, Command.ADD);
        
        Assertions.assertTrue(outputStreamCaptor.toString().contains(expectedMessage));
    }
    
    @Test
    void displayQuotes() {
        String expectedQuotes = """
                번호 / 작가 / 명언
                ----------------------
                2 / 작자미상 / 과거에 집착하지 마라.
                1 / 작자미상 / 현재를 사랑하라.
                """;
        List<String> quotesInfo = new ArrayList<>();
        quotesInfo.add("1 / 작자미상 / 현재를 사랑하라.");
        quotesInfo.add("2 / 작자미상 / 과거에 집착하지 마라.");
        
        new QuoteView().displayQuotes(quotesInfo);
        
        Assertions.assertEquals(outputStreamCaptor.toString(), expectedQuotes);
    }
}