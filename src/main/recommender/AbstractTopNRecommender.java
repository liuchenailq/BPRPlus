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
 * TopN推荐
 * 
 * @author liucheng
 */
public abstract class AbstractTopNRecommender {
	protected final Log LOG = LogFactory.getLog(this.getClass());
	
	public Configuration conf;
	
	/** 记录每次迭代之后的loss */
	public double[] losses;
	/** 推荐数目 */
	public int TopN;
	/** 当前损失 */
	public double loss;
	/** 上一次迭代损失 */
	public double lastLoss;
	/** 用户映射 */
	public BiMap<String, Integer> userMapping;
	/** 物品映射 */
	public BiMap<String, Integer> itemMapping;
	/** 训练矩阵 */
	public SparseMatrix trainMatrix;
	/** 测试矩阵 */
	public SparseMatrix testMatrix;
	/** 辅助数据矩阵 */
	public Map<String, SparseMatrix> auxiliaryMatrixs;
	/** 用户数 */
	public int userCount;
	/** 物品数 */
	public int itemCount;
	
	/** 是否在每次迭代后输出loss */
	public boolean showLoss;
	/** 是否绘制train loss 折线图 */
	public boolean drawLoss; 
	
	public AbstractDataModel dataModel;
	
	/** 如果为true,则在每次迭代之后都要进行模型评价 */
	public boolean evaluateIsRealTime;
	/** 记录每次迭代后的评价结果 */
	public Map<String, List<Double>> evaluateResults;
	/** 是否绘制评价折线图 */
	public boolean drawEvaluate;
	/** 真实测试集 */
	public RecommendList truthList;
	
	/**
	 * 建立推荐：推荐模块的入口函数
	 * @throws ClassNotFoundException 
	 */
	public void buildRecommender(AbstractDataModel dataModel) throws ClassNotFoundException {
		this.dataModel = dataModel;
		
		//第一步：所有参数初始化
		setup();
		LOG.info("模型参数初始化成功！");
		
		//第二步：模型训练
		trainModel();
		LOG.info("模型训练成功！");
		
		//第三步：结果分析
		
		//train loss折线图
		if(drawLoss) {
			ChartUtil.drawLineChart("train loss", "iter", "loss", losses);
			LOG.info("train loss 绘制成功！");
		}
		
		//评价指标折线图
		if(evaluateIsRealTime && drawEvaluate) {
			for(String key : evaluateResults.keySet()) {
				List<Double> list = evaluateResults.get(key);
				double[] array = new double[list.size()];
				for(int i=0; i< list.size(); i++) {
					array[i] = list.get(i);
				}
				ChartUtil.drawLineChart(key, "iter", key, array);
			}
			LOG.info("评价指标折线图绘制成功！");
		}
	}

	/**
	 * 参数初始化
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
		//初始化evaluateResults
		if(evaluateIsRealTime) {
			String[] evaluators = conf.getStrings("rec.evaluator.classes");
			evaluateResults = new HashMap<String, List<Double>>();
			for(String eval : evaluators) {
				evaluateResults.put(eval, new ArrayList<Double>());
			}
		}
	}
	
	/**
	 * 模型训练，由具体的子类执行
	 */
	public abstract void trainModel() throws ClassNotFoundException;
	
	public abstract double predict(int userId, int itemId);
	
	/**
	 * 为所有用户推荐TopN个物品
	 */
	public RecommendList recommender() {
		List<List<KeyValue<Integer, Double>>> recommendedList = new ArrayList<>(userCount);
		List<Integer> list = new ArrayList<Integer>(userCount);
		for(int u = 0; u < userCount; u++) {
			recommendedList.add(new ArrayList<KeyValue<Integer, Double>>());
			list.add(u);
		}
		
		//并行推荐
		list.parallelStream().forEach((Integer userId) -> {
			List<KeyValue<Integer, Double>> itemValueList = new ArrayList<KeyValue<Integer, Double>>(itemCount);
			//用户u在训练集中的物品集
			Set<Integer> items = trainMatrix.getViewRow(userId).getIndexList();
			for(int itemId = 0; itemId < itemCount; itemId ++) {
				if(items.contains(itemId)) {
					continue;
				}
				double p = predict(userId, itemId);
				itemValueList.add(new KeyValue<Integer, Double>(itemId, p));
			}
			//取前TopN项
			recommendedList.set(userId, ListUtil.sortKeyValueListTopK(itemValueList, true, TopN));
		});
		return new RecommendList(recommendedList);
	}
	
	/**
	 * 真实测试集
	 * @return
	 */
	public RecommendList getTruthList(){
		if(truthList == null) {
			//将testMatrix转化ArrayList<ArrayList<KeyValue<Integer, Double>>>
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
	 * 将此函数加到具体的trainModel()中,位于迭代的循环内
	 * 如果isRealTime为true，则每次迭代后进行模型评价，
	 * @param isRealTime
	 * @param iter 本次迭代的次数
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void modelEvaluate(boolean isRealTime, int iter) throws ClassNotFoundException {
		if(isRealTime) {
			LOG.info("第" + iter + "次迭代后的模型评价信息如下：");
			String[] evaluators = conf.getStrings("rec.evaluator.classes");
			RecommendList recommendList = this.recommender();
			for(String eval : evaluators) {
				//根据eval生成AbstactRecommenderEvaluator实例
				AbstractRecommenderEvaluator evaluator = ReflectionUtil.newInstance((Class<AbstractRecommenderEvaluator>)DriverClassUtil.getClass(eval));
				evaluator.setConf(conf);
				double value = evaluator.evaluate(this.getTruthList(), recommendList);
				evaluateResults.get(eval).add(value);
				LOG.info(evaluator.getClass().getSimpleName() + ": " + value);
			}
		}	
	}

}
