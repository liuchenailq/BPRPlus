package main.recommender;

import main.data_structure.DenseMatrix;

/**
 * ����ֽ�ģ��
 * 
 * @author liucheng
 *
 */
public abstract class AbstractMFRecommender extends AbstractTopNRecommender{
	/** ������Ŀ */
	public int factors;
	/** ���������� */
	public int maxIter;
	/** ѧϰ���� */
	public double learnRate;
	/** �Ƿ��Զ�����ѧϰ���� */
	public boolean adaptive;
	/** ������ */
	public double reg;
	/** �û����Ӿ��� */
	public DenseMatrix U;
	/** ��Ʒ���Ӿ��� */
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
		
		//���ģ�Ͳ���
		String modelPars = this.getClass().getSimpleName()+":TopN="+TopN +",factors=" + factors + ",maxIter=" + maxIter+",learnRate="+learnRate+",adaptive="+adaptive+",reg="+reg;
		LOG.info(modelPars);
	}
	
	@Override
	public double predict(int userId, int itemId) {
		return U.getViewRow(userId).dot(V.getViewRow(itemId));
	}
	
	/**
	 * ��ÿ�ε������Զ�����ѧϰ����
	 */
	public void updateLearnRate(int iter) {
		if(adaptive && iter > 1) {
			learnRate = Math.abs(lastLoss) > Math.abs(loss) ? learnRate * 1.05f : learnRate * 0.5f;
		}
		lastLoss = loss;
	}

}
