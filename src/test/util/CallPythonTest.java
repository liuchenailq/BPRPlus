package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import main.util.CallPythonUtil;

public class CallPythonTest {

	@Test
	public void testExecPython() {
		String[] args = {"D:\\JavaWork\\BPRPlus\\resources\\temp\\doubleArray2Json.json"};
		CallPythonUtil.execPython("D:\\JavaWork\\Chart\\LossLineChart.py", args);
	}

}
