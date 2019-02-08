package test.Recommender;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		RecommenderJob job = new RecommenderJob(conf);
		job.runJob();
	}
	

}
