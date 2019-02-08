package main.util;

/**
 * 绘图工具类
 * 
 * @author liucheng
 *
 */
public class ChartUtil {
	
	/**
	 * 绘制折线图
	 * @param title 标题
	 * @param xlabel x轴标签
	 * @param ylabel y轴标签
	 * @param array  数据
	 */
	public static void drawLineChart(String title, String xlabel, String ylabel, double[] array){
		//第一步：array转为json格式
		JsonUtil.doubleArray2Json(array, "resources/temp/lineChart.json");
		
		
		//第二步：调用python脚本
		String jsonFile = System.getProperty("user.dir") + "\\" + "resources/temp/lineChart.json";
		CallPythonUtil.execPython("resources/python/LineChart.py", new String[]{jsonFile, title, xlabel, ylabel});
		
	}
	
	

}
