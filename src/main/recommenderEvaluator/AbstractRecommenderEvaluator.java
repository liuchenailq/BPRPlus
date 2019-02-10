package main.recommenderEvaluator;

import java.util.List;
import main.configure.Configuration;
import main.configure.Configured;
import main.data_structure.RecommendList;
import main.recommender.AbstractTopNRecommender;
import main.util.KeyValue;

/**
 * Ä£ÐÍÆÀ¼Û
 * 
 * @author liucheng
 */

public abstract class AbstractRecommenderEvaluator extends Configured{
	
	public abstract double evaluate(RecommendList truthList, RecommendList recommendList);
	
}
