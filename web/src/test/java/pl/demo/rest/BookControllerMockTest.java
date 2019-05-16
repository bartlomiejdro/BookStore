package pl.demo.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import pl.demo.enumerations.BookStatus;
import pl.demo.service.BookService;
import pl.demo.service.impl.BookServiceImpl;
import pl.demo.to.BookTo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookControllerMockTest {

    private MockMvc mockMvc;

    private static ThymeleafViewResolver viewResolver;

    private BookService bookServiceMock;

    @BeforeClass
    public static void initViewResolver() {
        viewResolver = new ThymeleafViewResolver();
    }

    @Before
    public void setup() {
        // given
        bookServiceMock = Mockito.mock(BookServiceImpl.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new BookRestController(bookServiceMock))
                                 .setViewResolvers(viewResolver)
                                 .build();
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        // given
        List<BookTo> books = new ArrayList<>();

        BookTo book1 = new BookTo();
        book1.setId(1L);
        book1.setTitle("Title 1");
        book1.setStatus(BookStatus.FREE);
        book1.setDescription("Dexfription for book");

        books.add(book1);

        // when
        when(bookServiceMock.findAllBooks()).thenReturn(books);
        ResultActions resultActions = mockMvc.perform(get("/books"));

        // then
        Mockito.verify(bookServiceMock, times(1)).findAllBooks();
        resultActions.andDo(print())//
                     .andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                     .andExpect(jsonPath("$[0].title", is(book1.getTitle())))
                     .andExpect(jsonPath("$[0].status", is(book1.getStatus().name())))
                     .andExpect(jsonPath("$[0].description", is(book1.getDescription())));
    }

}
