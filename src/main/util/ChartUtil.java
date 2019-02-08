package main.util;

/**
 * ��ͼ������
 * 
 * @author liucheng
 *
 */
public class ChartUtil {
	
	/**
	 * ��������ͼ
	 * @param title ����
	 * @param xlabel x���ǩ
	 * @param ylabel y���ǩ
	 * @param array  ����
	 */
	public static void drawLineChart(String title, String xlabel, String ylabel, double[] array){
		//��һ����arrayתΪjson��ʽ
		JsonUtil.doubleArray2Json(array, "resources/temp/lineChart.json");
		
		
		//�ڶ���������python�ű�
		String jsonFile = System.getProperty("user.dir") + "\\" + "resources/temp/lineChart.json";
		CallPythonUtil.execPython("resources/python/LineChart.py", new String[]{jsonFile, title, xlabel, ylabel});
		
	}
	
	

}
