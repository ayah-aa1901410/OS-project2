package Library;

import java.util.ArrayList;
import java.util.List;

public class Book {
	String title;
	Long ISBN;
	String description;
	List rates = new ArrayList<Integer>();
	
	public Book(String title, Long iSBN, String description) {
		super();
		this.title = title;
		ISBN = iSBN;
		this.description = description;
	}
	
}
