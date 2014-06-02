/**
 * 
 */
package com.ide.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author ANOOP
 * 
 */
public class IDEUtils {

	/**
	 * Validates the String against null value and zero length
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		return (input == null || input.isEmpty());
	}

	/**
	 * Validates an array of Strings against null value and zero length
	 * 
	 * @param inputs
	 * @return boolean
	 */
	public static boolean validate(String[] inputs) {

		if (inputs == null)
			return false;

		for (String input : inputs) {
			if (isEmpty(input)) {
				return false;
			}				
		}
		return true;
	}

	/**
	 * Ensure a directory or file exits before performing any operation to that
	 * file or directory
	 * 
	 * @param String
	 * @return void
	 */
	public static boolean checkFileStatus(final String filePath) {

		if (isEmpty(filePath)) {
			System.out
					.println("File path specified is not valid\nFile/Directory not created");
			return false;
		}
		File file = new File(filePath);

		if (file.exists())
			return true;
		if (filePath.contains(".")) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				System.out.println("An error occured while creating file.");
				e.printStackTrace();
				return false;
			}
		} else {
			return file.mkdir();
		}
	}
	
	public static void deleteDirectory(File file, StringBuffer buffer) throws IOException{
		if(file.isDirectory()) {
			
			// if directory is empty
			if(file.list().length == 0) {
				file.delete();
				buffer.append("Deleting Empty directory: ").append(file.getName()).append("\n");
			}
			else {
				for (String f: file.list()) {
					File temp = new File(file,f);
					deleteDirectory(temp, buffer);
				}
				
				if(file.list().length == 0){
					file.delete();
					buffer.append("Deleting directory: ").append(file.getName()).append("\n");
				}
			}
		} else {
			file.delete();
			buffer.append("Deleting file: ").append(file.getName()).append("\n");
		}
	}
}