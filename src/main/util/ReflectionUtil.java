package main.util;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ���乤����
 * 
 * @author liucheng
 */
public class ReflectionUtil {
	private static final Class<?>[] EMPTY_ARRAY = new Class[]{};
	
	/** �������޲ι��캯��ӳ�� */ 
    private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<Class<?>, Constructor<?>>();

    /** 
     * ���ͷ��� <T>
     * ͨ������󴴽�ʵ��
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> theClass) {
        T result;
        try {
        	//����ͼ�ӻ�������ȡ���������Ĺ��캯��
            Constructor<T> meth = (Constructor<T>) CONSTRUCTOR_CACHE.get(theClass);
            if (meth == null) {
            	//�޲ι��캯��
                meth = theClass.getDeclaredConstructor(EMPTY_ARRAY);
                meth.setAccessible(true);
                CONSTRUCTOR_CACHE.put(theClass, meth);
            }
            //�����޲ι��캯�����ɶ���
            result = meth.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
    
    

}
