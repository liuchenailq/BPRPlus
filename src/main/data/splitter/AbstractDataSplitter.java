package main.data.splitter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import main.data.convert.AbstractDataConvert;
import main.data_structure.SparseMatrix;
/**
 * »®·ÖÆ÷
 *
 * @author liucheng
 */
public abstract class AbstractDataSplitter {
	protected final Log LOG = LogFactory.getLog(this.getClass());
	
	public AbstractDataConvert dataConvert;
	
	public SparseMatrix trainMatrix;
	public SparseMatrix testMatrix;
	
	
	public void setDataConvert(AbstractDataConvert dataConvert) {
		this.dataConvert = dataConvert;
	}
	
	public AbstractDataConvert getDataConvert() {
		return dataConvert;
	}
	
	public SparseMatrix getTrainMatrix() {
		if(trainMatrix == null) {
			splitData();
		}
		return trainMatrix;
	}
	
	public SparseMatrix getTestMatrix() {
		if(testMatrix == null) {
			splitData();
		}
		return testMatrix;
	}
	
	public abstract void splitData();

}
