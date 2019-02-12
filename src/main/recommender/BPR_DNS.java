package main.recommender;

import java.util.Random;
import java.util.Set;

import main.util.MathUtil;

/**
 *  利用动态负样本采样策略（选取当前评分最高的负样本）优化BPR算法
 *  参考 "Optimizing Top-N Collaborative Filtering via Dynamic Negative Item Sampling"
 * 
 * @author liucheng
 *
 */

public class BPR_DNS extends AbstractMFRecommender{
	public Integer[][] buydata;
	/** 选取评分最大的负样本时比较次数 */
	public int paraK;
    
	public BPR_DNS() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		buydata = new Integer[userCount][];
		for(int i =0; i<userCount; i++) {
			Set<Integer> items = trainMatrix.getViewRow(i).getIndexList();
			buydata[i] = items.toArray(new Integer[items.size()]);
		}
		paraK = conf.getInt("rec.dns.paraK", 100);
	}

	@Override
	public void trainModel() throws ClassNotFoundException {
		int nozero = trainMatrix.getNonzeroCount();
		for(int iter = 1; iter<=maxIter; iter++) {
			Random rand = new Random();
			loss = 0.0d;
			for(int sampleCount =0; sampleCount<nozero; sampleCount++) {
				//随机选取正样本(userIdx, posItemIdx)
				int userIdx, posItemIdx;
				while(true) {
					userIdx = rand.nextInt(userCount);
					if(buydata[userIdx].length == 0 || buydata[userIdx].length == itemCount) {
						continue;
					}
					posItemIdx = buydata[userIdx][rand.nextInt(buydata[userIdx].length)];
					break;
				}
				//更新参数
				update(userIdx, posItemIdx);
			}
			//记录loss
			losses[iter-1] = loss;
			
			if(showLoss) {
				LOG.info("第" + iter +"次迭代的loss： " + loss);
			}
			
			//实时评价模型
			modelEvaluate(evaluateIsRealTime, iter);
			
			//更新学习速率
			updateLearnRate(iter);
		}
		
	}
	
	/**
	 * 利用三元组做一次梯度下降
	 */
	private void update(int userIdx, int posItemIdx) {
		Random rand = new Random();
		//利用动态负样本采样策略选取负样本negItemIdx
		int negItemIdx = rand.nextInt(itemCount);
		double score = -Double.MAX_VALUE;
		int tempj = 0;
		double tempScore = 0;
		for (int k=0; k<paraK; k++){
			do {
				tempj = rand.nextInt(itemCount);
            } while(trainMatrix.getValue(userIdx, tempj) != 0d);
			tempScore = this.predict(userIdx, tempj);
			if(tempScore > score) {
				score = tempScore;
				negItemIdx = tempj;
			}
		}
		
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
