package compiler;

import compiler.logc.C_Sym;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CeSymTable {
    
    public class Item{
        String  nombre;
        String  type;
        String  rol;
        String  context;
        int     pointer;
        Set<Item>   parameters;


        public Item(String nombre, String type, String rol, String context, int pointer, Set<Item> parameters) {
            this.nombre = nombre;
            this.type = type;
            this.rol = rol;
            this.context = context;
            this.pointer = pointer;
            this.parameters = parameters;
        }
        
        
        
    }
    
    private Map<String, CeSymTable.Item> map;

    public CeSymTable() {
        map=new HashMap<>();
    }

    public CeSymTable(Map<String, Item> map) {
        this.map = map;
    }
    
    
    
    public Item add(String name,String type,String rol,String context,int pointer,Set<Item> parameters) throws Exception{
        Item item = new Item(name, type, rol, context, pointer, parameters);
        String key = getKey(item);
        
        if(map.containsKey(key)){
            throw new Exception(String.format("Ya existe el objeto '%s' con rol '%s' y contexto '%s'...", name,rol,context));
        }
        
        map.put(key, item);
        
        return item;
    }
    
    public Item addGlobalVar(String name,String type,String context,int pointer) throws Exception{
        return add(name, type, ROL_VAR_GLOBAL, context, pointer, null);
    }
    
    public Item addLocalVar(String name,String type,String context,int pointer) throws Exception{
        return add(name, type, ROL_VAR_LOCAL, context, pointer, null);
    }
     public Item addParameter(String name,String type,String context,int pointer) throws Exception{
        return add(name, type, ROL_PARAMETER, context, pointer, null);
    }
    
    public Item addFunction(String name,String type,String context,int pointer,Set<Item> parameters) throws Exception{
        return add(name, type, ROL_FUNCTION, context, pointer, parameters);
    }
    
    
    public String getKey(Item i){
        return String.format("%s/%s/%s",i.rol,i.context,i.nombre);
    }
    
    
    public static final String  ROL_VAR_LOCAL   =   "var_local";
    public static final String  ROL_VAR_GLOBAL  =   "var_global";
    public static final String  ROL_FUNCTION    =   "function";
    public static final String  ROL_PARAMETER   =   "parameter";
    
    public static final String  TYPE_VOID       =   "void";
    public static final String  TYPE_INT        =   "int";
    public static final String  TYPE_DOUBLE     =   "double";
    public static final String  TYPE_STRING     =   "string";
    public static final String  TYPE_CHAR       =   "char";
    public static final String  TYPE_BOOLEAN    =   "boolean";
    
}
