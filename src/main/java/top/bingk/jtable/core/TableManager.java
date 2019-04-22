package top.bingk.jtable.core;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("rawtypes")
public class TableManager {

	public static final Map<Class<? extends Model>, Table<? extends Model>> TABLES = new HashMap<Class<? extends Model>, Table<? extends Model>>();
	
	private TableManager(){}
	
	@SuppressWarnings("unchecked")
	public static <T extends Model> Table<T> get(Class<T> modelClass){
		if(TABLES.get(modelClass) == null){
			return Table.newInstance(modelClass);
		}
		return (Table<T>) TABLES.get(modelClass);
	}
	
	public static <T extends Model> void set(Class<T> modelClass, Table<T> table){
		TABLES.put(modelClass, table);
	}
	
	public static <T extends Model> boolean hasTable(Class<T> tableClass){
		return TABLES.containsKey(tableClass); 
	}
    
}
