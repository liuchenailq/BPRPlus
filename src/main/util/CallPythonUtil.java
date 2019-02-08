package main.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import main.exception.BPRPlusException;

/**
 * ����python������
 * 
 * @author liucheng
 */
public class CallPythonUtil {
	
	/**
	 * ����python
	 * @param pythonFile��
	 * @param args��
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
			new BPRPlusException("����python����", e);
		}
		
	}

}
