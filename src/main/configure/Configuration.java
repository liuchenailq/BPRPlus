package main.configure;

import java.net.URL;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import com.google.common.base.Joiner;

import main.exception.BPRPlusException;

/**
 *  该类提供对配置的访问
 *  配置分为数据配置和算法配置
 * 
 * @author liucheng
 */
public class Configuration {
	/** 配置 **/
	private Properties properties;
	/** 类加载器 */
	private ClassLoader classLoader;  
	{
        classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Configuration.class.getClassLoader();
        }
    }
	    
	
	public Configuration() {
		properties = new Properties();
		addDefaultResource();
	}
	
	/** 加载默认的数据配置**/
	private void addDefaultResource() {
		URL url = classLoader.getResource("data.properties");
		try {
			properties.load(url.openStream());
		} catch (Exception exception) {
			throw new BPRPlusException("无法获取数据配置", exception);
		}
	}
		
	/** 从配置文件中加载配置,一般调用此函数加载算法配置*/
	public void addResource(String file) {
		URL url = classLoader.getResource(file);
		try {
			properties.load(url.openStream());
		} catch (Exception exception) {
			throw new BPRPlusException("无法获取" + file, exception);
		}
	}
	
	public String get(String name) {
        return properties.getProperty(name);
    }
	
	public String get(String name, String defaultValue) {
        String value = get(name);
        return StringUtils.isNotBlank(value) ? value : defaultValue;
    }
	
	public void set(String name, String value) {
		properties.setProperty(name, value);
    }
	
	public void setStrings(String name, String... values) {
		if(values.length != 0) {
			String value = Joiner.on(",").skipNulls().join(values);
			set(name, value);
		}
    }
	
	public String[] getStrings(String name) {
        String values = get(name);
        return values == null ? null : values.split(",");
    }
	
	public void setFloat(String name, float value) {
        set(name, Float.toString(value));
    }
	
	public Float getFloat(String name, Float defaultValue) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Float.valueOf(value);
        } else {
            return defaultValue;
        }
    }
	
	public Float getFloat(String name) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Float.valueOf(value);
        } else {
            return null;
        }
    }
	
	public void setDouble(String name, double value) {
        set(name, Double.toString(value));
    }

    public Double getDouble(String name, Double defaultValue) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Double.valueOf(value);
        } else {
            return defaultValue;
        }
    }
    
    public double[] getDoubles(String name) {
    	String[] str = getStrings(name);
    	double[] d = new double[str.length];
    	for(int i = 0; i<str.length; i++) {
    		d[i] = Double.valueOf(str[i]);
    	}
    	return d;
    }

    public Double getDouble(String name) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Double.valueOf(value);
        } else {
            return null;
        }
    }
    
    public void setLong(String name, long value) {
        set(name, Long.toString(value));
    }

    public Long getLong(String name, Long defaultValue) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Long.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    public Long getLong(String name) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Long.valueOf(value);
        } else {
            return null;
        }
    }
    
    public void setInt(String name, int value) {
        set(name, Integer.toString(value));
    }

    public Integer getInt(String name, Integer defaultValue) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    public Integer getInt(String name) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        } else {
            return null;
        }
    }
    
    public void setBoolean(String name, boolean value) {
        set(name, Boolean.toString(value));
    }

    public boolean getBoolean(String name) {
        String value = get(name);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : false;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String value = get(name);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.valueOf(value);
        } else {
            return defaultValue;
        }
    }

}
