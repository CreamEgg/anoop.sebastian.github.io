package com.dcu.ee417.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.dcu.ee417.model.Book;
import com.dcu.ee417.model.Librarian;
import com.dcu.ee417.model.User;

public class Utils {
	public static final String oracleDatabaseUrl = "jdbc:oracle:thin:@136.206.35.131:1521:SSD";
	public static final String oracleDatabaseDriver = "oracle.jdbc.driver.OracleDriver";
	public static final String oracleUserName = "ee_user"; 
	public static final String oraclePassword = "ee_pass";
	
	public static final String mySQLDatabaseUrl = "jdbc:mysql://localhost/sakila";
	public static final String mySQLDatabaseDriver = "com.mysql.jdbc.Driver";
	public static final String mySQLUserName = "root"; 
	public static final String mySQLPassword = "anumol";
	
	/**
	 * Validates the String against null value and zero length
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		return (input == null || input.isEmpty());
	}
	
	/**
	 * Validates an array of Strings against null value and zero length
	 * @param inputs
	 * @return boolean
	 */
	public static boolean validate(String[] inputs) {
		
		if(inputs==null) return false;
		System.out.println("Validating: "+ Arrays.toString(inputs));
		for(String input: inputs) {
			if(Utils.isEmpty(input)) return false;
		}
		return true;
	}
	
	/**
	 * Increments a date object by the specified number of days
	 * @param date, the date to be modified
	 * @param daysToIncrement, the number of days to increment
	 * @return Date
	 * @throws ParseException 
	 */
	public static String addDays(String date, int daysToIncrement) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date currentDate = dateFormat.parse(date);
		
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(Calendar.DATE, daysToIncrement);
		return dateFormat.format(calender.getTime());
    }
	
	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	
	public static Set<Book> intialiseDataFromFile() throws Exception{
		Set<Book> books = new HashSet<>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(new File("src/data.txt")))) {
			String line = "";
			
			while((line=reader.readLine())!=null) {
				if(!line.isEmpty()) {
					
					String[] details = line.split("[|]");	
					//System.out.println(Arrays.toString(details));
					Book book = new Book(details[0], details[1], getImagePath(details[3],details[0]), details[2], details[3], 10);
					books.add(book);
				}
			}
		}	
		return books;
	}
	
	private static String getImagePath(String category, String title) {
		
		if(validate(new String[]{category,title})) {
			String cat = category.toLowerCase().trim();
			return "img/"+cat+"/"+title+".jpg";
		}
				
		return "http://placehold.it/200x200";
	}
	
	public static boolean validateUser(User user) {
		return validate(new String[]{ 
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPassword()});
	}
	
	public static boolean validateLibrarian(Librarian user) {
		boolean status =  validate(new String[]{ 
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPassword()});
		if(status && user.getLibrarianID() > 0) return true;
		return false;
	}
	
	public static Integer parseIntFromString(String value) {
		try {
			int intValue = Integer.parseInt(value);
			return intValue;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static void main(String args[]) throws Exception {
//		Set<Book> books = intialiseDataFromFile();
//		
//		for(Book b: books) {
//			System.out.println(b.getCoverPath());
//		}
		//System.out.println(addDays("09/04/2014", -2));
		System.out.println(parseIntFromString(null));
		
	}
}
