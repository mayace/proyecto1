package dot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class CeDotGraph {
	
	String					name;
	HashMap<String,String>	items;
	HashMap<String,String>	assns;
	HashMap<String,String>	others;
	CeDotGraphType			type;
	File					dotfile;
	
	public enum CeDotGraphType{
		DIGRAPH ("digraph"),
		GRAPH	("graph");
		
		private final String text;
		private CeDotGraphType(String text) {
			// TODO Auto-generated constructor stub
			this.text=text;
		}
		public String getText() {
			return text;
		}
	}
	
	
	public CeDotGraph(String name,String pathfile){
		this.name	=	name;
		this.items	=	new HashMap<>();
		this.assns	=	new HashMap<>();
		this.others	=	new HashMap<>();
		this.type	=	CeDotGraphType.DIGRAPH;
		this.dotfile=	new File(pathfile);
	}
	
	public String addItem(String label){
		String key	=	getNextItemId();
		String val	=	String.format("%s [label=\"%s\"];\n", key,label);
		
		items.put(key, val);
		return key;
	}
	public String addAssn(String from,String to){
		String key	=	getNextAssnId();
		String val	=	String.format("%s->%s;\n", from,to);
		
		assns.put(key, val);
		return key;
	}
	public String addOther(String other){
		String key	=	getNextOtherId();
		String val	=	other+"\n";
		
		others.put(key, val);
		return key;
	}
	
	public String getDotString(){
		String	dot_string	=	"";
		String	dot_assns	=	"";	
		String	dot_items	=	"";	
		String	dot_others	=	"";	
			
		
		for(Map.Entry<String,String> entry: this.items.entrySet()){
			dot_items	+=	entry.getValue();
		}
		for(Map.Entry<String,String> entry: this.assns.entrySet()){
			dot_assns	+=	entry.getValue();
		}
		for(Map.Entry<String,String> entry: this.others.entrySet()){
			dot_others	+=	entry.getValue();
		}
		
		dot_string		=	String.format("digraph %s{\n %s %s %s}", this.name,dot_others,dot_items,dot_assns);
		
		return dot_string;
	}
	
	
	public void saveToFile() throws IOException{
		Files.write(this.dotfile.toPath(), this.getDotString().getBytes());
	}
	
	public File getGraphImg() throws IOException{
		this.saveToFile();
		File	imgfile	=	new File(this.dotfile.getPath()+".png");
		String	command	=	String.format("dot %s -Tpng -o %s", this.dotfile.getPath(),imgfile.getPath());
		Process exec 	=	Runtime.getRuntime().exec(command);
		
		return imgfile;
	}
	
	public String getNextItemId(){
		return String.valueOf(this.items.size());
	}
	public String getNextAssnId(){
		return String.valueOf(this.assns.size());
	}
	public String getNextOtherId(){
		return String.valueOf(this.others.size());
	}

	public File getDotfile() {
		return dotfile;
	}
	
	
	/*String			name;
	CeDotGraphType	type;
	CeDotItems		items;
	CeDotAssns		assns;
	
	public enum CeDotGraphType{
		DIGRAPH,GRAPH;
	}
	
	public CeDotGraph(String name){
		this.name	=	name;
		this.items	=	new CeDotItems();
		this.assns	=	new CeDotAssns();
	}

	public String getDotString(){
		return String.format("%s %s{\n%s\n%s\n}",this.type,this.name,this.items.getDotString(),this.assns.getDotString());
	}
	public void saveToFile(String pathfile) throws IOException{
		Files.write((new File(pathfile)).toPath(),this.getDotString().getBytes());
	}
	public CeDotItem setItem(String label){
		return items.set(label);
	}
	public CeDotAssn setAssn(String key_from,String key_to){
		CeDotItem item_from	=	items.get(key_from);
		CeDotItem item_to	=	items.get(key_to);
		
		if(item_from==null||item_to==null)
			return null;
		
		return assns.set(item_from, item_to);
	}*/
}
