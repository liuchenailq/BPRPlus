package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import main.configure.Configuration;
import main.data.model.AbstractDataModel;
import main.util.DriverClassUtil;
import main.util.ReflectionUtil;

public class ReflectionUtilTest {

	@Test
	public void testNewInstance() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		AbstractDataModel dataModel =  ReflectionUtil.newInstance((Class<AbstractDataModel>)DriverClassUtil.getClass("textdatamodel"));
		dataModel.setConf(conf);
		dataModel.buildDataModel();
	}

}
