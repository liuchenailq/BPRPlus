package main.data.convert;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.BiMap;
import main.data_structure.DataFrame;

public abstract class AbstractDataConvert {
	protected final Log LOG = LogFactory.getLog(this.getClass());

	public DataFrame dataFrame;
	
	public BiMap<String, Integer> getUserMapping() {
		return dataFrame.getUserMapping();
	}
	
	public BiMap<String, Integer> getItemMapping() {
		return dataFrame.getItemMapping();
	}
	
	/**
	 * 返回用户数
	 */
	public int getUserCount() {
		return getUserMapping().size();
	}
	
	/**
	 * 返回物品数
	 */
	public int getItemCount() {
		return getItemMapping().size();
	}
	
	/** 读取并转换数据 **/
	public abstract void processData();

}
