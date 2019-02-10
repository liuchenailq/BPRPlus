package main.recommenderEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.data_structure.RecommendList;
import main.util.KeyValue;
import main.util.ListUtil;
import main.util.MathUtil;

/**
 * NDCG评价指标
 *详情请看 "https://en.wikipedia.org/wiki/Discounted_cumulative_gain" 
 *
 * @author liucheng
 *
 */

public class NDCGEvaluator extends AbstractRecommenderEvaluator{
	
	public NDCGEvaluator() {
		
	}
	
	/**
	 * IDCG
	 */
	public List<Double> getIDCGS(int size){
		List<Double> idcgs = new ArrayList<Double>(size + 1);
		idcgs.add(0d);
		for(int index = 0; index < size; index ++) {
			idcgs.add((double)(1d / MathUtil.log(index + 2, 2)) + idcgs.get(index));
		}
		return idcgs;
	}

	@Override
	public double evaluate(RecommendList truthList, RecommendList recommendList) {
		List<Double> idcgs = getIDCGS(conf.getInt("rec.recommender.topn", 10));
		double ndcg = 0d;
		int sum = 0;
		
		for(int u = 0; u<truthList.size(); u++) {
			Set<Integer> items = truthList.getKeySet(u);
			List<KeyValue<Integer, Double>> test = recommendList.getList(u);
			if(items.size() > 0) {
				double dcg = 0d;
				int index = 0;
				for(KeyValue<Integer, Double> entry : test) {
					if(items.contains(entry.getKey())) {
						dcg += (double)(1 / MathUtil.log(index + 2, 2)); 
					}
					index ++;
				}
				int size = items.size() < test.size() ? items.size() : test.size();
				dcg = dcg / idcgs.get(size);
				ndcg += dcg;
				sum ++;
			}
		}
		
		return sum > 0 ? ndcg / sum : 0;
	}
}
