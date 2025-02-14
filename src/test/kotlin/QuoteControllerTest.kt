import constant.Command
import controller.QuoteController
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import repository.MapQuoteRepository
import service.QuoteService
import view.QuoteView
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class QuoteControllerTest {
    @Mock
    private lateinit var mockQuoteView: QuoteView

    private lateinit var quoteController: QuoteController
    private val outputStreamCaptor = ByteArrayOutputStream()
    private val originalSystemOut = System.out

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val mockQuoteService =
            QuoteService(MapQuoteRepository())
        quoteController = QuoteController(mockQuoteView, mockQuoteService)
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @AfterEach
    fun restoreSystemOutStream() {
        System.setOut(originalSystemOut)
    }

    @Test
    fun testRun_Exit() {
        `when`(mockQuoteView.requestCommand()).thenReturn(Command.LIST.value, Command.LIST.value, Command.EXIT.value)
        quoteController.run()
    }

    @Test
    fun testRun_InvalidCommand() {
        val invalidCommandError = "잘못된 명령어 입니다: 없는 명령"
        `when`(mockQuoteView.requestCommand()).thenReturn("없는 명령", Command.EXIT.value)

        quoteController.run()

        verify(mockQuoteView).displayErrorMessage(invalidCommandError)
    }

    @Test
    fun testRun_Register() {
        val expectedContent = "새로운 명언"
        val expectedAuthor = "작가명"

        `when`(mockQuoteView.requestCommand()).thenReturn(Command.ADD.value, Command.EXIT.value)
        `when`(mockQuoteView.requestRegister()).thenReturn(arrayOf(expectedContent, expectedAuthor))

        quoteController.run()
    }

    @Test
    fun testRun_Delete() {
        val errorId = "1"
        val invalidQuoteIdError = "1번 명언은 존재하지 않습니다."

        `when`(mockQuoteView.requestCommand()).thenReturn(Command.DELETE.value, Command.EXIT.value)
        `when`(mockQuoteView.requestTargetId(Command.DELETE)).thenReturn(errorId)

        quoteController.run()

        verify(mockQuoteView).displayErrorMessage(invalidQuoteIdError)
    }

    @Test
    fun testRun_Update() {
        val invalidId = "일"
        val invalidNumError = "숫자만 입력해주세요. 입력된 값: 일"

        `when`(mockQuoteView.requestCommand()).thenReturn(Command.UPDATE.value, Command.EXIT.value)
        `when`(mockQuoteView.requestTargetId(Command.UPDATE)).thenReturn(invalidId)

        quoteController.run()

        verify(mockQuoteView).displayErrorMessage(invalidNumError)
    }

    @Test
    fun testRun_Select() {
        `when`(mockQuoteView.requestCommand()).thenReturn(Command.LIST.value, Command.EXIT.value)

        quoteController.run()
    }
}
