package main.recommenderEvaluator;

import java.util.List;
import java.util.Set;

import main.data_structure.RecommendList;
import main.util.KeyValue;
/**
 * 精度评价指标
 * 
 * @author liucheng
 */
public class PrecisionEvaluator extends AbstractRecommenderEvaluator{
	
	public PrecisionEvaluator() {
		
	}

	@Override
	public double evaluate(RecommendList truthList, RecommendList recommendList) {
		double totalPrecision = 0d;
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
				if(test.size() > 0 ) {
					totalPrecision += numHits * 1.0 / test.size();
					sum ++;
				}
			}
		}
		return sum > 0 ? totalPrecision / sum : 0;
	}

}
