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
	 * ��������ԣ����ǳ���getTruthList�Ϻ� recommender()����
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
	 * ���Բ��Լ�  
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
	 * �����Ƽ�����
	 * û����
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
	 * �����Ƽ�����
	 * û����
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
