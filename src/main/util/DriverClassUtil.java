package main.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import java.util.Map.Entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * 维护driver.classes.props
 * 
 * @author liucheng
 */
public class DriverClassUtil {
	/** 
	 * text=main.data.model.TextDataModel
	 *  维护驱动名称到具体类路径的映射
	 */
	private static BiMap<String, String> driverClassBiMap;
	
	static {
		driverClassBiMap = HashBiMap.create();
		//加载driver.classes.props中的属性键值对，存入prop中
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = DriverClassUtil.class.getClassLoader().getResourceAsStream("driver.classes.props");
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将属性键值对装入driverClassBiMap中 
        Iterator<Entry<Object, Object>> propIte = prop.entrySet().iterator();
        while (propIte.hasNext()) {
            Entry<Object, Object> entry = propIte.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            driverClassBiMap.put(key, value);
        }
	}
	
	/**
	 * 通过驱动名返回类对象
	 */
	public static Class<?> getClass(String driverName) throws ClassNotFoundException{
		if(StringUtils.isBlank(driverName)) {
			return null;
		}else {
			if(StringUtils.contains(driverName, ".")) {
				return Class.forName(driverName);
			}else {
				String classFullName = driverClassBiMap.get(driverName.toLowerCase());
				return Class.forName(classFullName);
			}
		}
	}
}
