package security.is;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.file.FileTypeDirectory;




public class App 
{
    public static void main( String[] args ) throws ImageProcessingException, IOException, NoSuchAlgorithmException 
    {
    	File file = new File( "C:\\Users\\이윤호\\Desktop\\zzz.jpg" );
    	String fName = file.getName();
    	long fileSize = file.length();
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
    	
    	
    	// 파일 타입 name	    
	    FileTypeDirectory FType
    		= metadata.getFirstDirectoryOfType(FileTypeDirectory.class);
	    String fType
    		= FType.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME);   
	    
	    // 파일 타입 full name 
	    FileTypeDirectory FLType
			= metadata.getFirstDirectoryOfType(FileTypeDirectory.class);
	    String fLType
			= FLType.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_LONG_NAME);
	    
	    
	    
	    System.out.format("%s\n",fType);
	    System.out.format("%s\n",fLType);
	    System.out.format("%s\n",fileSize);
	    System.out.format("%s\n",fName);
    	
    	
    	System.out.println("\n");
    	
    	// sha256   해상도, 넓이, 높이
    	
    	System.out.println(Hashs.sha256(fLType+"IS"));
    	System.out.println(Hashs.sha256(fType+"IS"));
    	System.out.println(Hashs.sha256(fileSize+"IS"));
    	System.out.println(Hashs.sha256(fName+"IS"));
    	
    	
    	
    	
    	    	
    }
}
