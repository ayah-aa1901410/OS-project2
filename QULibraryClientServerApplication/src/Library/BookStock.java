package Library;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BookStock {
	
	public static List<JSONObject> getBooksFromStock() throws Exception{
		FileReader reader = new FileReader("BooksStock.json");
		JSONParser jsonParser = new JSONParser();
		return (List<JSONObject>) jsonParser.parse(reader);
	}

	public static Book getBook(Long isbn) throws Exception {
		List<JSONObject> books = getBooksFromStock();
		JSONObject required = null;
		Book reqBook = null;
		for(int i = 0; i<books.size(); i++) {
			if(books.get(i).get("isbn") == isbn) {
				required = books.get(i);
			}
		}
		if(required != null) {
			reqBook.setAvailable((boolean) required.get("available"));
			reqBook.setDescription((String) required.get("description"));
			reqBook.setISBN((Long) required.get("isbn"));
			reqBook.setReviews((ArrayList<Integer>) required.get("reviews"));
			reqBook.setTitle((String) required.get("title"));
		}
		return reqBook;
	}
		
	public static void setAvailability(Book book, boolean availability) throws Exception{
		List<JSONObject> books = getBooksFromStock();
		JSONObject required = null;
		for(int i = 0; i<books.size(); i++) {
			if(books.get(i).get("isbn") == book.getISBN()) {
				required = books.get(i);
			}
		}
		required.remove("available");
		required.put("available", availability);
	}
	

}
