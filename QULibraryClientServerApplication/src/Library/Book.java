package Library;

import java.util.ArrayList;
import java.util.List;

public class Book {
	String title;
	int ISBN;
	String description;
	List rates = new ArrayList<Integer>();
	
	public Book(String title, int i, String description, List rates2) {
		super();
		this.title = title;
		this.ISBN = i;
		this.description = description;
		this.rates = rates2;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getISBN() {
		return ISBN;
	}

	public void setISBN(int iSBN) {
		ISBN = iSBN;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List getRates() {
		return rates;
	}

	public void setRates(List rates) {
		this.rates = rates;
	}
	
}
