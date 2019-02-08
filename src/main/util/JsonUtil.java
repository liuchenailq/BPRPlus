package main.util;

import java.io.FileWriter;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

import main.exception.BPRPlusException;

/**
 * Json工具
 * 
 * @author liucheng
 */
public class JsonUtil {
	
	/**
	 *  将double数组转为json，并写入文件
	 */
	public static void doubleArray2Json(double[] array, String file) {
		String filePath = System.getProperty("user.dir") + "/" + file;
		JSONObject object = new JSONObject();
		object.put("doubleArray", array);
		String text = object.toJSONString();
        FileWriter writer;
        try {
            writer = new FileWriter(filePath);
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            new BPRPlusException("double数组写入json文件出错！", e);
        }
	}

}
