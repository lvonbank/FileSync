// Copyright (c) 2021 LVonBank

package filesync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JScrollBar;
import javax.swing.JTextArea;

import org.javatuples.KeyValue;

public class FileSynchronizer {
	
	public static void syncfiles (ArrayList<KeyValue<String, String>> SyncPathList, String TempDir, String LogDir, String FileName, String FileExtention, JTextArea display, JScrollBar scrollbar) throws IOException, InterruptedException {
        for (KeyValue<String, String> pathTuple : SyncPathList) {
       		String dir = pathTuple.getKey();
       		String env = pathTuple.getValue();
       		
       		String TempPath = buildTempPath(TempDir, dir, env);

        	//System.out.println("Running a file check for " + env);
        		
			//Checks if file exists in prod and copies it to temp folder on C drive 
			if (FileChecker.checkfile(dir, env, FileName, FileExtention)) {
				try {
						FileCopier.copyfile(dir, TempPath, env, FileName, FileExtention);
						Overseer.updateMessageCenter(display, scrollbar, (Overseer.getDate() + env + "'s " + FileExtention + " file successfully saved to temporary directory"));
				}
					// Recovers from file copy error
			    catch (IOException error) {
				       	Overseer.updateMessageCenter(display, scrollbar, Logger.error(LogDir, env, "'s production " + FileExtention + " file could not be copied"));
			    }
			}
					
			//Checks if file went missing on prod and copies it from the temp folder, but only if temp has a file 
			if (!FileChecker.checkfile(dir, env, FileName, FileExtention)) {
						
				// Alerts message center that file was not found and delays check/replace for 10 seconds
				Overseer.updateMessageCenter(display, scrollbar, Logger.error(LogDir, env, "'s " + FileExtention + " file not found on first check"));
				// Sleep for 10 seconds
				Thread.sleep(10000);
				
				if (!FileChecker.checkfile(dir, env, FileName, FileExtention)) {
					if (FileChecker.checkfile(TempPath, env, FileName, FileExtention)) {
						try {
							FileCopier.copyfile(TempPath, dir, env, FileName, FileExtention);
							// Update message center and log info
							Overseer.updateMessageCenter(display, scrollbar, Logger.error(LogDir, env, "'s " + FileExtention + " file not found on second check and was replaced"));
						}
						// Recovers from file copy error
					    catch (IOException error) {
					        Overseer.updateMessageCenter(display, scrollbar, Logger.error(LogDir, env, "'s temporary " + FileExtention + " file could not be copied"));
					    }
					}
					else {
						Overseer.updateMessageCenter(display, scrollbar, Logger.error(LogDir, env, "'s " + FileExtention + " was skipped because no temporary file exists"));
					}
				}
			}
        }
	}
	
	public static String buildTempPath (String tempDir, String rootDir, String environment) throws IOException {
		// Grabs the base folder at the end of the root path
        String rootpath = new File(rootDir).getPath();
        String rootFolder = rootpath.substring(rootpath.lastIndexOf("\\") + 1);
        
        String masterTempDir = tempDir + "\\" + rootFolder;
        
        // Checks to make sure directory exists or create the folder
        FileChecker.checkdir(masterTempDir, environment);
        
        return masterTempDir;
	}
	
}
