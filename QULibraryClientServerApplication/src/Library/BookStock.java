package Library;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BookStock {

	public static List<JSONObject> getBooksFromStock() throws Exception {
		FileReader reader = new FileReader("BooksStock.json");
		JSONParser jsonParser = new JSONParser();
		return (List<JSONObject>) jsonParser.parse(reader);
	}

	public static JSONObject getBook(Long isbn) throws Exception {
		List<JSONObject> books = getBooksFromStock();
		JSONObject required = null;
		Book reqBook = null;
		for (int i = 0; i < books.size(); i++) {
			if (Long.compare((long) books.get(i).get("ISBN"), isbn) == 0) {
				required = books.get(i);
			}
		}
		return required;
	}

	public static void setAvailability(JSONObject requiredBook, boolean availability) throws Exception {
		List<JSONObject> books = getBooksFromStock();
		JSONObject required = null;
		for (int i = 0; i < books.size(); i++) {
			if (Long.compare((long) books.get(i).get("ISBN"), (long) requiredBook.get("ISBN")) == 0) {
				books.get(i).put("available", availability);
			}
		}
	    FileWriter file = new FileWriter("BooksStock.json");
	    file.write(books.toString());
	    file.close();
	}

//	public static void main(String args[]) {
//		try {
//			getBook(1122334455661L);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
