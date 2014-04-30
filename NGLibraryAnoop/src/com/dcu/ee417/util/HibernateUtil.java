package com.dcu.ee417.util;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.dcu.ee417.model.Book;
import com.dcu.ee417.model.Librarian;
import com.dcu.ee417.model.Transaction;
import com.dcu.ee417.model.User;

@SuppressWarnings("unchecked")
public class HibernateUtil {

	private static SessionFactory factory;

	public static Configuration getInitializedConfiguration() {
		Configuration config = new Configuration();
		config.addPackage("com.dcu.ee417.model");
		config.addAnnotatedClass(User.class);
		config.addAnnotatedClass(Book.class);
		config.addAnnotatedClass(Librarian.class);
		config.addAnnotatedClass(Transaction.class);
		config.configure();
		return config;
	}

	public static Session getSession() {
		if (factory == null) {
			Configuration config = HibernateUtil.getInitializedConfiguration();
			factory = config.buildSessionFactory();
		}
		Session hibernateSession = factory.getCurrentSession();
		return hibernateSession;
	}

	public static void closeSession() {
		HibernateUtil.getSession().close();
	}

	public static void recreateDatabase() {
		Configuration config;
		config = HibernateUtil.getInitializedConfiguration();
		new SchemaExport(config).create(true, true);
	}

	public static Session beginTransaction() {
		Session hibernateSession;
		hibernateSession = HibernateUtil.getSession();
		hibernateSession.beginTransaction();
		return hibernateSession;
	}

	public static void commitTransaction() {
		HibernateUtil.getSession().getTransaction().commit();
	}

	public static void rollbackTransaction() {
		HibernateUtil.getSession().getTransaction().rollback();
	}
	
