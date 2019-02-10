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
 * 推荐任务
 * 
 * @author liucheng
 */
public class RecommenderJob {
	protected final Log LOG = LogFactory.getLog(this.getClass());
	
	private Configuration conf;
	private AbstractDataModel dataModel;
	private AbstractTopNRecommender recommender;
	/** 最后的评价结果 */
	private Map<String, Double> evaluateResults;
	
	
	public RecommenderJob(Configuration conf) {
		this.conf = conf;
	}
	
	/** 执行推荐任务 
	 * @throws ClassNotFoundException */
	public void runJob() throws ClassNotFoundException {
		//第一步：生成dataModel,
		//并利用它的三驾马车（dataConvert、dataAppender、dataSplitter）读取、转化、划分数据
		generateDataModel();
		
		//第二步：生成recommender
		//并利用它进行模型训练、推荐
		generateRecommender();
		
		//第三步：生成evaluators
		//利用它进行模型评价
		generateEvaluators();
		
		
	}

	

	/**
	 * 根据"data.model.class"字段的值生成相应的dataModel实例
	 * 并调用buildDataModel()方法完成数据的读取、转化、划分。
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
	 * 根据"rec.recommender.class"字段的值生成相应的recommender实例
	 * 并调用buildRecommender()方法完成模型训练
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
	 * 根据rec.evaluator.classes生成评价器
	 * 并调用evaluator(recommender, isRealTime)评价模型
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void generateEvaluators() throws ClassNotFoundException {
		//如果不实时进行模型评价，则最后进行一次模型评价
		if(conf.getBoolean("rec.evaluator.realTime", false) == false ) {
			LOG.info("开始进行模型评价");
			String[] evaluators = conf.getStrings("rec.evaluator.classes");
			evaluateResults = new HashMap<String, Double>();
			RecommendList recommendList = recommender.recommender();
			for(String eval : evaluators) {
				//根据eval生成AbstactRecommenderEvaluator实例
				AbstractRecommenderEvaluator evaluator = ReflectionUtil.newInstance((Class<AbstractRecommenderEvaluator>)DriverClassUtil.getClass(eval));
				evaluator.setConf(conf);
				double value = evaluator.evaluate(recommender.getTruthList(), recommendList);
				evaluateResults.put(eval, value);
				LOG.info(evaluator.getClass().getSimpleName() + ": " + value);
			}
			LOG.info("模型评价成功！");
		}
	}

	public AbstractDataModel getDataModel() {
		return dataModel;
	}

}
