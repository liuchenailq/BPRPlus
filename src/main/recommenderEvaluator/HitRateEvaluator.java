package main.recommenderEvaluator;

import java.util.ArrayList;
import java.util.List;

import main.exception.BPRPlusException;
import main.util.KeyValue;

/**
 * ������
 * �����ݼ��Ļ��ַ���Ϊ��һ��ʱ��������ָ���������
 * @author liucheng
 *
 */

public class HitRateEvaluator extends AbstractRecommenderEvaluator{
	
	public HitRateEvaluator() {
		
	}

	@Override
	public double evaluate(List<List<KeyValue<Integer, Double>>> truthList,
			List<List<KeyValue<Integer, Double>>> recommenderList) {
		
		int hitTotla = 0;
		int sum = 0;
		for(int u = 0; u< truthList.size(); u++) {
			List<KeyValue<Integer, Double>> truth = truthList.get(u);
			if(truth.size() <= 1) {
				if(truth.size() == 1) {
					int itemId = truth.get(0).getKey();
					List<KeyValue<Integer, Double>> test = recommenderList.get(u);
					for(KeyValue<Integer, Double> entry : test) {
						if(itemId == entry.getKey()) {
							hitTotla ++;
							break;
						}
					}
					sum ++;
				}
			}else {
				new BPRPlusException("���ݼ����ַ���������һ����");
			}
		}
		
		return sum > 0 ? 1.0 * hitTotla / sum : 0d;
	}

}
