package security.is;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;


public class App 
{
    public static void main( String[] args ) throws ImageProcessingException, IOException 
    {
    	File file = new File( "C:\\Users\\이윤호\\Desktop\\14444.jpeg" );
    	Metadata metadata = ImageMetadataReader.readMetadata(file);
    	
    	for (Directory directory : metadata.getDirectories()) {
    	    for (Tag tag : directory.getTags()) {
    	        System.out.println(tag);
    	    }
    	}
    	
    }
}
