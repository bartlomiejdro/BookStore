package pl.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.demo.constants.ModelConstants;
import pl.demo.service.impl.BookServiceImpl;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private BookServiceImpl bookService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService)).setViewResolvers(viewResolver())
				.build();
	}

	private ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classPath:/template");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

	@Test
	public void shouldFindAll() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/books"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.findAllBooks()));
	}

	@Test
	public void shouldRemoveBookWithId1() throws Exception {
		ResultActions resultActions = mockMvc.perform(post("/books/remove?id=1"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.findAllBooks()));
	}

	@Test
	public void shouldReturnListOfBooksCorrespondingToSearchedValues() throws Exception {

		ResultActions resultActions = mockMvc.perform(get("/books/filter/?title=ja&author=&category="));

		resultActions.andExpect(model().attribute(ModelConstants.LISTEDBOOKS, bookService.filterBooks("ja", "", "")))
				.andExpect(view().name("books")).andExpect(status().isOk());
	}

	@Test
	public void shouldFindBookByTitle() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/books/filter").param("title", "Java"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks("Java", null, null)));
	}

	@Test
	public void shoulFindBookByAuthor() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/books/filter").param("author", "Jacek"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks(null, "Jacek", null)));
	}

	@Test
	public void shoulFindBookByAuthorAndByTitle() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get("/books/filter").param("author", "Jacek").param("title", "Ani"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks("Ani", "Jacek", null)));
	}

	@Test
	public void shoulFindBookByAuthorByTitleAndByStatus() throws Exception {
		ResultActions resultActions = mockMvc.perform(
				get("/books/filter").param("title", "Ani").param("author", "Jacek").param("status", "missing"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks("Ani", "Jacek", "missing")));
	}

	@Test
	public void shoulFindBookByAuthorAndByStatus() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get("/books/filter").param("author", "Jacek").param("status", "missing"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks(null, "Jacek", "missing")));
	}

	@Test
	public void shoulFindBookByTitleAndByStatus() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get("/books/filter").param("title", "Ani").param("status", "missing"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks(null, "Jacek", "missing")));
	}

	@Test
	public void shouldFindBookByStatus() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/books/filter").param("status", "loan"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("books")).andDo(print())
				.andExpect(model().attribute("bookList", bookService.filterBooks(null, null, "loan")));
	}

	@Test
	public void shouldReturnBookWithId1() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=1"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("book")).andDo(print())
				.andExpect(model().attribute("book", bookService.findById(1L)));
	}

	@Test
	public void shouldReturnAddBookView() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/books/add"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("addBook")).andDo(print());
	}

	@Test
	public void shouldReturn403page() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/403"));

		resultActions.andExpect(status().isOk()).andExpect(view().name("403")).andDo(print());
	}

	@Test
	public void shouldReturnErrerMessageNoTitleWhenTitleIsNotEntered() throws Exception {
		ResultActions resultActions = mockMvc.perform(post("/books/add").param("title", "")
				.param("authors", "Jakub Cwiek").param("description", "Ksiazka o Nordyckim bogu Lokim")
				.param("status", "LOAN").param("categories", "HORROR"));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No title. Please enter title"));
	}

	@Test
	public void shouldReturnErrerMessageNoAuthorWhenOnlySpaceIsEnteredToTitle() throws Exception {
		ResultActions resultActions = mockMvc.perform(post("/books/add").param("title", "Klamca 2").param("authors", "")
				.param("description", "Ksiazka o Nordyckim bogu Lokim").param("status", "LOAN")
				.param("categories", "HORROR"));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No author. Please enter author"));
	}

	@Test
	public void shouldReturnErrerMessageNoAuthoIsEntered() throws Exception {
		ResultActions resultActions = mockMvc.perform(
				post("/books/add").param("title", "Klamca 2").param("description", "Ksiazka o Nordyckim bogu Lokim")
						.param("status", "LOAN").param("categories", "HORROR"));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No author. Please enter author"));
	}

	@Test
	public void shouldReturnErrorMessageNoDescreptionWhenDescreptionIsNotEntered() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(post("/books/add").param("title", "Klamca 2").param("authors", "Jakub Cwiek")
						.param("description", "").param("status", "LOAN").param("categories", "HORROR"));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No descryption. Please enter descryption"));
	}

	@Test
	public void shouldReturnErrorMessageNoStatusWhenStatusIsNotEntered() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(post("/books/add").param("title", "Klamca 2").param("authors", "Jakub Cwiek")
						.param("description", "Ksiazka o Nordyckim bogu Lokim").param("categories", "HORROR"));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No status. Please enter status"));
	}

	@Test
	public void shouldReturnErrorMessageNoCategoryWhenCategoryIsNotEntered() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(post("/books/add").param("title", "Klamca 2").param("authors", "Jakub Cwiek")
						.param("description", "Ksiazka o Nordyckim bogu Lokim").param("status", "LOAN"));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No category. Please enter category"));
	}

	@Test
	public void shouldReturnErrorMessageNoCategoryWhenCategoryIsEmpty() throws Exception {
		ResultActions resultActions = mockMvc.perform(post("/books/add").param("title", "Klamca 2")
				.param("authors", "Jakub Cwiek").param("description", "Ksiazka o Nordyckim bogu Lokim")
				.param("status", "LOAN").param("categories", ""));

		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "No category. Please enter category"));
	}

	@Test
	public void shouldReturnMessageBookAddedWhenAllDataIsCorrect() throws Exception {
		ResultActions resultActions = mockMvc.perform(post("/books/add").param("title", "Klamca 2")
				.param("authors", "Jakub Cwiek").param("description", "Ksiazka o Nordyckim bogu Lokim")
				.param("status", "LOAN").param("categories", "HORROR"));

		resultActions.andExpect(view().name("books"))
				.andExpect(model().attribute(ModelConstants.ERROR_MESSAGE, "Book has been added"));
	}
}
