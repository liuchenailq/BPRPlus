package main.util;

/** 
 * 数学计算工具类
 * @author liucheng
 *
 */

public class MathUtil {
	
	/**
	 * log函数
	 * @param n 
	 * @param base 基数 
	 * @return
	 */
	public static double log(double n, int base) {
        return Math.log(n) / Math.log(base);
    }
	
	public static double logistic(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

}
