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
	 * �����û���
	 */
	public int getUserCount() {
		return getUserMapping().size();
	}
	
	/**
	 * ������Ʒ��
	 */
	public int getItemCount() {
		return getItemMapping().size();
	}
	
	/** ��ȡ��ת������ **/
	public abstract void processData();

}
