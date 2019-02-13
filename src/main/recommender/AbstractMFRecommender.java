package main.recommender;

import main.data_structure.DenseMatrix;

/**
 * 矩阵分解模型
 * 
 * @author liucheng
 *
 */
public abstract class AbstractMFRecommender extends AbstractTopNRecommender{
	/** 因子数目 */
	public int factors;
	/** 最大迭代次数 */
	public int maxIter;
	/** 学习速率 */
	public double learnRate;
	/** 是否自动调整学习速率 */
	public boolean adaptive;
	/** 正则项 */
	public double reg;
	/** 用户因子矩阵 */
	public DenseMatrix U;
	/** 物品因子矩阵 */
	public DenseMatrix V;
	
	
	@Override
	public void setup() {
		super.setup();
		
		factors = conf.getInt("rec.factor.number", 10);
		maxIter = conf.getInt("rec.iterator.maximum", 50);
		learnRate = conf.getDouble("rec.iterator.learnRate", 0.01);
		adaptive = conf.getBoolean("rec.learnRate.adaptive", false);
		reg = conf.getDouble("rec.iterator.regularization", 0.01);
		U = new DenseMatrix(userCount, factors);
		U.init(0, 0.01);
		V = new DenseMatrix(itemCount, factors);
		V.init(0, 0.01);
		losses = new double[maxIter];
		
		//输出模型参数
		String modelPars = this.getClass().getSimpleName()+":TopN="+TopN +",factors=" + factors + ",maxIter=" + maxIter+",learnRate="+learnRate+",adaptive="+adaptive+",reg="+reg;
		LOG.info(modelPars);
	}
	
	@Override
	public double predict(int userId, int itemId) {
		return U.getViewRow(userId).dot(V.getViewRow(itemId));
	}
	
	/**
	 * 在每次迭代后，自动更新学习速率
	 */
	public void updateLearnRate(int iter) {
		if(adaptive && iter > 1) {
			learnRate = Math.abs(lastLoss) > Math.abs(loss) ? learnRate * 1.05f : learnRate * 0.5f;
		}
		lastLoss = loss;
	}

}
