package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import java_cup.runtime.Symbol;

public class CeParseAttr extends HashMap<String,Object>{
	/**
	 * Almacena el nombre y el valor de los atributos creados
	 */

	public CeParseAttr(){}
	/**
	 * Instanciar con un atributo inicial...
	 * @param nombre
	 * @param valor 
	 */
	public CeParseAttr(String nombre, Object valor){
		this.set(nombre, valor);
	}
	/**
	 * Crea | Modifica un atributo
	 * @param nombre
	 * @param valor 
	 */ 
	public Object set(String nombre,Object valor){
		return this.put(nombre, valor);
	}

	/**
	 * Devuelve el valor de un atributo
	 * @param nombre
	 * @return 
	 */
	public String getString(String nombre){
		String val=null;

		if(this.containsKey(nombre)){
			try {
				val = String.valueOf( this.get(nombre));
			} catch (Exception exc) {
				return null;
			}
		}

		return val;
	}
	/**
	 * Devuelve el valor de un atributo
	 * @param nombre
	 * @return 
	 */
	public Float getFloat(String nombre){
		Float val=null;

		if(this.containsKey(nombre)){
			try {
				val =Float.valueOf( this.get(nombre).toString());
			} catch (Exception exc) {
				return null;
			}
		}

		return val;
	}
	/**
	 * Devuelve el valor de un atributo
	 * @param nombre
	 * @return null si no se encuentra el atributo ó no se puede convertir
	 */
	public Integer getInteger(String nombre){
		Integer val=null;

		if(this.containsKey(nombre)){
			try {
				val = new Integer(this.get(nombre).toString());
			} catch (Exception exc) {
				return null;
			}
		}

		return val;
	}
	/**
	 * Devuelve el valor de un atributo
	 * @param nombre
	 * @return null si no se encuentra el atributo ó no se puede convertir
	 */
	public ArrayList<CeParseAttr> getList(String nombre){
		ArrayList<CeParseAttr> val=null;

		if(this.containsKey(nombre)){
			try {
				val = (ArrayList<CeParseAttr>) this.get(nombre);
			} catch (Exception exc) {
				return null;
			}
		}

		return val;
	}
	//           public ArrayList<MySym> getMySymList(String nombre){
	//		   ArrayList<MySym> val=null;
	//		   
	//		   if(this.attribute.containsKey(nombre)){
	//			   try {
	//				   val = (ArrayList<MySym>) this.attribute.get(nombre);
	//			   } catch (Exception exc) {
	//				   return null;
	//			   }
	//		   }
	//		   
	//		   return val;
	//		}

	/**
	 * Devuelve el valor de un atributo
	 * @param nombre
	 * @return null si no se encuentra el atributo ó no se puede convertir
	 */
	public ArrayList<Integer> getIntegerList(String nombre_l ,String nombre ){
		ArrayList<CeParseAttr> val=null;

		if(this.containsKey(nombre_l)){
			try {
				val = (ArrayList<CeParseAttr>) this.get(nombre_l);
			} catch (Exception exc) {
				return null;
			}
		}
		ArrayList<Integer> val2=new ArrayList<>();
		for(CeParseAttr mc : val){
			Integer v=mc.getInteger(nombre);
			if(v!=null)
				val2.add(v);
			else
				return null;		//si getInteger tira null que returne null
		}

		return val2;
	}
	/**
	 * Devuelve una lista de Flotantes
	 * @param list_nombre nombre de la lista donde se encuentra los MyClass
	 * @param nombre nombre del atributo que contiene los flotatnes en MyClass
	 * @return null si no se encuentra el atributo ó no se puede convertir
	 */
	public ArrayList<Float> getFloatList(String list_nombre ,String nombre ){
		ArrayList<CeParseAttr> val=null;

		if(this.containsKey(list_nombre)){
			try {
				val = (ArrayList<CeParseAttr>) this.get(list_nombre);
			} catch (Exception exc) {
				return null;
			}
		}
		ArrayList<Float> val2=new ArrayList<>();
		for(CeParseAttr mc : val){
			Float v=mc.getFloat(nombre);
			if(v!=null)
				val2.add(v);
			else
				return null;		//si getInteger tira null que returne null
		}

		return val2;
	}
	/**
	 * Devuelve una lista de Flotantes
	 * @param list_nombre nombre de la lista donde se encuentra los MyClass
	 * @param nombre nombre del atributo que contiene los flotatnes en MyClass
	 * @return null si no se encuentra el atributo ó no se puede convertir
	 */
	public ArrayList<String> getStringList(String list_nombre ,String nombre ){
		ArrayList<CeParseAttr> val=null;

		if(this.containsKey(list_nombre)){
			try {
				val = (ArrayList<CeParseAttr>) this.get(list_nombre);
			} catch (Exception exc) {
				return null;
			}
		}
		ArrayList<String> val2=new ArrayList<>();
		for(CeParseAttr mc : val){
			String v=mc.getString(nombre);
			if(v!=null)
				val2.add(v);
			else
				return null;		//si getInteger tira null que returne null
		}

		return val2;
	}
	/**
	 * Devuelve el valor de un atributo
	 * @param nombre
	 * @return 
	 */
//	public Object get(String nombre){
//		Object val=null;
//
//		if(this.containsKey(nombre)){
//			try {
//				val = this.get(nombre);
//			} catch (Exception exc) {
//				return null;
//			}
//		}
//
//		return val;
//	}

	public Boolean getBoolean(String nombre){
		try {
			return Boolean.valueOf(get(nombre).toString());
		} catch (Exception exc) {
			return null;
		}
	}

	public CeParseAttr getMyClass(String nombre){
		try {
			return ((CeParseAttr)get(nombre));
		} catch (Exception exc) {
			return null;
		}
	}

	//           public MySym getMySym(String nombre){
		//               try {
			//                   return ((MySym)getObject(nombre));
			//               } catch (Exception exc) {
				//                   return null;
	//               }
	//           }
	public Symbol getSymbol(String nombre){
		try {
			return ((Symbol)get(nombre));
		} catch (Exception exc) {
			return null;
		}
	}

	public String getHtmlTable(){
		String html_table="<table>";
		html_table	+=	String.format("<tr><td><b>%s</b></td><td><b>%s</b></td></tr>", "Key","Value");
		for(Entry<String, Object> entry: this.entrySet()){
			html_table	+=	String.format("<tr><td>%s</td><td>%s</td></tr>", entry.getKey(),entry.getValue());
		}
		html_table+="</table>";
		return html_table;
	}
	
	
	public String toString(String separator,String ...except){
		
		HashSet<String>	except_set	=	new HashSet<>();
		if(except!=null)
			for (int i = 0; i < except.length; i++) {
				except_set.add(except[i]);
			}
		
		String 	string	=	"";
		boolean	first	=	true;
		for(Entry<String, Object> entry: this.entrySet()){
			String	entry_key	=	entry.getKey();
			
			if(!except_set.contains(entry_key)){
				if(first){
					string	+=	String.format("%s=%s", entry.getKey(),entry.getValue());
					first=false;
				}
				else{
					string	+=	String.format("%s%s=%s",separator, entry.getKey(),entry.getValue());
				}
			}			
		}
		
		return string;
	}
	
}


