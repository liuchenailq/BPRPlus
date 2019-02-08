package main.util;

import java.io.FileWriter;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

import main.exception.BPRPlusException;

/**
 * Json����
 * 
 * @author liucheng
 */
public class JsonUtil {
	
	/**
	 *  ��double����תΪjson����д���ļ�
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
            new BPRPlusException("double����д��json�ļ�����", e);
        }
	}

}
