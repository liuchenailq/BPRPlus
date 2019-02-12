package test.Recommender;

import static org.junit.Assert.*;



import org.junit.Test;

import main.configure.Configuration;
import main.data.model.AbstractDataModel;
import main.data.model.TextDataModel;
import main.recommender.AbstractTopNRecommender;
import main.recommender.BPR_baseline;
import main.recommenderJob.RecommenderJob;
import main.util.KeyValue;

public class BPR_baselineTest {

	/**
	 * 问题可能性：还是出现getTruthList上和 recommender()函数
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr.properties");
		conf.setInt("rec.recommender.topn", 100);
		conf.setInt("rec.iterator.maximum", 200);
		conf.setBoolean("rec.evaluator.realTime", true);
		conf.setBoolean("rec.chart.loss", false);
		conf.setBoolean("rec.chart.evaluate", false);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}
	
	/** 
	 * 测试测试集  
	 * 
	 */
	@Test
	public void testGetTruthList() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr.properties");
		conf.setInt("rec.recommender.topn", 100);
		conf.setInt("rec.iterator.maximum", 1);
		conf.setBoolean("rec.evaluator.realTime", false);
		conf.setBoolean("rec.chart.evaluate", false);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}
	
	/** 
	 * 测试推荐函数
	 * 没问题
	 */
	@Test
	public void testRecommender() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr.properties");
		conf.setInt("rec.recommender.topn", 100);
		conf.setInt("rec.iterator.maximum", 1);
		conf.setBoolean("rec.evaluator.realTime", false);
		conf.setBoolean("rec.chart.evaluate", false);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}
	
	/** 
	 * 测试推荐函数
	 * 没问题
	 */
	@Test
	public void testEvaluator() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr.properties");
		conf.setInt("rec.recommender.topn", 100);
		conf.setInt("rec.iterator.maximum", 1);
		conf.setBoolean("rec.evaluator.realTime", false);
		conf.setBoolean("rec.chart.evaluate", false);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}
	
	
	
	
	
}
