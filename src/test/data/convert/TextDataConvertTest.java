package test.data.convert;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data.convert.AbstractDataConvert;
import main.data.convert.TextDataConvert;

public class TextDataConvertTest {

	@Test
	public void testSortDataByTime() {
		String filePath = "D:\\JavaWork\\BPRPlus\\data\\tmall-all\\buy";
		String sep = "\t";
		AbstractDataConvert convert = new TextDataConvert("UIRT", sep, filePath, -1d);
		convert.processData();
		long startTime = System.currentTimeMillis();
		convert.dataFrame.sortDataByTime(false);
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
		for(int u = 0;u<3;u++) {
			System.out.println(convert.dataFrame.getData().get(u));
		}
	}

}
