// Copyright (c) 2021 LVonBank

package filesync;

import java.io.File;
import java.io.IOException;

public class FileChecker {
	
	public static boolean checkfile (String filepath, String environment, String filename, String fileextention) throws IOException {
		
		boolean fileExists = false;
		
		File file = new File(filepath + "\\" + environment + "\\" + filename + "." + fileextention);
		if (file.exists() && file.isFile())  {
		   fileExists = true;
		}
		return fileExists;
	}
	
	public static void checkdir (String filepath, String environment) throws IOException {

		File directory = new File(filepath + "\\" + environment);
        // Create directory for non existed path.
        if (!directory.exists() && directory.isDirectory()) {
            directory.mkdir();
        }
	}

}
