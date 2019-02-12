package main.util;

/** 
 * ��ѧ���㹤����
 * @author liucheng
 *
 */

public class MathUtil {
	
	/**
	 * log����
	 * @param n 
	 * @param base ���� 
	 * @return
	 */
	public static double log(double n, int base) {
        return Math.log(n) / Math.log(base);
    }
	
	public static double logistic(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

}
