package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import main.util.JsonUtil;

public class JsonUtilTest {

	@Test
	public void testDoubleArray2Json() {
		double[] array = {1d, 2d, 3d, 4.5d};
		String file = "resources/temp/doubleArray2Json.json";
		JsonUtil.doubleArray2Json(array, file);
	}

}
