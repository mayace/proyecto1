package compiler;

import java.io.File;

public class CeFile extends File {

	public CeFile(String pathname) {
		super(pathname);
	}


	public String getExtension(){
		String	name		= super.getName();
		int		dot_index 	= name.lastIndexOf('.');
		
		String extension = (dot_index==-1||dot_index+1==name.length()?null:name.substring(dot_index+1));
		return extension;
	}
	
	
}
