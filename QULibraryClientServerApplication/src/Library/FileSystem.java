package Library;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class FileSystem {
	
	
	public static void downloadBook(String filename, Book book) throws Exception {
	    JSONObject sampleObject = new JSONObject();
	    
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
	    FileWriter file = new FileWriter(filename);
	    file.write(sampleObject.toJSONString());
	    file.close();
	}
	
	public static Object uploadBook(String filename) throws Exception {
	    FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
	}
	
//	public static void main(String[] args) throws Exception {
//        downloadBook("book1.json");
//        JSONObject jsonObject = (JSONObject) uploadBook("book1.json");
//        System.out.println(jsonObject);
//        System.out.println(jsonObject.get("title"));
//    }
	
}
