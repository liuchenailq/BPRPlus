package main.recommenderEvaluator;

import java.util.List;
import java.util.Set;

import main.data_structure.RecommendList;
import main.util.KeyValue;

/**
 * Recall∆¿º€÷∏±Í
 * 
 * @author liucheng
 *
 */

public class RecallEvaluator extends AbstractRecommenderEvaluator{

	public RecallEvaluator() {
		
	}
	
	
	@Override
	public double evaluate(RecommendList truthList, RecommendList recommendList) {
		double totalRecall = 0d;
		int sum = 0;
		for(int u = 0; u<truthList.size(); u++) {
			Set<Integer> items = truthList.getKeySet(u);
			if(items.size()>0) {
				List<KeyValue<Integer, Double>> test = recommendList.getList(u);
				int numHits = 0;
				for(KeyValue<Integer, Double> entry : test) {
					if(items.contains(entry.getKey())) {
						numHits ++;
					}
				}
				totalRecall += numHits * 1.0 / items.size();
				sum ++;
			}
		}
		return sum > 0 ? totalRecall / sum : 0;
	}

}
