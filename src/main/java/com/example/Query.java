package main.java.com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Query {

	private static File file;
	private static RandomAccessFile rand;

	// This main function takes number from stdin,
	// then query in the file
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path = Constants.PATH;
		file = getFile(path);
		try {
			rand = new RandomAccessFile(file,"r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Use scanner to get input from stdin
		Scanner input = new Scanner(System.in);
		System.out.print(">");
		String line = input.nextLine().toLowerCase();
		
		while (!line.equals("exit")){
			long lineNum = 0;
			
			try {  
			    lineNum = Long.parseLong(line);  
			}  
			catch(NumberFormatException nfe) {  
			  	System.out.print(">");
				line = input.nextLine().toLowerCase();
				continue;
			}
			long progStartTime = System.currentTimeMillis();
			// Do the exponential search
			long position = exponentialSearch(lineNum);
			
			if (position != -1) {
				try {
					rand.seek(position * Constants.BYTES_OF_LINE);
					String entry = rand.readLine();
					System.out.println(entry);
				}
					catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			} else {
				System.out.println("No enrty found.");
			}
		    long progEndTime = System.currentTimeMillis();
		    long diff = progEndTime - progStartTime;
			System.out.println("\nFinished query in " + diff + " Milliseconds");
			
			System.out.print(">");
			line = input.nextLine().toLowerCase();
		}
		if (rand != null)
			try {
				rand.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * This method checks the existence of directory and log file.
	 * @param path Input data path.
	 * @return File object of log file.
	 */
	private static File getFile(String path) {

		File directory = new File(path);
		if (!directory.exists()){
			System.out.println("No directory found");
			System.exit(0);
		}
		File file = new File(directory, Constants.FILE_NAME);
		if (!file.exists()){
			System.out.println("No directory found");
			System.exit(0);
		}
		return file;
	}

	/**
	 * Perform a exponential search on the file,
	 * the position increment exponentially each iterate.
	 * @param target long target
	 * @return The position of line in file if there's a match. Return -1 if can't find a match.
     */
	private static long exponentialSearch(long target){

		long lower = 0;
		long upper = 0;
		long k = 0;
		
		while (true) {
			long lineNumber = getLineNumber(upper);
			// If there's a match, return the upper index.
			if (lineNumber == target) {
				return upper;
			}
			// If target number is between lower and upper, perform binary search.
			if (lineNumber > target) {
				return binarySearch(target, lower, upper);
			}
			// If upper is out of range of the file, perform binary search.
			if (lineNumber == -1) {
				return binarySearch(target, lower, upper);				
			}
			// If target is larger than upper, set lower to upper, 
			// upper grows exponentially each iterate.
			if (lineNumber < target) {
				lower = upper;
				upper =  upper + (long) Math.pow(2, k++);
			}
		}
	}

	/**
	 * Binary search, the only difference is the returned line number could be -1,
	 * means it's out of range of the file, upper is assigned to middle - 1.
	 * @param target Target number to query
	 * @param lower Lower index of binary search.
	 * @param upper Upper index of binary search.
     * @return Position of target in the file if found, -1 if not found.
     */
	private static long binarySearch(long target, long lower, long upper) {
		
		while(upper >= lower) {
			long middle = (lower + upper)/2;
			long lineNumber = getLineNumber(middle);
			// If time is -1, it's out of range of the file.
			// Reassign upper index and continue.
			if (lineNumber == -1) {
				upper = middle -1;
				continue;
			}
			if (lineNumber == target) {
				return middle;
			}
			if(lineNumber < target) {
				lower = middle + 1;
			}
			if(lineNumber > target) {
				upper = middle -1;
			}
		}
		return -1;
	}
	
	/**
	 * Get the line number of specific line in file.
	 * @param Position of line in file.
	 * @return Line number of the specific line, return -1 if can't read line.
     */
	private static long getLineNumber(long position){
		try {
			rand.seek(position * Constants.BYTES_OF_LINE);
			String line = rand.readLine();
			if (line == null)
				return -1;
			return Long.parseLong(line.substring(0, 10).trim());
		}
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
