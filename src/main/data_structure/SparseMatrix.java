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
	/** 矩阵的列存储 */
	private SparseVector[] cols;
	
	
	public SparseMatrix(int M, int N) {
		this.M = M;
		this.N = N;
		rows = new SparseVector[M];
		cols = new SparseVector[N];
		//初始化
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
		cols[col].setValue(row, value);
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
	 * 返回矩阵指定列的引用
	 * 修改引用等价于修改原矩阵的列
	 * 
	 * 有问题，因为取值是从行矩阵中取值
	 */
	public SparseVector getViewCol(int col) {
		return cols[col];
	}
	
	/**
	 * 返回矩阵指定列
	 */
	public SparseVector getCol(int col) {
		return new SparseVector(cols[col]);
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
