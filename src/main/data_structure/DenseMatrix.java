package main.data_structure;

import java.io.Serializable;

import happy.coding.io.Strings;
import happy.coding.math.Randoms;

/**
 * 稠密矩阵的实现
 * 
 * @author liucheng
 */
public class DenseMatrix implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 行数 */
	private int M;
	/** 列数 */
	private int N;
	private double[][] data;
	
	public DenseMatrix(int M, int N) {
		this.M = M;
		this.N = N;
		this.data = new double[M][N];
	}
	
	/**
	 * 给矩阵指定位置赋值
	 */
	public void set(int row, int col, double value) {
		data[row][col] = value;
	}
	
	/**
	 * 返回矩阵指定位置的值
	 */
	public double get(int row, int col) {
		return data[row][col];
	}
	
	/**
	 * 利用高斯分布初始化矩阵
	 */
	public void init(double mean, double sigma) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				data[i][j] = Randoms.gaussian(mean, sigma);
			}
		}
	}
	
	/**
	 * 利用(0, range)的均匀分布初始化矩阵
	 */
	public void init(double range) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				data[i][j] = Randoms.uniform(0, range);
			}
		}
	}

	/**
	 * 利用(0, 1)的均匀分布初始化矩阵
	 */
	public void init() {
		init(1.0);
	}
	
	/**
	 * 返回矩阵特定行的引用
	 * 修改该行等价于修改原矩阵
	 */
	public DenseVector getViewRow(int row) {
		return new DenseVector(data[row], false);
	}
	
	/**
	 * 返回矩阵特定行
	 */
	public DenseVector getRow(int row) {
		return new DenseVector(data[row], true);
	}
	
	/**
	 * 返回矩阵特定列
	 */
	public DenseVector getCol(int col) {
		double[] array = new double[M];
		for(int i =0; i<M; i++) {
			array[i] = data[i][col];
		}
		return new DenseVector(array, true);
	}
	
	/**
	 * 给矩阵指定位置增值
	 */
	public void add(int row, int col, double value) {
		data[row][col] += value;
	}
	
	/**
	 * 给矩阵指定位置减值
	 */
	public void minus(int row, int col, double value) {
		data[row][col] -= value;
	}
	
	
	
	/**
	 * 返回矩阵的行数
	 */
	public int getM() {
		return M;
	}
	
	/**
	 * 返回矩阵的列数
	 * @return
	 */
	public int getN() {
		return N;
	}
	
	@Override
	public String toString() {
		return Strings.toString(data);
	}
}
