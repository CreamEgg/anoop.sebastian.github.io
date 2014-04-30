package com.dcu.ee417.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	+----------------------------------------------------------------+
	| ID | FIRSTNAME | LASTNAME | EMAIL   | PASSWORD | LIBRARIAN_ID  |
	+----------------------------------------------------------------+
	| 01 | Joe       | Jones    | a@a.com | hahah    | 12345678      |
	+----------------------------------------------------------------+
 
	mysql> describe librarians_anoop;
	+-------------+--------------+------+-----+---------+----------------+
	| Field       | Type         | Null | Key | Default | Extra          |
	+-------------+--------------+------+-----+---------+----------------+
	| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
	| email       | varchar(255) | YES  |     | NULL    |                |
	| firstName   | varchar(255) | YES  |     | NULL    |                |
	| lastName    | varchar(255) | YES  |     | NULL    |                |
	| librarianID | int(11)      | NO   |     | NULL    |                |
	| password    | varchar(255) | YES  |     | NULL    |                |
	+-------------+--------------+------+-----+---------+----------------+
*/

@Entity
@Table (name="librarians_sebasta2")
public class Librarian {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private int librarianID;

	public Librarian(String firstName, String lastName, String email, String password, int librarianID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.librarianID = librarianID;
	}
	
	public Librarian(){		
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

	public int getLibrarianID() {
		return librarianID;
	}

	public void setLibrarianID(int librarianID) {
		this.librarianID = librarianID;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Librarian [id=").append(id).append(", firstName=")
				.append(firstName).append(", lastName=").append(lastName)
				.append(", email=").append(email).append(", password=")
				.append(password).append(", librarianID=").append(librarianID)
				.append("]");
		return builder.toString();
	}
}