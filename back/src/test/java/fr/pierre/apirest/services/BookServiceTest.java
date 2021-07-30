package fr.pierre.apirest.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.pierre.apirest.entities.Book;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class BookServiceTest {

	@Autowired
	BookService bookService;
	
	@Test
	public void saveUpdateDeleteProcessBook() {
		Book book = new Book("titletest", "authortest", "publishertest");
		book.setRelease_date(new Date());
	
		Book bookReturn = bookService.save(book);

		String parseDate = new SimpleDateFormat("yyyy-MM-dd").format(bookReturn.getRelease_date());
		
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), parseDate);
		Assert.assertEquals("titletest", bookReturn.getTitle());
		Assert.assertEquals("authortest", bookReturn.getAuthor());
		Assert.assertEquals("publishertest", bookReturn.getPublisher());
		
		bookReturn.setTitle("titletest2");
		bookReturn.setAuthor("authortest2");
		bookReturn.setPublisher("publishertest2");
		
		Book bookUpdate = bookService.update(bookReturn);
		
		Assert.assertEquals("titletest2", bookUpdate.getTitle());
		Assert.assertEquals("authortest2", bookUpdate.getAuthor());
		Assert.assertEquals("publishertest2", bookUpdate.getPublisher());
			
		bookService.delete(bookUpdate.getIbn());
	}
}
