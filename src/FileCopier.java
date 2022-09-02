// Copyright (c) 2021 LVonBank

package filesync;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class FileCopier {
	
	public static void copyfile (String sourcefilepath, String destfilepath, String environment, String filename, String fileextention) throws IOException {
		
		File source = new File(sourcefilepath + "\\" + environment + "\\" + filename + "." + fileextention);
		File dest = new File(destfilepath + "\\" + environment + "\\" + filename + "." + fileextention);
		FileUtils.copyFile(source, dest);
	}

}
