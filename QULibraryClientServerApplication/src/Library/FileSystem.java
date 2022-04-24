package Library;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class FileSystem {
	
	
	public static void downloadBook(Book book) throws Exception {
	    JSONObject sampleObject = new JSONObject();
	    
	    String filename = book.getTitle()+".json";
	    
	    sampleObject.put("title",book.getTitle());
	    sampleObject.put("ISBN", book.getISBN());
	    sampleObject.put("description", book.getDescription());
	    sampleObject.put("available", book.isAvailable());

	    JSONArray bookRates = new JSONArray();
	    for(int i = 0; i< book.getReviews().size(); i++) {
	    	bookRates.add(book.getReviews().get(i));
	    }
	    
	    sampleObject.put("rates", bookRates);
//	    Files.write(Paths.get(filename), sampleObject.toJSONString().getBytes());
	    FileWriter file = new FileWriter("BooksStorage\\"+filename);
	    file.write(sampleObject.toJSONString());
	    file.close();
	}
	
	public static Object uploadBook(String filename) throws Exception {
	    FileReader reader = new FileReader("BooksStorage\\"+filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
	}
	
//	public static void main(String[] args) throws Exception {
//		Book book1 = new Book("Book1", 1122334455661L,"This is the first book in the dataset", new ArrayList<Integer>());
//
//        downloadBook(book1);
//        JSONObject jsonObject = (JSONObject) uploadBook("book1.json");
//        System.out.println(jsonObject);
//        System.out.println(jsonObject.get("title"));
//    }
	
}
