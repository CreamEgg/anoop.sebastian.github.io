package com.dcu.ee417.model;

import java.text.ParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	+-------------------------------------------------------------------------------------------------------+
   	| ISBN | TITLE            | AUTHOR  | COVER      | DESCRIPTION                    | CATEGORY | QUANTITY |
 	+-------------------------------------------------------------------------------------------------------+
 	| 1234 | Java for Dummies | Someone | /imagepath | A good book for Java beginners | Science  | 5        |
 	+-------------------------------------------------------------------------------------------------------+

	mysql> describe books_anoop;
	+-------------+--------------+------+-----+---------+----------------+
	| Field       | Type         | Null | Key | Default | Extra          |
	+-------------+--------------+------+-----+---------+----------------+
	| ISBN        | int(11)      | NO   | PRI | NULL    | auto_increment |
	| author      | varchar(255) | NO   |     | NULL    |                |
	| category    | varchar(255) | NO   |     | NULL    |                |
	| cover       | varchar(255) | YES  |     | NULL    |                |
	| description | varchar(255) | YES  |     | NULL    |                |
	| expiryDate  | varchar(255) | YES  |     | NULL    |                |
	| quantity    | int(11)      | NO   |     | NULL    |                |
	| rentDate    | varchar(255) | YES  |     | NULL    |                |
	| title       | varchar(255) | YES  |     | NULL    |                |
	+-------------+--------------+------+-----+---------+----------------+
 */

@Entity
@Table (name="books_sebasta2")
public class Book implements Comparable<Book>{
	private int ISBN;
	private String title;
	private String author;
	private String coverPath;
	private byte[] description;
	private String category;
	private int quantity;
	private String rentDate;
	private String expiryDate;
	
	

	public Book(String title, String author, String coverPath,
			String description, String category, int quantity) throws ParseException {
		this.title = title;
		this.author = author;
		this.coverPath = coverPath;
		this.description = description.getBytes();
		this.category = category;
		this.quantity = quantity;		
	}
	
	public Book(){
	}

	@Id
	@GeneratedValue
	public int getISBN() {
		return ISBN;
	}

	public void setISBN(int ISBN) {
		this.ISBN = ISBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "cover")
	public String getCoverPath() {
		return coverPath;
	}

	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}
	@Column(columnDefinition="BLOB") //oracle specific
	//@Column(columnDefinition="LONGBLOB")
	public byte[] getDescription() {
		return description;
	}
	
	public void setDescription(byte[] description) {
		this.description = description;
	}

	@Column(nullable = false)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(nullable = false)
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getRentDate() {
		return rentDate;
	}

	public void setRentDate(String rentDate) {
		this.rentDate = rentDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [ISBN=").append(ISBN).append(", Title=")
				.append(title).append(", Author=").append(author)
				.append(", coverPath=").append(coverPath)
				.append(", description=").append(description)
				.append(", category=").append(category).append(", quantity=")
				.append(quantity).append("]");
		return builder.toString();
	}

	
	public int compareTo(Book book) {
		return 0;
	}
}
