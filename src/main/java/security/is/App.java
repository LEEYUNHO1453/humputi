package security.is;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;


public class App 
{
    public static void main( String[] args ) throws ImageProcessingException, IOException, NoSuchAlgorithmException 
    {
    	File file = new File( "C:\\Users\\이윤호\\Desktop\\zzz.jpg" );
    	
    	Metadata metadata = ImageMetadataReader.readMetadata(file);

    
   	for (Directory directory : metadata.getDirectories()) {
    	    for (Tag tag : directory.getTags()) {
    	        System.out.format("[%s] - %s = %s\n",  // pirnt out all of metadata
    	            directory.getName(), tag.getTagName(), tag.getDescription());
    	    }
    	    if (directory.hasErrors()) {
    	        for (String error : directory.getErrors()) {
    	            System.err.format("ERROR: %s", error);
    	        }
    	    }


    	} 
    	
    	System.out.println("\n\n");
    	
    	
   		// Date/Time
    	ExifSubIFDDirectory dateDirectory
    	    = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    	Date date
    	    = dateDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
 
    	
    	// Gps
    	GpsDirectory gpsDirectoryL
    	    = metadata.getFirstDirectoryOfType(GpsDirectory.class);
    	String gpsL
    	    = gpsDirectoryL.getString(GpsDirectory.TAG_LATITUDE);
    	GpsDirectory gpsDirectoryH
    		= metadata.getFirstDirectoryOfType(GpsDirectory.class);
	    String gpsH
	    	= gpsDirectoryH.getString(GpsDirectory.TAG_LONGITUDE);
	
    	
    	
    	System.out.format("%s\n",date);   	
    	System.out.format("%s\n",gpsL); 
    	System.out.format("%s\n",gpsH);
    	
    	System.out.println("\n");
    	
    	// make sha256 date,time,gps
    	System.out.println(Hashs.sha256(date.toString()));
    	System.out.println(Hashs.sha256(gpsL.toString()));
    	System.out.println(Hashs.sha256(gpsH.toString())); 
    	
    	    	
    }
}
