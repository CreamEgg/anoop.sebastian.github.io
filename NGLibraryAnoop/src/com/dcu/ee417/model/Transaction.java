package com.dcu.ee417.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 Transaction
 Involves a User and a Book
 +-------------------------------------------------------------------+
 | TRANSACTIONID | USER        | BOOK          | DATE       | TYPE   |
 +-------------------------------------------------------------------+
 | 209712        | User Object | Book Object   | 01/01/2014 | RETURN |
 | 209713        | User Object | Book Object   | 01/01/2014 | RENT   |
 +-------------------------------------------------------------------+
 *
 mysql> describe transactions_anoop;
 +-----------------+--------------+------+-----+---------+----------------+
 | Field           | Type         | Null | Key | Default | Extra          |
 +-----------------+--------------+------+-----+---------+----------------+
 | transactionID   | int(11)      | NO   | PRI | NULL    | auto_increment |
 | date            | varchar(255) | YES  |     | NULL    |                |
 | transactionType | varchar(255) | YES  |     | NULL    |                |
 | book_ISBN       | int(11)      | YES  | MUL | NULL    |                |
 | user_id         | int(11)      | YES  | MUL | NULL    |                |
 +-----------------+--------------+------+-----+---------+----------------+
 */

@Entity
@Table(name = "transactions_sebasta2")
public class Transaction {
    private int transactionID;
    private String transactionDate;
    private String userID;
    private String bookID;
    private String transactionType;

    public Transaction() {

    }

    public Transaction(String transactionDate, String userID, String bookID, String transactionType)
    {
        this.transactionDate = transactionDate;
        this.userID = userID;
        this.bookID = bookID;
        this.transactionType = transactionType;
    }

    @Column(nullable = false)
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Column(nullable = false)
    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    @Id
    @GeneratedValue
    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    @Column(nullable = false)
    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /*
     * @OneToOne(cascade = CascadeType.ALL) public User getUser() { return user;
     * } public void setUser(User user) { this.user = user; }
     * 
     * @OneToOne(cascade = CascadeType.ALL) public Book getBook() { return book;
     * } public void setBook(Book book) { this.book = book; }
     */

    @Column(nullable = false)
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}