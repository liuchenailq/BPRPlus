package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import main.util.ChartUtil;

public class ChartUtilTest {

	@Test
	public void testDrawLoss() {
		ChartUtil.drawLineChart("test", "test", "test", new double[]{1d, 2d, 10d});
	}

}
