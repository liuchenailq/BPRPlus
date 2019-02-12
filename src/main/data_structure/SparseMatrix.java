package main.data_structure;

/**
 * 稀疏矩阵的实现
 * 
 * @author liucheng
 */
public class SparseMatrix {
	/** 矩阵的行数 */
	private int M;
	/** 矩阵的列数 */
	private int N;
	
	/** 矩阵的行存储 */
	private SparseVector[] rows;
	
	
	public SparseMatrix(int M, int N) {
		this.M = M;
		this.N = N;
		rows = new SparseVector[M];
		//初始化
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
	 * 返回矩阵指定位置的值 
	 */
	public double getValue(int row, int col) {
		return rows[row].getValue(col);
	}
	
	/** 
	 * 给矩阵指定位置赋值 
	 */
	public void setValue(int row, int col, double value) {
		rows[row].setValue(col, value);
	}
	
	/**
	 * 返回矩阵指定行的引用
	 * 修改引用等价于修改原矩阵的行
	 */
	public SparseVector getViewRow(int row) {
		return rows[row];
	}
	
	/**
	 * 返回矩阵指定行
	 */
	public SparseVector getRow(int row) {
		return new SparseVector(rows[row]);
	}
	
	
	/**
	 * 返回矩阵中非零元个数
	 */
	public int getNonzeroCount() {
		int count = 0;
		for(int i = 0; i < M; i++) {
			count += rows[i].getNonzeroCount();
		}
		return count;
	}
	
	/** 
	 * 返回矩阵的行数
	 */
	public int getM() {
		return M;
	}
	
	/**
	 * 返回矩阵的列数
	 */
	public int getN() {
		return N;
	}
	
	
}
