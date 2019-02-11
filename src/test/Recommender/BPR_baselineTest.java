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

	@Test
	public void test() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr.properties");
		conf.setInt("rec.iterator.maximum", 10);
		conf.setBoolean("rec.evaluator.realTime", true);
		conf.setBoolean("rec.chart.evaluate", true);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}
	
}
