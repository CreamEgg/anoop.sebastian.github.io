package com.dcu.ee417.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/*
	+------------------------------------------------+
	| ID | FIRSTNAME | LASTNAME | EMAIL   | PASSWORD |
	+------------------------------------------------+
	| 01 | Anoop     | Sebastian| a@a.com | hahah    |
	+------------------------------------------------+

	mysql> describe users_anoop;
	+-----------+--------------+------+-----+---------+----------------+
	| Field     | Type         | Null | Key | Default | Extra          |
	+-----------+--------------+------+-----+---------+----------------+
	| id        | int(11)      | NO   | PRI | NULL    | auto_increment |
	| email     | varchar(255) | YES  |     | NULL    |                |
	| firstName | varchar(255) | YES  |     | NULL    |                |
	| lastName  | varchar(255) | YES  |     | NULL    |                |
	| password  | varchar(255) | YES  |     | NULL    |                |
	+-----------+--------------+------+-----+---------+----------------+
*/

@Entity
@Table (name="users_sebasta2")
public class User {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Set<Book> books;

	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		books = new TreeSet<Book>();
	}
	
	public User(){	
	}
	
	@Id	
	@GeneratedValue
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/*
	mysql> describe rentedbooks_sebasta2;
	+--------+---------+------+-----+---------+-------+
	| Field  | Type    | Null | Key | Default | Extra |
	+--------+---------+------+-----+---------+-------+
	| userid | int(11) | NO   | PRI | NULL    |       |
	| isbn   | int(11) | NO   | PRI | NULL    |       |
	+--------+---------+------+-----+---------+-------+*/
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "rentedbooks_sebasta2", joinColumns = { 
			@JoinColumn(name = "userid", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "isbn", 
					nullable = false, updatable = false) })
	public Set<Book> getBooks() {
		return books;
	}
	
	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	/*public void rentBook(Book book) {
		this.books.add(book);
	}
	
	public void returnBook(Book book) {
		this.books.remove(book);
	}*/

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", email=");
		builder.append(email);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}
}