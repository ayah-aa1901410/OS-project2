package Library;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class FileSystem {
	
	public static void downloadBook(String filename) throws Exception {
	    JSONObject sampleObject = new JSONObject();
	    List rates = new ArrayList<Integer>();
	    rates.add(1);
	    rates.add(2);
	    Book book1 = new Book("TestBook", 1234, "This book is a test for the function", rates);
	    
	    sampleObject.put("title",book1.getTitle());
	    sampleObject.put("ISBN", book1.getISBN());
	    sampleObject.put("description", book1.getDescription());

	    JSONArray bookRates = new JSONArray();
	    bookRates.add(book1.getRates().get(0));
	    bookRates.add(book1.getRates().get(1));
	    
	    
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
	
	public static void main(String[] args) throws Exception {
        downloadBook("book1.json");
        JSONObject jsonObject = (JSONObject) uploadBook("book1.json");
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("title"));
    }
	
}
