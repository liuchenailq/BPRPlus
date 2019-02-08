package main.recommender;

import java.util.Random;
import java.util.Set;


/**
 * BPR�㷨��ʵ��
 *  �ο� "BPR Bayesian Personalized Ranking from Implicit Feedback"
 * 
 * @author liucheng
 */

public class BPR_baseline extends AbstractMFRecommender{
	
	public Integer[][] buydata;
	
	public BPR_baseline() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		buydata = new Integer[userCount][];
		for(int i =0; i<userCount; i++) {
			Set<Integer> items = trainMatrix.getViewRow(i).getIndexList();
			buydata[i] = items.toArray(new Integer[items.size()]);
		}
	}

	@Override
	public void trainModel() throws ClassNotFoundException {
		int nozero = trainMatrix.getNonzeroCount();
		for(int iter = 1; iter<=maxIter; iter++) {
			Random rand = new Random();
			loss = 0.0d;
			for(int sampleCount =0; sampleCount<nozero; sampleCount++) {
				//���ѡȡ��Ԫ��(userIdx, posItemIdx, negItemIdx)
				int userIdx, posItemIdx, negItemIdx;
				while(true) {
					userIdx = rand.nextInt(userCount);
					if(buydata[userIdx].length == 0 || buydata[userIdx].length == itemCount) {
						continue;
					}
					posItemIdx = buydata[userIdx][rand.nextInt(buydata[userIdx].length)];
					do {
                        negItemIdx = rand.nextInt(itemCount);
                    } while(trainMatrix.getValue(userIdx, negItemIdx) != 0d);
					break;
				}
				//���²���
				update(userIdx, posItemIdx, negItemIdx);
			}
			//��¼loss
			losses[iter-1] = loss;
			
			if(showLoss) {
				LOG.info("��" + iter +"�ε�����loss�� " + loss);
			}
			
//			if(iter > 490) {
//				AbstractRecommenderEvaluator evaluator = new HitRateEvaluator();
//				double value = evaluator.evaluate(this, true);
//				LOG.info("��" + iter +"�ε�����hit�� " + value);
//			}
			
			//ʵʱ����ģ��
			modelEvaluate(evaluateIsRealTime, iter);
			//����ѧϰ����
			updateLearnRate(iter);
		}
		
	}

	/**
	 * ������Ԫ����һ���ݶ��½�
	 */
	private void update(int userIdx, int posItemIdx, int negItemIdx) {
		//���²���
        double posPredictRating = predict(userIdx, posItemIdx);
        double negPredictRating = predict(userIdx, negItemIdx);
        double diffValue = posPredictRating - negPredictRating;

        double lossValue = -Math.log(logistic(diffValue));
        loss += lossValue;

        double deriValue = logistic(-diffValue);

        for (int factorIdx = 0; factorIdx < factors; factorIdx++) {
            double userFactorValue = U.get(userIdx, factorIdx);
            double posItemFactorValue = V.get(posItemIdx, factorIdx);
            double negItemFactorValue = V.get(negItemIdx, factorIdx);

            U.add(userIdx, factorIdx, learnRate * (deriValue * (posItemFactorValue - negItemFactorValue) - reg * userFactorValue));
            V.add(posItemIdx, factorIdx, learnRate * (deriValue * userFactorValue - reg * posItemFactorValue));
            V.add(negItemIdx, factorIdx, learnRate * (deriValue * (-userFactorValue) - reg * negItemFactorValue));

            loss += reg * userFactorValue * userFactorValue + reg * posItemFactorValue * posItemFactorValue + reg * negItemFactorValue * negItemFactorValue;
        }
	}
	
	private double logistic(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}
}
