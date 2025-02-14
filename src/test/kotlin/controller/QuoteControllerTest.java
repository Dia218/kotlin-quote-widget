package controller;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import constant.Command;
import repository.MapQuoteRepository;
import service.QuoteService;
import view.QuoteView;

class QuoteControllerTest {
    @Mock
    private QuoteView mockQuoteView;
    
    private QuoteService mockQuoteService;
    private QuoteController quoteController;
    
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockQuoteService = new QuoteService(new MapQuoteRepository());
        quoteController = new QuoteController(mockQuoteView, mockQuoteService);
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalSystemOut); // 출력 복구
    }
    
    @Test
    void testRun_Exit() {
        when(mockQuoteView.requestCommand()).thenReturn(Command.LIST.getValue(), Command.LIST.getValue(), Command.EXIT.getValue());
        
        quoteController.run();
    }
    
    @Test
    void testRun_InvalidCommand() {
        String invalidCommandError = "잘못된 명령어 입니다: 없는 명령";
        when(mockQuoteView.requestCommand()).thenReturn("없는 명령", Command.EXIT.getValue());
        
        quoteController.run();
        
        verify(mockQuoteView).displayErrorMessage(invalidCommandError);
    }
    
    @Test
    void testRun_Register() {
        String expectedContent = "새로운 명언";
        String expectedAuthor = "작가명";
        
        when(mockQuoteView.requestCommand())
                .thenReturn(Command.ADD.getValue())
                .thenReturn(Command.EXIT.getValue());
        when(mockQuoteView.requestRegister()).thenReturn(new String[]{expectedContent, expectedAuthor});
        
        quoteController.run();
    }
    
    @Test
    void testRun_Delete() {
        String errorId = "1";
        String invalidQuoteIdError = "1번 명언은 존재하지 않습니다.";
        
        when(mockQuoteView.requestCommand())
                .thenReturn(Command.DELETE.getValue())
                .thenReturn(Command.EXIT.getValue());
        when(mockQuoteView.requestTargetId(Command.DELETE)).thenReturn(errorId);
        
        quoteController.run();
        
        verify(mockQuoteView).displayErrorMessage(invalidQuoteIdError);
    }
    
    @Test
    void testRun_Update() {
        String invalidId = "일";
        String invalidNumError = "숫자만 입력해주세요. 입력된 값: 일";
        
        when(mockQuoteView.requestCommand())
                .thenReturn(Command.UPDATE.getValue())
                .thenReturn(Command.EXIT.getValue());
        when(mockQuoteView.requestTargetId(Command.UPDATE)).thenReturn(invalidId);
        
        quoteController.run();
        
        verify(mockQuoteView).displayErrorMessage(invalidNumError);
    }
    
    @Test
    void testRun_Select() {
        when(mockQuoteView.requestCommand()).thenReturn(Command.LIST.getValue(), Command.EXIT.getValue());
        
        quoteController.run();
    }
}