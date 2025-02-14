package service

import exception.QuoteNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.MapQuoteRepository
import repository.QuoteRepository
import kotlin.test.*

class QuoteServiceTest {

    private val author1 = "작가명1"
    private val quoteContent1 = "명언내용1"
    private val author2 = "작가명2"
    private val quoteContent2 = "명언내용2"
    private val quoteId1 = 1
    private val quoteId2 = 2

    private lateinit var quoteService: QuoteService
    private lateinit var quoteRepository: QuoteRepository

    @BeforeEach
    fun setUp() {
        quoteRepository = MapQuoteRepository()
        quoteService = QuoteService(quoteRepository)
    }

    @Test
    fun testAddQuote() {
        val id = quoteService.addQuote(author1, quoteContent1)
        val actualQuote = quoteRepository.selectQuoteById(id)

        val expectedQuoteInfo = "$quoteId1 / $author1 / $quoteContent1"

        assertNotNull(actualQuote, "저장된 명언이 존재하지 않음")
        assertEquals(expectedQuoteInfo, actualQuote.info, "명언 객체가 다름")
    }

    @Test
    fun testDeleteQuote() {
        val id = quoteService.addQuote(author1, quoteContent1)
        val addedQuote = quoteRepository.selectQuoteById(id)
        quoteService.deleteQuote(addedQuote)

        val deletedQuote = quoteRepository.selectQuoteById(id)
        assertNull(deletedQuote, "삭제된 명언이 존재함")
    }

    @Test
    fun testUpdateQuote() {
        val id = quoteService.addQuote(author1, quoteContent1)
        val addedQuote = quoteRepository.selectQuoteById(id)

        val newAuthor = "새로운 작가명"
        val newContent = "새로운 명언 내용"
        quoteService.updateQuote(addedQuote, newAuthor, newContent)
        val updatedQuote = quoteRepository.selectQuoteById(id)
        val expectedQuoteInfo = "$quoteId1 / $newAuthor / $newContent"

        assertNotNull(updatedQuote, "업데이트된 명언이 존재하지 않음")
        assertEquals(expectedQuoteInfo, updatedQuote.info, "내용이 잘못 업데이트됨")
    }

    @Test
    fun testListQuotes() {
        quoteService.addQuote(author1, quoteContent1)
        quoteService.addQuote(author2, quoteContent2)

        val quotesList = quoteService.listQuotes()
        val expectedQuote1Info = "$quoteId1 / $author1 / $quoteContent1"
        val expectedQuote2Info = "$quoteId2 / $author2 / $quoteContent2"

        assertNotNull(quotesList, "명언 목록이 비어 있음")
        assertEquals(2, quotesList.size, "명언 개수가 맞지 않음")
        assertTrue(quotesList.contains(expectedQuote1Info), "목록에 첫 번째 명언이 없음")
        assertTrue(quotesList.contains(expectedQuote2Info), "목록에 두 번째 명언이 없음")
    }

    @Test
    fun testGetQuoteById() {
        val id = quoteService.addQuote(author1, quoteContent1)

        val retrievedQuote = quoteService.getQuoteById(id)
        assertNotNull(retrievedQuote, "명언을 찾을 수 없음")
        assertEquals(id, retrievedQuote.id, "저장된 ID 정보가 다름")

        assertFailsWith<QuoteNotFoundException> {
            quoteService.getQuoteById(999)
        }
    }
}
