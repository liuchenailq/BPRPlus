package main.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import main.exception.BPRPlusException;

/**
 * 调用python工具类
 * 
 * @author liucheng
 */
public class CallPythonUtil {
	
	/**
	 * 调用python
	 * @param pythonFile：
	 * @param args：
	 */
	public static void execPython(String pythonFile, String[] args) {
		String python = System.getProperty("user.dir") + "/" + pythonFile;
		String[] p = new String[args.length + 2];
		p[0] = "python";
		p[1] = python;
		for(int i =2; i<p.length; i++) {
			p[i] = args[i-2];
		}
		try {
			Process pr = Runtime.getRuntime().exec(p);
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line; 
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			} 
			in.close();
			pr.waitFor(); 
		} catch (Exception e) {
			new BPRPlusException("调用python出错！", e);
		}
		
	}

}
