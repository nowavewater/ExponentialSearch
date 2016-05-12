package main.java.com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

public class Generator {

	static final String ab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// File path	
		long count = 0;
		
		File file = new File(Constants.PATH + Constants.FILE_NAME);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
			System.out.println("Generating logs...");
				
			FileWriter writer = new FileWriter(file);			
			for (long i=1; i<=Constants.num1; i++){
				for (long j=1; j<=Constants.num2; j++){
					long rand = (long) (Math.random() * 3 + 1);
					count += rand;
					String line =  getLineNumber(count) + " " +
							randomString(20) + 
							" 2016-01-01T01:12:34 " + 
							"\r\n";
					writer.write(line);
				}
			}
			writer.flush();
			writer.close();
			System.out.println("Finished.");
		} catch (IOException e) {
				
			// TODO Auto-generated catch block
				
			e.printStackTrace();
		}
	
	}
	
	private static String randomString( int len ){
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append( ab.charAt( rnd.nextInt(ab.length()) ) );
		return sb.toString();
	}
	
	private static String getLineNumber(long count){
		StringBuilder result = new StringBuilder(Long.toString(count));
		int offset = 10 - String.valueOf(result).length();
		while (offset >= 0){
			result.append(' ');
			offset--;
		}
		return result.toString();
	}

}
