package Library;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BookStock {

	public static List<JSONObject> getBooksFromStock() throws Exception {
		FileReader reader = new FileReader("BooksStock.json");
		JSONParser jsonParser = new JSONParser();
		return (List<JSONObject>) jsonParser.parse(reader);
	}

	public static Book getBook(Long isbn) throws Exception {
		List<JSONObject> books = getBooksFromStock();
		JSONObject required = null;
		Book reqBook = null;
		for (int i = 0; i < books.size(); i++) {
			if (Long.compare((long) books.get(i).get("ISBN"), isbn) == 0) {
				required = books.get(i);
			}
		}
		if (required != null) {
			reqBook = new Book((String) required.get("title"), (Long) required.get("ISBN"),
					(String) required.get("description"), (ArrayList<Integer>) required.get("reviews"),
					(boolean) required.get("available"));
		}
		System.out.println(reqBook);
		return reqBook;
	}

	public static void setAvailability(Book book, boolean availability) throws Exception {
		List<JSONObject> books = getBooksFromStock();
		JSONObject required = null;
		for (int i = 0; i < books.size(); i++) {
			if (books.get(i).get("ISBN") == book.getISBN()) {
				required = books.get(i);
			}
		}
		required.remove("available");
		required.put("available", availability);
	}

	public static void main(String args[]) {
		try {
			getBook(1122334455661L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
