package main.util;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 * 
 * @author liucheng
 */
public class ReflectionUtil {
	private static final Class<?>[] EMPTY_ARRAY = new Class[]{};
	
	/** 类对象的无参构造函数映射 */ 
    private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<Class<?>, Constructor<?>>();

    /** 
     * 泛型方法 <T>
     * 通过类对象创建实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> theClass) {
        T result;
        try {
        	//先试图从缓冲区中取出该类对象的构造函数
            Constructor<T> meth = (Constructor<T>) CONSTRUCTOR_CACHE.get(theClass);
            if (meth == null) {
            	//无参构造函数
                meth = theClass.getDeclaredConstructor(EMPTY_ARRAY);
                meth.setAccessible(true);
                CONSTRUCTOR_CACHE.put(theClass, meth);
            }
            //调用无参构造函数生成对象
            result = meth.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
    
    

}
