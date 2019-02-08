package main.recommenderEvaluator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import main.recommender.AbstractTopNRecommender;
import main.util.KeyValue;

/**
 * ģ������
 * 
 * @author liucheng
 */

public abstract class AbstractRecommenderEvaluator {
	
	/**
	 * ģ��������ں���
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
	 * ģ������
	 * @param truthList ��ʵ������Ʒ��
	 * @param recommenderList �Ƽ���Ʒ��
	 * @return 
	 */
	public abstract double evaluate(List<List<KeyValue<Integer, Double>>> truthList, List<List<KeyValue<Integer, Double>>> recommenderList) ;

}
