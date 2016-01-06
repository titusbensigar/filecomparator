import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opencsv.CSVReader;

/**
 * 
 */

/**
 * @author ubensti
 *
 */
public class FileComparator extends JFrame{
	
	public static String filename1 = null, filename2 = null, filename3 = null;
	
	JFrame jframe;
	
	final static JLabel statusbar = 
            new JLabel("Output of your selection will go here");

	public FileComparator() {
		
		
		JButton openButton,openButton2,openButton3;
		JLabel label = new JLabel("Scenario File:");;
        JTextField textField = new JTextField("",300);
        textField.setMinimumSize(new Dimension(300,30));
        textField.setSize(new Dimension(300,30));
		openButton = new JButton("",
		        createImageIcon("images/fileopen.png"));
		
		
		JLabel label2 = new JLabel("DB Extract File:");;
        JTextField textField2 = new JTextField("",300);
        textField2.setMinimumSize(new Dimension(300,30));
        textField2.setSize(new Dimension(300,30));
		openButton2 = new JButton("",
		        createImageIcon("images/fileopen2.png"));
		
		
		JLabel label3 = new JLabel("Output Location:");;
        JTextField textField3 = new JTextField("",300);
        textField3.setMinimumSize(new Dimension(300,30));
        textField3.setSize(new Dimension(300,30));
		openButton3 = new JButton("",
		        createImageIcon("images/fileopen2.png"));
		
		
	    JLabel msg = new JLabel("Choose scenario file and db extract for comparison", JLabel.CENTER);

	    
		// Create some buttons
	    JButton ok = new JButton("Compare");
	    JButton close = new JButton("Close");
	    JPanel file1buttonPanel = new JPanel();
	    file1buttonPanel.add(openButton);
	    JPanel file2buttonPanel = new JPanel();
	    file2buttonPanel.add(openButton2);
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.add(ok);
	    buttonPanel.add(close);

	    openButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(false);
	          int option = chooser.showOpenDialog(FileComparator.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	        	  File file = chooser.getSelectedFile();
	        	  
					String fullPath = file.getAbsolutePath();
					statusbar.setText("You chose " + fullPath);
					filename1 = fullPath;
					textField.setText(fullPath);
	          }
	          else {
	            statusbar.setText("You canceled.");
	          }
	        }
	      });
	    openButton2.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(false);
	          int option = chooser.showOpenDialog(FileComparator.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	        	  File file = chooser.getSelectedFile();
	        	  
					String fullPath = file.getAbsolutePath();
					statusbar.setText("You chose " + fullPath);
					filename2 = fullPath;
					textField2.setText(fullPath);
	          }
	          else {
	            statusbar.setText("You canceled.");
	          }
	        }
	      });
	    openButton3.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          
	          chooser.setAcceptAllFileFilterUsed(false);
	          chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	          int option = chooser.showOpenDialog(FileComparator.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	        	  File file = chooser.getSelectedFile();

					String fullPath = file.getAbsolutePath();
					statusbar.setText("You chose " + fullPath);
			        if(isWindows()) {
			        	if(fullPath == null || fullPath.trim().length() == 0) {
			        		fullPath = "c:";
			        	}
			        	filename3 = fullPath + "\\" +"Compare_Results.xlsx";
			        } else {
			        	filename3 = fullPath + "/" +"Compare_Results.xlsx";
			        }

					textField3.setText(filename3);
	          }
	          else {
	            statusbar.setText("You canceled.");
	          }
	        }
	      });
	    ok.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ev) {
	        	statusbar.setText("File comparison started... ");
	        	compare(jframe);
	        }
	      });
	    close.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ev) {
	        	System.exit(1);
	        }
	      });
	      
	    GridBagLayout layout = new GridBagLayout();
	    JPanel panel = new JPanel(layout);
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(2,2,2,2);
	    // Create a frame, get its root pane, and set the OK button as the
	    // default. This button is pressed if we press the Enter key while the
	    // frame has focus.
	    
	    
	    gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(msg, gbc);
	    
	    gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(label, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(textField, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(openButton, gbc);
	    
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(label2, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(textField2, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(openButton2, gbc);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(label3, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        panel.add(textField3, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(openButton3, gbc);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(buttonPanel, gbc);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(statusbar, gbc);
	    
	    // Layout and Display
	    
	    setSize(550, 500);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);

	    Container content = getContentPane();
	    content.add(panel);
	    jframe = this;
	   
	    
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	  protected static ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = FileComparator.class.getResource(path);
	    if (imgURL != null) {
	      return new ImageIcon(imgURL);
	    } else {
	      System.err.println("Couldn't find file: " + path);
	      return null;
	    }
	  }
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//
//		if(args.length < 3) {
//			System.out.println("Usage: firstfile secondfile log");
//			System.exit(0);
//		}
//		
		FileComparator fileComparator = new FileComparator();
		fileComparator.setVisible(true);
		
	}

	private static boolean compare(JFrame frame) {
		statusbar.setText("File comparison started... ");
		XSSFWorkbook f;
	    try {
	    	if((filename1 == null || filename2 == null) || (filename1.trim() == "" || filename2.trim() == "")){
	    		JPanel dlgPanel = new JPanel();
	    		JButton dlgclose = new JButton("Close");
	    		JLabel dlgmsg = new JLabel("Scenaria and DB Extract file paths can not be empty", JLabel.CENTER);
	    		dlgPanel.add(dlgmsg);
	    		dlgPanel.add(dlgclose);
	    		JDialog jd = new JDialog(frame);
		  	      jd.setTitle("File Not Found");
		  	      jd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		  	      jd.add(dlgPanel);
		  	      jd.pack();
		  	      jd.setLocationRelativeTo(null);
		  	      jd.setVisible(true);
		  	      jd.setDefaultCloseOperation(HIDE_ON_CLOSE);
	    		
		  	    dlgclose.addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent ev) {
			        	jd.setVisible(false);
			        	jd.dispose();
			        	
			        }
			      });
	    		return false;
	    	}
	    	int totalmatch=0, totalmismatch=0;
	    	String extn = filename1.substring(filename1.lastIndexOf(".") +1, filename1.length());
	    	String extn2 = filename2.substring(filename2.lastIndexOf(".") +1, filename2.length());
	    	System.out.println("extn-" + extn + ";extn2="+extn2);
	    	System.out.println("status="+ (extn == null || extn2 == null || !extn.trim().toLowerCase().equals("csv") || !extn2.trim().toLowerCase().contains("xls")));
	    	if(extn == null || extn2 == null || !extn.trim().toLowerCase().equals("csv") || !extn2.trim().toLowerCase().contains("xls")) {
	    		JPanel dlgPanel = new JPanel();
	    		JButton dlgclose = new JButton("Close");
	    		JLabel dlgmsg = new JLabel("Unsupported file format, scenario file should be CSV and DB Extract should be XLS or XLSX", JLabel.CENTER);
	    		dlgPanel.add(dlgmsg);
	    		dlgPanel.add(dlgclose);
	    		JDialog jd = new JDialog(frame);
		  	      jd.setTitle("UnSupported File Type");
		  	      jd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		  	      jd.add(dlgPanel);
		  	      jd.pack();
		  	      jd.setLocationRelativeTo(null);
		  	      jd.setVisible(true);
		  	      jd.setDefaultCloseOperation(HIDE_ON_CLOSE);
	    		
		  	    dlgclose.addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent ev) {
			        	jd.setVisible(false);
			        	jd.dispose();
			        	
			        }
			      });
	    		return false;
	    	}
	    	
	    	CSVReader reader = new CSVReader(new FileReader(filename1));
	    	FileInputStream workbook = new FileInputStream(new File(filename2));
	        String [] nextLine;
	        f = new XSSFWorkbook(workbook);
	        // Get the first sheet
	        XSSFSheet sheet = f.getSheetAt(0);
	        reader.readNext();//skip header
	        boolean ismatch = false;
	        
	        
	        XSSFWorkbook wb = new XSSFWorkbook();   //or new HSSFWorkbook();
	        XSSFSheet wsheet = wb.createSheet();
	        XSSFRow wrow =  wsheet.createRow(0);
	        XSSFCell wcell=wrow.createCell(0);
	        wcell.setCellValue("uin");
	        XSSFCell wcell2=wrow.createCell(1);
	        wcell2.setCellValue("Results");
	        
	        String outputfile = "";
	        if(filename3 == null || filename3.trim().length() == 0) {
		        if(isWindows()) {
		        	if(filename3 == null || filename3.trim().length() == 0) {
		        		filename3 = "c:";
		        	}
		        	outputfile = filename3 + "\\" +"Compare_Results.xlsx";
		        } else {
		        	outputfile = filename3 + "/" +"Compare_Results.xlsx";
		        }
	        } else {
	        	outputfile = filename3;
	        }
	        List<String> mismatchUINs = new ArrayList<String>();
	        XSSFRow outputrow = null;
	        XSSFCell outputcell1 = null;
	        XSSFCell outputcell2 = null;
	        String uin = null;
	        int index = 1;
	        while ((nextLine = reader.readNext()) != null) {
	        	ismatch = false;
	           // nextLine[] is an array of values from the line
//	           System.out.println(nextLine[0] + "\t"+ nextLine[4] );
		      // Loop over first 10 column and lines
	           Iterator<Row> rowIterator = sheet.iterator(); // Create iterator object
	           rowIterator.next();//skip header
               while(rowIterator.hasNext()) {
                   Row row = rowIterator.next(); //Read Rows from Excel document
                   Cell cell = row.getCell(5);
                   Cell cell2 = row.getCell(11);
                   Map<String,String> respMap = new HashMap<String,String>();
            	   Map<String,String> respMap2 = new HashMap<String,String>();
                   if(nextLine[0] != null && cell.getStringCellValue() != null && nextLine[0].trim().equals(cell.getStringCellValue().trim())) {
                	   ismatch = true;
                	   uin = nextLine[0].trim();
                	   JSONObject json = new JSONObject(cell2.getStringCellValue());
                	   JSONArray json2 = new JSONArray(nextLine[4]);
//                	   System.out.println("Match " +json2 + "\t"+json ); //print string value 
                	   
                	   if(json2 != null && json2.length() >0) {
                		   if(json != null && json.has("mods")){
                			   JSONArray jarr = json.getJSONArray("mods");
                			   if(jarr != null && jarr.length() >0) {
                				   for(int i =0; i < jarr.length() ; i++){
    	                			   JSONObject jobj = jarr.getJSONObject(i);
    	                			   if(jobj != null){
    	                				   if(jobj.has("did") && jobj.has("r")) {
    	                					   String did = jobj.getString("did");
    	                					   if(did.contains("RESPONSE")) {
    	                						   JSONArray rArr = jobj.getJSONArray("r");
    	                						   if(rArr != null && rArr.length() >0) {
    	                							   String rRes = rArr.getString(0);
    	                							   respMap.put(did, rRes);
    	                						   }
    	                					   }
    	                				   }
    	                			   }
    	                		   }
                			   }
	                		   for(int i =0; i < json2.length() ; i++){
	                			   JSONObject jobj = json2.getJSONObject(i);
	                			   if(jobj != null){
	                				   if(jobj.has("did") && jobj.has("r")) {
	                					   String did = jobj.getString("did");
	                					   System.out.println("did="+did +"\t"+respMap);
	                					   if(did.contains("RESPONSE")) {
		                					   if(respMap.containsKey(did)) {
		                						   JSONArray rArr = jobj.getJSONArray("r");
    	                						   if(rArr != null && rArr.length() >0) {
    	                							   String rRes = rArr.getString(0);
    	                							   respMap2.put(did, rRes);
			                						   if(!respMap.get(did).equals(rRes)){
			                							   ismatch= false;  
			                						   }
    	                						   }
		                					   }
	                					   }
	                				   }
	                			   }
	                		   }
                			   
                		   }
                	   }
                	   System.out.println("Match uin="+ uin+"\t" +respMap + "\t"+respMap2 );
                   } else {
//                	   System.out.println("No Match " + nextLine[0] + "\t" +cell.getStringCellValue()); //print string value
                   }
               }
                
	   	        String status = "True";
               if(ismatch) {
            	   totalmatch++;
               } else {
            	   totalmismatch++;
            	   status = "False";
            	   mismatchUINs.add(uin);
               }
               	outputrow =  wsheet.createRow(index);
	   	        outputcell1=outputrow.createCell(0);
	   	        outputcell1.setCellValue(uin);
	   	        outputcell2=outputrow.createCell(1);
	   	        outputcell2.setCellValue(status);
	   	        index++;
		    }
	        File ff = new File(outputfile);
	        if (ff.exists()) {
	           ff.delete();     //clean old file
	        }
	        FileOutputStream fileOut = new FileOutputStream(outputfile);
	        wb.write(fileOut);
	        fileOut.close();
	        if(totalmismatch > 0) {
	        	statusbar.setText("File comparison completed with some mismatches... ");
	        } else {
	        	statusbar.setText("File comparison completed successfully without any mismatches... ");
	        }
	        System.out.println("total match = " + totalmatch + "\t totalmismatch=" +totalmismatch );
	        reader.close();
            f.close();
            workbook.close(); //Close the XLS file opened for printing
            System.out.println("Mismatch uins -->" + mismatchUINs);
	    }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return true;
	}

	private static String OS = null;
	   public static String getOsName()
	   {
	      if(OS == null) { OS = System.getProperty("os.name"); }
	      System.out.println("OS===" + OS);
	      return OS;
	   }
	   public static boolean isWindows()
	   {
	      return getOsName().startsWith("Windows");
	   }
}
