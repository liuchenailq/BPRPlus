package main.recommenderJob;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import main.configure.Configuration;
import main.data.model.AbstractDataModel;
import main.data_structure.RecommendList;
import main.recommender.AbstractTopNRecommender;
import main.recommenderEvaluator.AbstractRecommenderEvaluator;
import main.util.DriverClassUtil;
import main.util.ReflectionUtil;

/**
 * �Ƽ�����
 * 
 * @author liucheng
 */
public class RecommenderJob {
	protected final Log LOG = LogFactory.getLog(this.getClass());
	
	private Configuration conf;
	private AbstractDataModel dataModel;
	private AbstractTopNRecommender recommender;
	/** �������۽�� */
	private Map<String, Double> evaluateResults;
	
	
	public RecommenderJob(Configuration conf) {
		this.conf = conf;
	}
	
	/** ִ���Ƽ����� 
	 * @throws ClassNotFoundException */
	public void runJob() throws ClassNotFoundException {
		//��һ��������dataModel,
		//������������������dataConvert��dataAppender��dataSplitter����ȡ��ת������������
		generateDataModel();
		
		//�ڶ���������recommender
		//������������ģ��ѵ�����Ƽ�
		generateRecommender();
		
		//������������evaluators
		//����������ģ������
		generateEvaluators();
		
		
	}

	

	/**
	 * ����"data.model.class"�ֶε�ֵ������Ӧ��dataModelʵ��
	 * ������buildDataModel()����������ݵĶ�ȡ��ת�������֡�
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void generateDataModel() throws ClassNotFoundException {
		if(dataModel == null) {
			dataModel = ReflectionUtil.newInstance((Class<AbstractDataModel>)DriverClassUtil.getClass(conf.get("data.model.class")));
		}
		dataModel.setConf(conf);
		dataModel.buildDataModel();
	}
	
	/**
	 * ����"rec.recommender.class"�ֶε�ֵ������Ӧ��recommenderʵ��
	 * ������buildRecommender()�������ģ��ѵ��
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void generateRecommender() throws ClassNotFoundException {
		if(recommender == null) {
			recommender = ReflectionUtil.newInstance((Class<AbstractTopNRecommender>)DriverClassUtil.getClass(conf.get("rec.recommender.class")));
		}
		recommender.buildRecommender(dataModel);
	}
	
	/**
	 * ����rec.evaluator.classes����������
	 * ������evaluator(recommender, isRealTime)����ģ��
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void generateEvaluators() throws ClassNotFoundException {
		//�����ʵʱ����ģ�����ۣ���������һ��ģ������
		if(conf.getBoolean("rec.evaluator.realTime", false) == false ) {
			LOG.info("��ʼ����ģ������");
			String[] evaluators = conf.getStrings("rec.evaluator.classes");
			evaluateResults = new HashMap<String, Double>();
			RecommendList recommendList = recommender.recommender();
			for(String eval : evaluators) {
				//����eval����AbstactRecommenderEvaluatorʵ��
				AbstractRecommenderEvaluator evaluator = ReflectionUtil.newInstance((Class<AbstractRecommenderEvaluator>)DriverClassUtil.getClass(eval));
				evaluator.setConf(conf);
				double value = evaluator.evaluate(recommender.getTruthList(), recommendList);
				evaluateResults.put(eval, value);
				LOG.info(evaluator.getClass().getSimpleName() + ": " + value);
			}
			LOG.info("ģ�����۳ɹ���");
		}
	}

	public AbstractDataModel getDataModel() {
		return dataModel;
	}

}
