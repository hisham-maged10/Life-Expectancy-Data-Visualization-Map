package LifeExpectancyPkj;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class LifeExpectancyDataHandler {

  // a private Field variable made to hold and parse the CSV Records
  private CSVParser csvParser;
  // a private field Map that will get the Country Id as Key and life Expectancy as value (used with map in shadeCountries())
  private Map<String,Float> lifeExpectancyData;
  {
    this.lifeExpectancyData = new HashMap<>();
  }
  // Default Constructor uses the getFile instance method that uses a JFileChooser to choose a csv format file
  public LifeExpectancyDataHandler()
  {
    initCSVParser(this.getFile());
  }
  // String Constructor that uses the CSVParser.Parse String version method to make the CSVParser
  public LifeExpectancyDataHandler(String filePath)
  {
    initCSVParser(filePath);
  }
  // File Constructor that uses the CSVParser.parse File version method to make the CSVParser
  public LifeExpectancyDataHandler(File csvFile)
  {
    initCSVParser(csvFile);
  }


  // puts the data from the csv file of the life expectancy and key is country id and value is life expectancy
  public Map<String,Float> getLifeExpectancyData()
  {
    if(this.lifeExpectancyData.isEmpty())
    {
      for(CSVRecord r : this.getRecords())
      {
//        System.out.println(r);
        try {
          this.lifeExpectancyData.put(r.get(3), Float.parseFloat(r.get(4)));
        }catch(NumberFormatException ex)
        {
          this.lifeExpectancyData.put(r.get(3),0.0F);
        }
      }
        return this.lifeExpectancyData;
    }
    else{
      return this.lifeExpectancyData;
    }
  }




  // main use of the class, it returns the List<CSVRecords> of the parsed CSV file
  public List<CSVRecord> getRecords()
  {
    try{
      return this.csvParser.getRecords();
    }catch(IOException ex)
    {
      System.err.println(ex.getMessage());
    }
    return null;
  }


  // a private helper method that initializes the csvParser private member using a File version checking whether
  //  //  it's a CSV file first or not
  private void initCSVParser(File file)
  {
    try {
      if (file == null || !this.getFileNameExtension(file).equalsIgnoreCase("csv"))
        throw new IllegalArgumentException("You must use a csv Format file only!");
      else {
        this.csvParser = CSVParser.parse(file, Charset.defaultCharset(),CSVFormat.DEFAULT);
      }

    }catch(IOException ex)
    {
      System.err.println(ex.getMessage());
    }catch(Exception ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  // a private Helper method that initializes the csvParser private member using a String version checking whether
  //  it's a CSV file first or not
  private void initCSVParser(String filePath)
  {
    if(filePath.toLowerCase().matches("^.*\\.csv$"))
    {
      initCSVParser(new File(filePath));
    }
    else
      throw new IllegalArgumentException("You must use a CSV format file only");
  }

  // a Private helper method that gets the extension of the File given to it to check whether it's a csv file or not
  private String getFileNameExtension(File file)
  {
    if(file == null)
      throw new IllegalArgumentException("Passed file can't be null");
    String fileName;
    int idx = (fileName = file.getName()).lastIndexOf(".");
    return idx == -1 ? "" : fileName.substring(++idx);
  }

  // a private method that is used to get the file if the default constructor is used
  private File getFile()
  {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setCurrentDirectory(new File("."));
    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files","CSV","csv","Csv");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);
    return returnVal == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
  }

  // made for testing only
  // initial test: works
//  public static void main(String[] args) {
//    LifeExpectancyDataHandler info = new LifeExpectancyDataHandler();
////    for(CSVRecord r : info.getRecords())
////    {
////      System.out.println(r.get(3)+", "+r.get(4));
////    }
//    System.out.println(info.getLifeExpectancyData());
//  }

}
