package main.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.BiMap;
import main.configure.Configuration;
import main.data.model.AbstractDataModel;
import main.data_structure.RecommendList;
import main.data_structure.SparseMatrix;
import main.recommenderEvaluator.AbstractRecommenderEvaluator;
import main.util.ChartUtil;
import main.util.DriverClassUtil;
import main.util.KeyValue;
import main.util.ListUtil;
import main.util.ReflectionUtil;

/**
 * TopN�Ƽ�
 * 
 * @author liucheng
 */
public abstract class AbstractTopNRecommender {
	protected final Log LOG = LogFactory.getLog(this.getClass());
	
	public Configuration conf;
	
	/** ��¼ÿ�ε���֮���loss */
	public double[] losses;
	/** �Ƽ���Ŀ */
	public int TopN;
	/** ��ǰ��ʧ */
	public double loss;
	/** ��һ�ε�����ʧ */
	public double lastLoss;
	/** �û�ӳ�� */
	public BiMap<String, Integer> userMapping;
	/** ��Ʒӳ�� */
	public BiMap<String, Integer> itemMapping;
	/** ѵ������ */
	public SparseMatrix trainMatrix;
	/** ���Ծ��� */
	public SparseMatrix testMatrix;
	/** �������ݾ��� */
	public Map<String, SparseMatrix> auxiliaryMatrixs;
	/** �û��� */
	public int userCount;
	/** ��Ʒ�� */
	public int itemCount;
	
	/** �Ƿ���ÿ�ε��������loss */
	public boolean showLoss;
	/** �Ƿ����train loss ����ͼ */
	public boolean drawLoss; 
	
	public AbstractDataModel dataModel;
	
	/** ���Ϊtrue,����ÿ�ε���֮��Ҫ����ģ������ */
	public boolean evaluateIsRealTime;
	/** ��¼ÿ�ε���������۽�� */
	public Map<String, List<Double>> evaluateResults;
	/** �Ƿ������������ͼ */
	public boolean drawEvaluate;
	/** ��ʵ���Լ� */
	public RecommendList truthList;
	
	/**
	 * �����Ƽ����Ƽ�ģ�����ں���
	 * @throws ClassNotFoundException 
	 */
	public void buildRecommender(AbstractDataModel dataModel) throws ClassNotFoundException {
		this.dataModel = dataModel;
		
		//��һ�������в�����ʼ��
		setup();
		LOG.info("ģ�Ͳ�����ʼ���ɹ���");
		
		//�ڶ�����ģ��ѵ��
		trainModel();
		LOG.info("ģ��ѵ���ɹ���");
		
		//���������������
		
		//train loss����ͼ
		if(drawLoss) {
			ChartUtil.drawLineChart("train loss", "iter", "loss", losses);
			LOG.info("train loss ���Ƴɹ���");
		}
		
		//����ָ������ͼ
		if(evaluateIsRealTime && drawEvaluate) {
			for(String key : evaluateResults.keySet()) {
				List<Double> list = evaluateResults.get(key);
				double[] array = new double[list.size()];
				for(int i=0; i< list.size(); i++) {
					array[i] = list.get(i);
				}
				ChartUtil.drawLineChart(key, "iter", key, array);
			}
			LOG.info("����ָ������ͼ���Ƴɹ���");
		}
	}

	/**
	 * ������ʼ��
	 */
	public void setup() {
		conf = dataModel.getConf();
		TopN = conf.getInt("rec.recommender.topn", 10);
		showLoss = conf.getBoolean("rec.iterator.showLoss", false);
		loss = 0;
		lastLoss = 0;
		userMapping = dataModel.getUserMapping();
		itemMapping = dataModel.getItemMapping();
		trainMatrix = dataModel.getTrainMatrix();
		testMatrix = dataModel.getTestMatrix();
		auxiliaryMatrixs = dataModel.getAuxiliaryMatrixs();
		userCount = dataModel.getUserCount();
		itemCount = dataModel.getItemCount();
		drawLoss = conf.getBoolean("rec.chart.loss", false);
		evaluateIsRealTime = conf.getBoolean("rec.evaluator.realTime", false);
		drawEvaluate = conf.getBoolean("rec.chart.evaluate", false);
		//��ʼ��evaluateResults
		if(evaluateIsRealTime) {
			String[] evaluators = conf.getStrings("rec.evaluator.classes");
			evaluateResults = new HashMap<String, List<Double>>();
			for(String eval : evaluators) {
				evaluateResults.put(eval, new ArrayList<Double>());
			}
		}
	}
	
