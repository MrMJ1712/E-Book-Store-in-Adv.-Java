package com.bookStore.controller;

//import java.awt.PageAttributes.MediaType;
import org.springframework.http.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bookStore.dto.BookDetails;
import com.bookStore.entity.Book;
import com.bookStore.service.BookService;

import jakarta.websocket.server.PathParam;

@RestController
@CrossOrigin
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("/book")
	public String insertBook(@RequestBody Book book)
	{
		bookService.addBook(book);
		return "Book Added Sucessfuly...";
	}
	
	@GetMapping("/book")
	public List<Book> getAllBook(){
		
		return bookService.findAllBook();
	}
	
	@GetMapping("/book/{bookId}")
	public Book getBook(@PathVariable int bookId){
		
		return bookService.findBookbyId(bookId);
	}
	
	@PutMapping("/book/{bookId}")
	public String updateBook(@PathVariable int bookId, @RequestBody Book book)
	{
		bookService.updateBookbyId(bookId,book);
		return "Book Updated Successfully...";
	}
	
	@DeleteMapping("/book/{bookId}")
	public String deleteBook(@PathVariable int bookId) {
		bookService.deleteBookbyId(bookId);
		return "Book Deleted sucessfuly";
	}
	@PostMapping("/book/v3")
	public String registerv3(BookDetails bookDetails) {
		try {
			Book book = new Book();
			BeanUtils.copyProperties(bookDetails, book);
			
			//storing the uploaded file
			try {
				String fileName = bookDetails.getCoverImg().getOriginalFilename();
				//TODO:here should be the code to generate a unique name for the file before proceeding further
				String generatedFileName = fileName; //replace this later
				
				book.setCoverImg(generatedFileName);
				
				InputStream is = bookDetails.getCoverImg().getInputStream();
				FileOutputStream os = new FileOutputStream("E:\\Project Imgs\\img" + generatedFileName);
				FileCopyUtils.copy(is, os);
			}
			catch (IOException e) {
				//hoping no error here hence keeping it blank
			}
			
			 bookService.addBook(book);
			 return "Data inserted sucessfuly..";
			
			
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Invalid data not able to store it";
	}
	@GetMapping(path = "/book/fetch/coverImg/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfilePic(@PathVariable int id) throws IOException {
		Book book = getBook(id);
	    return Files.readAllBytes(Paths.get("E:\\Project Imgs\\img" + book.getCoverImg()));
	}
	
}
