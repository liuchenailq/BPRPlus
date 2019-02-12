package main.data_structure;

/**
 * ϡ������ʵ��
 * 
 * @author liucheng
 */
public class SparseMatrix {
	/** ��������� */
	private int M;
	/** ��������� */
	private int N;
	
	/** ������д洢 */
	private SparseVector[] rows;
	
	
	public SparseMatrix(int M, int N) {
		this.M = M;
		this.N = N;
		rows = new SparseVector[M];
		//��ʼ��
		for(int i = 0; i < M; i++) {
			rows[i] = new SparseVector(N);
		}
	}
	
	public SparseMatrix(SparseMatrix matrix) {
		this.M = matrix.getM();
		this.N = matrix.getN();
		rows = new SparseVector[M];
	
		for(int i = 0; i < M; i++) {
			rows[i] = matrix.getRow(i);
		}
	}
	
	/**
	 * ���ؾ���ָ��λ�õ�ֵ 
	 */
	public double getValue(int row, int col) {
		return rows[row].getValue(col);
	}
	
	/** 
	 * ������ָ��λ�ø�ֵ 
	 */
	public void setValue(int row, int col, double value) {
		rows[row].setValue(col, value);
	}
	
	/**
	 * ���ؾ���ָ���е�����
	 * �޸����õȼ����޸�ԭ�������
	 */
	public SparseVector getViewRow(int row) {
		return rows[row];
	}
	
	/**
	 * ���ؾ���ָ����
	 */
	public SparseVector getRow(int row) {
		return new SparseVector(rows[row]);
	}
	
	
	/**
	 * ���ؾ����з���Ԫ����
	 */
	public int getNonzeroCount() {
		int count = 0;
		for(int i = 0; i < M; i++) {
			count += rows[i].getNonzeroCount();
		}
		return count;
	}
	
	/** 
	 * ���ؾ��������
	 */
	public int getM() {
		return M;
	}
	
	/**
	 * ���ؾ��������
	 */
	public int getN() {
		return N;
	}
	
	
}
