package main.recommender;

import java.util.Random;
import java.util.Set;
import main.data_structure.SparseMatrix;
import main.util.MathUtil;

/**
 * 利用view data 增强BPR性能 (BPR+View_prob)
 * 详情见 "Sampler Design for Bayesian Personalized Ranking by Leveraging View Data"
 * 
 * @author liucheng
 *
 */
public class BPR_PlusView extends AbstractMFRecommender{
	public Integer[][] buydata;
	public Integer[][] viewdata;
	/** (buy, view) 样本的概率 */
	public double p_iv;
	/** (buy, not)样本的概率 */
	public double p_ij;
	/** (view, not)样本的概率 */
	public double p_vj;
	
	Random rand = new Random();
	
	public BPR_PlusView() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		
		p_iv = conf.getDouble("rec.plusview.p_iv", 0.1);
		p_ij = conf.getDouble("rec.plusview.p_ij", 0.3);
		p_vj = 1.0 - p_iv - p_ij;
		
		buydata = new Integer[userCount][];
		for(int i =0; i<userCount; i++) {
			Set<Integer> items = trainMatrix.getViewRow(i).getIndexList();
			buydata[i] = items.toArray(new Integer[items.size()]);
		}
		
		SparseMatrix viewMatrix = this.getauxiliaryMatrix("view");
		viewdata = new Integer[userCount][];
		for(int i =0; i<userCount; i++) {
			Set<Integer> items = viewMatrix.getViewRow(i).getIndexList();
			viewdata[i] = items.toArray(new Integer[items.size()]);
		}
		
	}

	@Override
	public void trainModel() throws ClassNotFoundException {
		double p_buyispos = 1 - p_vj;  //正样本为buy的概率
		double p_viewisneg = p_iv / p_buyispos;  //当正样本为buy时，view为负样本的概率
		int nozero = trainMatrix.getNonzeroCount();
		int flag = 0;
		for(int iter = 1; iter<=maxIter; iter++) {
			loss = 0.0d;
			for(int sampleCount =0; sampleCount<nozero; sampleCount++) {
				//随机选取三元组(userIdx, posItemIdx, negItemIdx)
				int userIdx, posItemIdx, negItemIdx;
				while(true) {
					//选取一个用户
					userIdx = rand.nextInt(userCount);
					if(buydata[userIdx].length == 0 || buydata[userIdx].length == itemCount || viewdata[userIdx].length == 0) {
						continue;
					}
					double p1 = rand.nextDouble();
					if(p1 < p_buyispos) { //购买为正样本
						posItemIdx = buydata[userIdx][rand.nextInt(buydata[userIdx].length)];
						double p2 = rand.nextDouble();
						if(p2 < p_viewisneg) {
							negItemIdx = viewdata[userIdx][rand.nextInt(viewdata[userIdx].length)];
						}else {
							do {
		                        negItemIdx = rand.nextInt(itemCount);
		                    } while(trainMatrix.getValue(userIdx, negItemIdx) != 0d);
						}
					}else {  //浏览为正样本
						posItemIdx = viewdata[userIdx][rand.nextInt(viewdata[userIdx].length)];
						do {
	                        negItemIdx = rand.nextInt(itemCount);
	                    } while(trainMatrix.getValue(userIdx, negItemIdx) != 0d);
					}
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
