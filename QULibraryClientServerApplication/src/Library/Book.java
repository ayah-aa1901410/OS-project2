package Library;

import java.util.ArrayList;
import java.util.List;

public class Book {
	String title;
	Long ISBN;
	String description;
	List reviews = new ArrayList<Integer>();
	boolean available = true;
	
	public Book(String title, Long isbn, String description, ArrayList<Integer> reviews) {
		super();
		this.title = title;
		this.ISBN = isbn;
		this.description = description;
		this.reviews = reviews;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getISBN() {
		return ISBN;
	}

	public void setISBN(Long iSBN) {
		this.ISBN = iSBN;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List getReviews() {
		return reviews;
	}

	public void setReviews(List reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(int review) {
		this.reviews.add(review);
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	
}
