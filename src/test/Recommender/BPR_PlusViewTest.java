package test.Recommender;

import static org.junit.Assert.*;

import org.junit.Test;

import main.configure.Configuration;
import main.recommenderJob.RecommenderJob;

public class BPR_PlusViewTest {

	@Test
	public void test() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr_plusview.properties");
		conf.setInt("rec.recommender.topn", 100);
		conf.setInt("rec.iterator.maximum", 500);
		conf.setBoolean("rec.evaluator.realTime", true);
		conf.setBoolean("rec.chart.loss", false);
		conf.setBoolean("rec.chart.evaluate", false);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}

}
