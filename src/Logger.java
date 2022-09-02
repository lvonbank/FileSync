// Copyright (c) 2021 LVonBank

package filesync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	public static String error (String filepath, String environment, String message) throws IOException {
		
		File directory = new File(filepath);
        // Create directory for non existed path.
        if (!directory.exists()) {
            directory.mkdir();
        }
        
		// Captures the current date and time
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		// Create file
		File file = new File(filepath + "\\" + dateFormat.format(date) + ".txt");
		
		DateFormat fullDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss - ");
		String errormessage = fullDateFormat.format(date) + environment + message;
		
		// Checks if log file is present
		if (!file.exists() && !file.isDirectory()) {
			file.createNewFile();
			
			FileWriter writerError = new FileWriter(file);
			writerError.write(errormessage);
			writerError.close();
		} 
		// Writes to existing log file
		else {
			FileWriter writerError = new FileWriter(file, true);
			writerError.write(System.lineSeparator() + errormessage);
			writerError.close();
		}
		return errormessage;
	}

}
