package main.recommender;

import java.util.Random;
import java.util.Set;

import main.util.MathUtil;


/**
 * BPR算法的实现
 *  参考 "BPR Bayesian Personalized Ranking from Implicit Feedback"
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
		int flag = 0;
		for(int iter = 1; iter<=maxIter; iter++) {
			Random rand = new Random();
			loss = 0.0d;
			for(int sampleCount =0; sampleCount<nozero; sampleCount++) {
				//随机选取三元组(userIdx, posItemIdx, negItemIdx)
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
				//更新参数
				update(userIdx, posItemIdx, negItemIdx);
			}
			//记录loss
			losses[iter-1] = loss;
			
			if(showLoss) {
				LOG.info("第" + iter +"次迭代的loss： " + loss);
			}
			flag ++;
			//间隔20次迭代评价模型
			if(flag == 20) {
				modelEvaluate(evaluateIsRealTime, iter);
				flag = 0;
			}
			
			//更新学习速率
			updateLearnRate(iter);
		}
		
	}

	/**
	 * 利用三元组做一次梯度下降
	 */
	private void update(int userIdx, int posItemIdx, int negItemIdx) {
		//更新参数
        double posPredictRating = predict(userIdx, posItemIdx);
        double negPredictRating = predict(userIdx, negItemIdx);
        double diffValue = posPredictRating - negPredictRating;

        double lossValue = -Math.log(MathUtil.logistic(diffValue));
        loss += lossValue;

        double deriValue = MathUtil.logistic(-diffValue);

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
	
}
