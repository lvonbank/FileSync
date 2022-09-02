// Copyright (c) 2021 LVonBank

package filesync;

import java.io.IOException;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.javatuples.KeyValue;
import org.ini4j.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Overseer {

	public static void main(String[] args)  {
		// Tags a version identifier
		String Version = "v2.1.4";
		
		// Hold the system messages
		// String message = "";
		
		// Start of app window setup
	    JPanel mainpanel = new JPanel();
	    mainpanel.setBorder (new TitledBorder (new EtchedBorder(), "Message Center"));

	    // Creates the main panel components
	    JTextArea display = new JTextArea(16, 58);
	    display.setEditable (false); // set textArea non-editable
	    
	    // Creates a scroll bar 
	    JScrollPane scrollpane = new JScrollPane(display);
	    scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    JScrollBar scrollbar = scrollpane.getVerticalScrollBar();
	    /*
	    // Auto scroll to bottom of screen
	    scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    }); 
	    */

	    //Add Textarea in to main panel
	    mainpanel.add(scrollpane);

	    JFrame frame = new JFrame();
	    // Sets a default window name
	    frame.setTitle("FileSync"); 
	    // Moved below to use WorkingPath
	    //frame.setIconImage((new ImageIcon("C:\\Tasks\\FileSync\\Assets\\icon.ico")).getImage());
	    frame.add(mainpanel);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setResizable(false);
	    frame.setVisible(true);
        // End of app window setup

        try{
    		/* 
    		// Sets variables
            String FileName = "adlshost";
            String FileExtention = "ini";
            String MasterDir = "C:\\Tasks\\FileSync";
            String LogDir = masterDir + "\\Logs";
            String ASPTempDir = masterDir + "\\Temp\\ASPShare";
            String KWTempDir = masterDir + "\\Temp\\KWShare";
            String ASPDir = "\\\\kp-file01\\ASPShare"; //"C:\\Users\\lvonbank\\Desktop\\ASPShare";
            String[] ASP = {"AMWater","AustinEnergy","CenterPoint","EPROD","KorWeb-Contractors","MPNexLevel","Vannguard","Windstream","Xcel"};
            String KWDir = "\\\\kp-file01\\KWShare"; //"C:\\Users\\lvonbank\\Desktop\\KWShare";
            String[] KW = {"KorWeb","KorWeb-AR","KorWeb-KW2","KorWeb-MN","KorWeb-MS","KorWeb-OH","KorWeb-SC","KorWeb-TN","KorWeb-TX"};
        	*/
        	
        	// Gets variables from the Master INI file
        	String MasterDir = new File(".").getCanonicalPath();
            Wini Config = new Wini(new File(MasterDir + "\\FileSync.INI"));
            
            // Default sync time is zero!
            int SyncLoopDelay = Config.get("Runtime", "SyncLoopDelay", int.class);
            String FileName = Config.get("File", "FileName");
            String FileExtention = Config.get("File", "FileExtention");
            //String MasterDir = Config.get("Directory", "MasterDir");
            String LogDir = MasterDir + Config.get("Directory", "LogDir");
            String TempDir = MasterDir + Config.get("Directory", "TempDir");
            String[] RootDir = Config.get("Directory", "RootDir").split(",");
            String[] InstanceList = Config.get("Directory", "InstanceList").split(",");
            int MsgLineTotal = Config.get("Window", "TotalMsgCenterLines", int.class);
            
            // Loops through instances and root directories to build a paths list
            ArrayList<KeyValue<String, String>> SyncPathList = new ArrayList<KeyValue<String, String>>();
            for (String dir : RootDir) {
            	 for (String env : InstanceList) {
            		 // Only adds sync paths that exist and makes sure env is valid!
            		 if (new File(dir + "\\" + env).exists() && !env.isEmpty()) {
            			 SyncPathList.add(new KeyValue<String, String>(dir, env));
            			 //System.out.println(dir + "\\" + env);
            		 }
            	 }
            }
            
            // Validates that any sync path exists
            if (SyncPathList.isEmpty()) {
            	throw new IOException("Invalid file path(s)");
            }
            
            // Re-write application window to add sync extension type, version info, and icon
            frame.setTitle("FileSync "+ FileExtention +" - "+ Version);
            frame.setIconImage((new ImageIcon(MasterDir + "\\Assets\\icon.ico")).getImage());
            
            // Loops through the designated files in each folder making a backup of each
            // and drops the backup file back into the originating folder if it disappears using the latest copy.
            while (true) {
            	FileSynchronizer.syncfiles(SyncPathList, TempDir, LogDir, FileName, FileExtention, display, scrollbar);

                // Pauses the program until the next scan
               	Thread.sleep(SyncLoopDelay); //Scan and copy every 5 min. 300000
               	
            	// Flushes message center text area after a given amount of lines. This is to prevent memory overload.
            	if (display.getText().split("\n").length >= MsgLineTotal) {
            		clearMessageCenter(display, scrollbar);
            	}
            }
            
        }
        catch (InterruptedException error) {
           	updateMessageCenter(display, scrollbar, (getDate() + error.getMessage()));
        }
        catch (IOException error) {
          	updateMessageCenter(display, scrollbar, (getDate() + error.getMessage()));
        }
        // Catch basically any error related to finding Master INI file.
        catch (Exception error){
        	updateMessageCenter(display, scrollbar, (getDate() + error.getMessage()));
        }

	};
	
	public static void updateMessageCenter (JTextArea displaypanel, JScrollBar scrollbar, String message) {
		// Repaint frame
		displaypanel.append(message + "\n");
		displaypanel.repaint();
		scrollbar.setValue(scrollbar.getMaximum());
	};
	
	public static void clearMessageCenter (JTextArea displaypanel, JScrollBar scrollbar) {
		// Repaint frame
		displaypanel.selectAll();
		displaypanel.replaceSelection("");
		displaypanel.repaint();
		scrollbar.setValue(scrollbar.getMaximum());
	};
	
	public static String getDate () {
		Date date = new Date();
		DateFormat fullDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss - ");
		return fullDateFormat.format(date);
	};
	
}
