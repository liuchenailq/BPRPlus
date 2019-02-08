package main.recommenderEvaluator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import main.recommender.AbstractTopNRecommender;
import main.util.KeyValue;

/**
 * 模型评价
 * 
 * @author liucheng
 */

public abstract class AbstractRecommenderEvaluator {
	
	/**
	 * 模型评价入口函数
	 * @param recommender
	 * @param isRealTime 
	 * @return
	 */
	public double evaluate(AbstractTopNRecommender recommender, boolean isRealTime) {
		List<List<KeyValue<Integer, Double>>> truthList = recommender.getTruthList();
		List<List<KeyValue<Integer, Double>>> recommenderList = recommender.getRecommenderList(isRealTime);
		return evaluate(truthList, recommenderList);
	}
	
	/**
	 * 模型评价
	 * @param truthList 真实交互物品集
	 * @param recommenderList 推荐物品集
	 * @return 
	 */
	public abstract double evaluate(List<List<KeyValue<Integer, Double>>> truthList, List<List<KeyValue<Integer, Double>>> recommenderList) ;

}