	/**
	 * ģ��ѵ�����ɾ��������ִ��
	 */
	public abstract void trainModel() throws ClassNotFoundException;
	
	public abstract double predict(int userId, int itemId);
	
	/**
	 * Ϊ�����û��Ƽ�TopN����Ʒ
	 */
	public RecommendList recommender() {
		List<List<KeyValue<Integer, Double>>> recommendedList = new ArrayList<>(userCount);
		List<Integer> list = new ArrayList<Integer>(userCount);
		for(int u = 0; u < userCount; u++) {
			recommendedList.add(new ArrayList<KeyValue<Integer, Double>>());
			list.add(u);
		}
		
		//�����Ƽ�
		list.parallelStream().forEach((Integer userId) -> {
			List<KeyValue<Integer, Double>> itemValueList = new ArrayList<KeyValue<Integer, Double>>(itemCount);
			//�û�u��ѵ�����е���Ʒ��
			Set<Integer> items = trainMatrix.getViewRow(userId).getIndexList();
			for(int itemId = 0; itemId < itemCount; itemId ++) {
				if(items.contains(itemId)) {
					continue;
				}
				double p = predict(userId, itemId);
				itemValueList.add(new KeyValue<Integer, Double>(itemId, p));
			}
			//ȡǰTopN��
			recommendedList.set(userId, ListUtil.sortKeyValueListTopK(itemValueList, true, TopN));
		});
		return new RecommendList(recommendedList);
	}
	
	/**
	 * ��ʵ���Լ�
	 * @return
	 */
	public RecommendList getTruthList(){
		if(truthList == null) {
			//��testMatrixת��ArrayList<ArrayList<KeyValue<Integer, Double>>>
			List<List<KeyValue<Integer, Double>>> tempList = new ArrayList<>(userCount);
			List<Integer> list = new ArrayList<Integer>(userCount);
			for(int u = 0; u< userCount; u++) {
				tempList.add(new ArrayList<KeyValue<Integer, Double>>());
				list.add(u);
			}
			
			list.parallelStream().forEach((Integer u) -> {
				List<KeyValue<Integer, Double>> items = new ArrayList<KeyValue<Integer, Double>>();
				for(Entry<Integer, Double> entry: testMatrix.getViewRow(u)) {
					items.add(new KeyValue<Integer, Double>(entry.getKey(), entry.getValue()));
				}
				tempList.set(u, items);
			});
			
			truthList = new RecommendList(tempList);
		}
		return truthList;
	}
	
	/**
	 * ���˺����ӵ������trainModel()��,λ�ڵ�����ѭ����
	 * ���isRealTimeΪtrue����ÿ�ε��������ģ�����ۣ�
	 * @param isRealTime
	 * @param iter ���ε����Ĵ���
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void modelEvaluate(boolean isRealTime, int iter) throws ClassNotFoundException {
		if(isRealTime) {
			LOG.info("��" + iter + "�ε������ģ��������Ϣ���£�");
			String[] evaluators = conf.getStrings("rec.evaluator.classes");
			RecommendList recommendList = this.recommender();
			for(String eval : evaluators) {
				//����eval����AbstactRecommenderEvaluatorʵ��
				AbstractRecommenderEvaluator evaluator = ReflectionUtil.newInstance((Class<AbstractRecommenderEvaluator>)DriverClassUtil.getClass(eval));
				evaluator.setConf(conf);
				double value = evaluator.evaluate(this.getTruthList(), recommendList);
				evaluateResults.get(eval).add(value);
				LOG.info(evaluator.getClass().getSimpleName() + ": " + value);
			}
		}	
	}

}
