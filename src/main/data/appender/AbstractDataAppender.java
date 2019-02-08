package main.data.appender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.BiMap;

import main.data_structure.DataFrame;

public abstract class AbstractDataAppender {
	protected final Log LOG = LogFactory.getLog(this.getClass());

	public DataFrame dataFrame;
	
	public void setUserMapping(BiMap<String, Integer> userMapping) {
		dataFrame.setUserMapping(userMapping);
	}
	
	public void setItemMapping(BiMap<String, Integer> itemMapping) {
		dataFrame.setItemMapping(itemMapping);
	}
	
	public abstract void processData();

}
