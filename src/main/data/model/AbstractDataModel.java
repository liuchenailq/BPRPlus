package main.data.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.BiMap;

import main.configure.Configured;
import main.data.appender.AbstractDataAppender;
import main.data.appender.TextDataAppender;
import main.data.convert.AbstractDataConvert;
import main.data.splitter.AbstractDataSplitter;
import main.data_structure.Rating;
import main.data_structure.SparseMatrix;
import main.util.DriverClassUtil;
import main.util.ReflectionUtil;

public abstract class AbstractDataModel extends Configured{
	protected final Log LOG = LogFactory.getLog(this.getClass());
	
	/** 主数据读取类 */
	public AbstractDataConvert dataConvert;
	
	/** 辅助数据读取类 */
	public Map<String, AbstractDataAppender> dataAppenders;
	
	/** 划分数据类 */
	public AbstractDataSplitter dataSplitter;
	
	/** 训练矩阵 */
	public SparseMatrix trainMatrix;
	/** 测试矩阵 */
	public SparseMatrix testMatrix;
	
	/** 辅助数据矩阵 */
	public Map<String, SparseMatrix> auxiliaryMatrixs;
	
	public void buildDataModel() throws ClassNotFoundException {
		//第一步：读取数据
		if(!conf.getBoolean("data.convert.read.ready")) {
			buildDataConvert();
			conf.setBoolean("data.convert.read.ready", true);
		}
		
		//第二步：读取辅助数据
		if(StringUtils.isNotBlank(conf.get("data.appender.keys")) && !conf.getBoolean("data.appender.read.ready")) {
			buildDataAppenders();
            conf.setBoolean("data.appender.read.ready", true);
		}
		
		//第三步：划分数据集
		if(!conf.getBoolean("data.splitter.read.ready")) {
			buildSplitter();
			conf.setBoolean("data.splitter.read.ready", true);
		}
		
		
	}
	
	/** 创建DataAppender，利用它读取辅助数据 */
	public void buildDataAppenders() {
		String[] keys = conf.getStrings("data.appender.keys");
		String[] classes = conf.getStrings("data.appender.classes");
		String[] dataColumnFormats = conf.getStrings("data.appender.column.formats");
		String[] seps = conf.getStrings("data.appender.convert.seps");
		String[] filepaths = conf.getStrings("data.appender.input.filepaths");
		double[] binTholds = conf.getDoubles("data.appender.binarize.thresholds");
		int count = keys.length;
		if(count != classes.length || count != dataColumnFormats.length || count != seps.length || count != filepaths.length || count != binTholds.length) {
			LOG.error("dataAppender设置错误！");
			System.exit(1);
		}
		dataAppenders = new HashMap<String, AbstractDataAppender>();
		for(int i = 0; i<count; i++) {
			//未来考虑反射机制
			AbstractDataAppender dataAppender = new TextDataAppender(dataColumnFormats[i], seps[i], filepaths[i], binTholds[i]);
			dataAppender.setUserMapping(getUserMapping());
			dataAppender.setItemMapping(getItemMapping());
			//为辅助数据初始化容量
			for(int index = 0; index< getUserMapping().size(); index++) {
				dataAppender.dataFrame.getData().add(new ArrayList<Rating>());
			}
			dataAppender.processData();
			dataAppenders.put(keys[i], dataAppender);
		}
	}
	
	/** 由具体的子类完成，生成对应的dataConvert,并利用它读取、转化数据 */
	public abstract void buildDataConvert();
	
	/**
	 * 创建划分器，并划分数据
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void buildSplitter() throws ClassNotFoundException {
		String splitter = conf.get("data.splitter.class");
		//将来用反射
		dataSplitter = ReflectionUtil.newInstance((Class<AbstractDataSplitter>)DriverClassUtil.getClass(splitter));
		dataSplitter.setDataConvert(dataConvert);
		dataSplitter.splitData();
	}
	
	public BiMap<String, Integer> getUserMapping(){
		return dataConvert.getUserMapping();
	}
	
	public BiMap<String, Integer> getItemMapping(){
		return dataConvert.getItemMapping();
	}
	
	public AbstractDataConvert getDataConvert() {
		return dataConvert;
	}
	
	public AbstractDataAppender getDataAppender(String key) {
		return dataAppenders.get(key);
	}
	
	public SparseMatrix getTrainMatrix() {
		return dataSplitter.getTrainMatrix();
	}
	
	public SparseMatrix getTestMatrix() {
		return dataSplitter.getTestMatrix();
	}
	
	public Map<String, SparseMatrix> getAuxiliaryMatrixs(){
		if(auxiliaryMatrixs == null) {
			auxiliaryMatrixs = new HashMap<String, SparseMatrix>();
			for(String s : dataAppenders.keySet()) {
				auxiliaryMatrixs.put(s, dataAppenders.get(s).dataFrame.toSparseMatrix());
			}
		}
		return auxiliaryMatrixs;
	}
	
	/**
	 * 返回指定的辅助数据矩阵
	 */
	public SparseMatrix getAuxiliaryMatrix(String key) {
		return getAuxiliaryMatrixs().get(key);
	}
	
	public int getUserCount() {
		return dataConvert.getUserCount();
	}
	
	public int getItemCount() {
		return dataConvert.getItemCount();
	}

}
