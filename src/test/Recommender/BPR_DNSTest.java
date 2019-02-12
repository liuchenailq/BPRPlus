package test.Recommender;

import static org.junit.Assert.*;

import org.junit.Test;

import main.configure.Configuration;
import main.recommenderJob.RecommenderJob;

public class BPR_DNSTest {

	@Test
	public void test() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.addResource("recommender/bpr_dns.properties");
		conf.setInt("rec.recommender.topn", 100);
		conf.setInt("rec.dns.paraK", 100);
		conf.setInt("rec.iterator.maximum", 80);
		conf.setBoolean("rec.evaluator.realTime", false);
		conf.setBoolean("rec.chart.evaluate", false);
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}

}