	public static boolean rentBook(Transaction transaction) {
		
		if(!transaction.getTransactionType().equalsIgnoreCase("rent")) return false;
		
		try {
			Session session = HibernateUtil.beginTransaction();
			// get the user by id
			String userQuery = "from User where id = :id"; 
			Query query = session.createQuery(userQuery); 
			query.setInteger("id", Integer.parseInt(transaction.getUserID()));		
			User user = (User)query.uniqueResult();
			
			// get the book by isbn
			userQuery = "from Book where ISBN = :ISBN";
			query = session.createQuery(userQuery); 
			query.setInteger("ISBN", Integer.parseInt(transaction.getBookID()));		
			Book book = (Book)query.uniqueResult();
			
			book.setRentDate(transaction.getTransactionDate());
			book.setExpiryDate(Utils.addDays(transaction.getTransactionDate(), 5));
			//System.out.println(user);
			//System.out.println(book);
			
			//get the user, update the Set<Book>, get the book, reduce the quantity
			Set<Book> books = user.getBooks();
			if(books.contains(book)) {
				System.out.println("Cannot rent the same book twice");
				return false;
			}
			books.add(book);
			user.setBooks(books);
			
			int quantity = book.getQuantity();
			if(quantity > 0) {
				quantity--;
				book.setQuantity(quantity);
			}
			session.save(user);
			session.save(book);
			session.save(transaction);
			HibernateUtil.commitTransaction();
			return true;
		} catch (Exception e) {
			System.out.println("Error while renting book: "+ e.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		return false;
	}
	
	public static boolean returnBook(Transaction transaction) {
		
		//if (!transaction.isValid()) return false;
		if(!transaction.getTransactionType().equalsIgnoreCase("return")) return false;
		
		Integer userID = Utils.parseIntFromString(transaction.getUserID());
		Integer bookID = Utils.parseIntFromString(transaction.getBookID());
		
		if(userID==null || bookID ==null) return false;
		
		try {
			Session session = HibernateUtil.beginTransaction();
			// get the user by id
			String userQuery = "from User where id = :id"; 
			Query query = session.createQuery(userQuery); 
			query.setInteger("id", userID);		
			User user = (User)query.uniqueResult();
			
			// get the book by isbn
			userQuery = "from Book where ISBN = :ISBN";
			query = session.createQuery(userQuery); 
			query.setInteger("ISBN", bookID);		
			Book book = (Book)query.uniqueResult();
			
			//get the user, update the Set<Book>, get the book, reduce the quantity
			Set<Book> books = user.getBooks();
			if(!books.contains(book)) {
				System.out.println("Cannot return book without renting it");
				return false;
			}
			books.remove(book);
			user.setBooks(books);
			
			int quantity = book.getQuantity();
			if(quantity < 10) {
				quantity++;
				book.setQuantity(quantity);
			}
			session.save(user);
			session.save(book);
			session.save(transaction);
			HibernateUtil.commitTransaction();
			return true;
		} catch (Exception e) {
			System.out.println("Error while renting book: "+ e.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		return false;
	}
	
	public static String updateDateForUser(int userid, int isbn, int daysToIncrement) throws ParseException {
		try {
			Session session = HibernateUtil.beginTransaction();			
			String userQuery = "from User where id = :id";
			Query query = session.createQuery(userQuery); 
			query.setInteger("id", userid);	
			User user = (User)query.uniqueResult();
			Set<Book> books = getUsersBooks(userid);
			
			for (Book b: books) {
				if(b.getISBN()==isbn) {
					b.setExpiryDate(Utils.addDays(b.getExpiryDate(), daysToIncrement));
					break;
				}
			}
			
			user.setBooks(books);
			session.save(user);
			return "success";
		} catch (Exception e) {
			System.out.println("Error while updating exppiry date");
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	public static User validateUser(final String email, final String password) {		
		if(!Utils.validate(new String[]{email,password})) return null;
		
		try {
			Session session = HibernateUtil.beginTransaction();			
			String userQuery = "from User where email = :email"; 
			Query query = session.createQuery(userQuery); 
			query.setString("email", email);		
			User user = (User)query.uniqueResult();
			
			if(user!=null) {
				if(user.getPassword().equals(password)) {
					HibernateUtil.commitTransaction();
					return user;
				}
			}				
		}
		catch(NonUniqueResultException e) {
			System.out.println("Non unique user");
			e.printStackTrace();
			HibernateUtil.commitTransaction();
		}		
		return null;
	}
	
	public static Librarian validateAdmin(String email, String password) {
		if(!Utils.validate(new String[]{email,password})) return null;
		
		try {
			Session session = HibernateUtil.beginTransaction();			
			String userQuery = "from Librarian where email = :email"; 
			Query query = session.createQuery(userQuery); 
			query.setString("email", email);		
			Librarian admin = (Librarian)query.uniqueResult();
			
			if(admin!=null) {
				if(admin.getPassword().equals(password)) {
					HibernateUtil.commitTransaction();
					return admin;
				}
			}				
		}
		catch(NonUniqueResultException e) {
			System.out.println("Non unique user");
			e.printStackTrace();
			HibernateUtil.commitTransaction();
			return null;
		}		
		return null;
	}
	
	public static String addUser(Object object) {
		if(object.getClass().equals(User.class)) {	
			User user = (User)object;
			if(!Utils.validateUser(user)) return "nonvalidemail";
			if(adduser(user)) return "success";
			return "nonvalidemail";
		}
		if(object.getClass().equals(Librarian.class)) {
			Librarian admin = (Librarian)object;
			if(!Utils.validateLibrarian(admin)) return "nonvalidemail";
			if(addAdmin(admin)) return "success";
			return "nonvalidemail";
		}
		return "nonvalidemail";		
	}

	private static boolean addAdmin(Librarian admin) {
		Session session = HibernateUtil.beginTransaction();
		String email = admin.getEmail();
		int adminID = admin.getId();
		
		Query queryResult = session.createQuery("from Librarian");

		
		List<Librarian> users = (List<Librarian>) queryResult.list();

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getEmail().equals(email) || users.get(i).getId() == adminID) {				
				HibernateUtil.rollbackTransaction();
				return false;
			}
		}		
		session.save(admin);
		HibernateUtil.commitTransaction();
		return true;
	}

	private static boolean adduser(User user) {
		Session session = HibernateUtil.beginTransaction();
		String email = user.getEmail();
		Query queryResult = session.createQuery("from User");

		List<User> users = (List<User>) queryResult.list();

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getEmail().equals(email)) {				
				HibernateUtil.rollbackTransaction();
				return false;
			}
		}		
		session.save(user);
		HibernateUtil.commitTransaction();
		return true;
	}
		
	public static List<Book> getBooksByCategory(final String category) {
		Session session = HibernateUtil.beginTransaction();
		Query queryResult = session.createQuery("from Book where category = '"+category+"'"); 
		List<Book> books = (List<Book>)queryResult.list();
		
		HibernateUtil.commitTransaction();
		return books;		
	}
	
	public static List<Book> getBooksByTitle(final String title) {
		Session session = HibernateUtil.beginTransaction();
		Query queryResult = session.createQuery("FROM Book WHERE title like '%"+title+"%'"); 
		//queryResult.setString("title", title);
		List<Book> books = (List<Book>)queryResult.list();		
		HibernateUtil.commitTransaction();
		return books;		
	}
	
	public static List<Book> getBooksByAuthor(final String author) {
		Session session = HibernateUtil.beginTransaction();
		Query queryResult = session.createQuery("FROM Book WHERE author like '%"+author+"%'"); 
		//queryResult.setString("title", title);
		List<Book> books = (List<Book>)queryResult.list();		
		HibernateUtil.commitTransaction();
		return books;		
	}
	
	public static Set<Book> getUsersBooks(int id) {
		
		Session session = HibernateUtil.beginTransaction();			
		String userQuery = "from User where id = :id"; 
		Query query = session.createQuery(userQuery); 
		query.setInteger("id", id);		
		User user = (User)query.uniqueResult();
		
		if(user==null) return null;
		
		Set<Book> books = user.getBooks();
		
		return (books == null || books.isEmpty()) ? null : books;		
	}
	
	public static List<Transaction> getUsersTransactions(int userID) {
		
		Session session = HibernateUtil.beginTransaction();			
		String userQuery = "from Transaction where userID = :userID and transactionType = :transactionType"; 
		Query query = session.createQuery(userQuery); 
		query.setInteger("userID", userID);
		query.setString("transactionType", "return");
		List<Transaction> transactions = (List<Transaction>)query.list();		
		
		return (transactions == null || transactions.isEmpty()) ? null : transactions;		
	}
	
	public static List<Transaction> getAllTransactions() {
		Session session = HibernateUtil.beginTransaction();	
		Query queryResult = session.createQuery("from Transaction"); 
		List<Transaction> transactions = (List<Transaction>)queryResult.list();
		return transactions;
	}
	
	public static List<Book> getRentedBooks() {
		Session session = HibernateUtil.beginTransaction();	
		Query query = session.createQuery("from Book where quantity < :quantity");
		query.setInteger("quantity", 10);
		List<Book> books = (List<Book>)query.list();
		return books;
	}
	
	public static List<User> getUsers() {
		Session session = HibernateUtil.beginTransaction();	
		Query queryResult = session.createQuery("from User"); 
		List<User> users = (List<User>)queryResult.list();
		return users;
	}
	
	public static List<Book> getAllBooks(String term) {
        Session session = HibernateUtil.beginTransaction(); 
        Query query = session.createQuery("FROM Book WHERE title like '%"+term+"%' or author like '%"+term+"%'");
        List<Book> books = (List<Book>)query.list();
        return books;
    }
	
	public static Book getBook(String value) {
		
		Integer isbn = Utils.parseIntFromString(value);
		if (isbn == null) return null;
		
		try {
			Session session = HibernateUtil.beginTransaction();			
			String userQuery = "from Book where isbn = :isbn"; 
			Query query = session.createQuery(userQuery); 
			query.setInteger("isbn", isbn);		
			Book book = (Book)query.uniqueResult();
			System.out.println(book);
			return book;
		}
		catch(NonUniqueResultException e) {
			System.out.println("Non unique book");
			e.printStackTrace();
			HibernateUtil.commitTransaction();
			return null;
		}
	}
	
	public static User getUser(int id) {
		
		try {
			Session session = HibernateUtil.beginTransaction();			
			String userQuery = "from User where id = :id"; 
			Query query = session.createQuery(userQuery); 
			query.setInteger("id", id);		
			User user = (User)query.uniqueResult();
			return user;
		}
		catch(NonUniqueResultException e) {
			System.out.println("Non unique user");
			e.printStackTrace();
			HibernateUtil.commitTransaction();
			return null;
		}
	}
	
	

	public static void main(String args[]) throws Exception {
		//HibernateUtil.recreateDatabase();
		
		// Adding book entries to the book_sebasta2 table
		/*Session hibernateSession = HibernateUtil.beginTransaction(); 
		Set<Book> books  = Utils.intialiseDataFromFile(); 
		for(Book book: books) {
			hibernateSession.save(book); 
		}		
		HibernateUtil.commitTransaction();*/

		// adding users and admins to tables
		/*Session hibernateSession = HibernateUtil.beginTransaction(); 
		
		User anoop = new User("Anoop", "Sebastian", "anoop@a.com", "pass");
		User tom = new User("Tom", "Dunne", "tom@a.com", "pass");
		User sean = new User("Sean", "Quinn", "sean@a.com", "pass");
		
		Librarian steve = new Librarian("Steve", "Jobs", "steve@a.com", "pass", 1234);
		Librarian bill = new Librarian("Bill", "Gates", "bill@a.com", "pass", 3456);
		
		hibernateSession.save(anoop); hibernateSession.save(tom); hibernateSession.save(sean);
		hibernateSession.save(steve); hibernateSession.save(bill);
		
		HibernateUtil.commitTransaction();*/	
		
		System.out.println(getUsers());
	}
}