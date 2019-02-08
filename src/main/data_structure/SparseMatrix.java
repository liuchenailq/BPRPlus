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
	/** ������д洢 */
	private SparseVector[] cols;
	
	
	public SparseMatrix(int M, int N) {
		this.M = M;
		this.N = N;
		rows = new SparseVector[M];
		cols = new SparseVector[N];
		//��ʼ��
		for(int i = 0; i < M; i++) {
			rows[i] = new SparseVector(N);
		}
		for(int i=0; i < N; i++) {
			cols[i] = new SparseVector(M);
		}
	}
	
	public SparseMatrix(SparseMatrix matrix) {
		this.M = matrix.getM();
		this.N = matrix.getN();
		rows = new SparseVector[M];
		cols = new SparseVector[N];
	
		for(int i = 0; i < M; i++) {
			rows[i] = matrix.getRow(i);
		}
		for(int i=0; i < N; i++) {
			cols[i] = matrix.getCol(i);
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
		cols[col].setValue(row, value);
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
	 * ���ؾ���ָ���е�����
	 * �޸����õȼ����޸�ԭ�������
	 * 
	 * �����⣬��Ϊȡֵ�Ǵ��о�����ȡֵ
	 */
	public SparseVector getViewCol(int col) {
		return cols[col];
	}
	
	/**
	 * ���ؾ���ָ����
	 */
	public SparseVector getCol(int col) {
		return new SparseVector(cols[col]);
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(obj == this) {
			return true;
		}
		
		if(obj instanceof SparseMatrix) {
			SparseMatrix matrix  = (SparseMatrix)obj;
			if(matrix.getM() != M || matrix.getN() != N) {
				return false;
			}
			
			if(matrix.getNonzeroCount() != this.getNonzeroCount()) {
				return false;
			}
			
			for(int i=0; i<matrix.getM(); i++) {
				if(!matrix.getViewRow(i).equals(this.getViewRow(i))) {
					return false;
				}
			}
			for(int i=0; i<matrix.getN(); i++) {
				if(!matrix.getViewCol(i).equals(this.getViewCol(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
