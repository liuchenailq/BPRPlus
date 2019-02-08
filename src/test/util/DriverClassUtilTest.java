package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import main.util.DriverClassUtil;

public class DriverClassUtilTest {

	@Test
	public void test() throws ClassNotFoundException {
		System.out.println(DriverClassUtil.getClass("TextDataModel").getName());
	}

}
