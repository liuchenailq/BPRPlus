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
	
	/** �����ݶ�ȡ�� */
	public AbstractDataConvert dataConvert;
	
	/** �������ݶ�ȡ�� */
	public Map<String, AbstractDataAppender> dataAppenders;
	
	/** ���������� */
	public AbstractDataSplitter dataSplitter;
	
	/** ѵ������ */
	public SparseMatrix trainMatrix;
	/** ���Ծ��� */
	public SparseMatrix testMatrix;
	
	/** �������ݾ��� */
	public Map<String, SparseMatrix> auxiliaryMatrixs;
	
	public void buildDataModel() throws ClassNotFoundException {
		//��һ������ȡ����
		if(!conf.getBoolean("data.convert.read.ready")) {
			buildDataConvert();
			conf.setBoolean("data.convert.read.ready", true);
		}
		
		//�ڶ�������ȡ��������
		if(StringUtils.isNotBlank(conf.get("data.appender.keys")) && !conf.getBoolean("data.appender.read.ready")) {
			buildDataAppenders();
            conf.setBoolean("data.appender.read.ready", true);
		}
		
		//���������������ݼ�
		if(!conf.getBoolean("data.splitter.read.ready")) {
			buildSplitter();
			conf.setBoolean("data.splitter.read.ready", true);
		}
		
		
	}
	
	/** ����DataAppender����������ȡ�������� */
	public void buildDataAppenders() {
		String[] keys = conf.getStrings("data.appender.keys");
		String[] classes = conf.getStrings("data.appender.classes");
		String[] dataColumnFormats = conf.getStrings("data.appender.column.formats");
		String[] seps = conf.getStrings("data.appender.convert.seps");
		String[] filepaths = conf.getStrings("data.appender.input.filepaths");
		double[] binTholds = conf.getDoubles("data.appender.binarize.thresholds");
		int count = keys.length;
		if(count != classes.length || count != dataColumnFormats.length || count != seps.length || count != filepaths.length || count != binTholds.length) {
			LOG.error("dataAppender���ô���");
			System.exit(1);
		}
		dataAppenders = new HashMap<String, AbstractDataAppender>();
		for(int i = 0; i<count; i++) {
			//δ�����Ƿ������
			AbstractDataAppender dataAppender = new TextDataAppender(dataColumnFormats[i], seps[i], filepaths[i], binTholds[i]);
			dataAppender.setUserMapping(getUserMapping());
			dataAppender.setItemMapping(getItemMapping());
			//Ϊ�������ݳ�ʼ������
			for(int index = 0; index< getUserMapping().size(); index++) {
				dataAppender.dataFrame.getData().add(new ArrayList<Rating>());
			}
			dataAppender.processData();
			dataAppenders.put(keys[i], dataAppender);
		}
	}
	
	/** �ɾ����������ɣ����ɶ�Ӧ��dataConvert,����������ȡ��ת������ */
	public abstract void buildDataConvert();
	
	/**
	 * ����������������������
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void buildSplitter() throws ClassNotFoundException {
		String splitter = conf.get("data.splitter.class");
		//�����÷���
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
	 * ����ָ���ĸ������ݾ���
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
