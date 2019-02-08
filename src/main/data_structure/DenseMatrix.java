package main.data_structure;

import java.io.Serializable;

import happy.coding.io.Strings;
import happy.coding.math.Randoms;

/**
 * ���ܾ����ʵ��
 * 
 * @author liucheng
 */
public class DenseMatrix implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** ���� */
	private int M;
	/** ���� */
	private int N;
	private double[][] data;
	
	public DenseMatrix(int M, int N) {
		this.M = M;
		this.N = N;
		this.data = new double[M][N];
	}
	
	/**
	 * ������ָ��λ�ø�ֵ
	 */
	public void set(int row, int col, double value) {
		data[row][col] = value;
	}
	
	/**
	 * ���ؾ���ָ��λ�õ�ֵ
	 */
	public double get(int row, int col) {
		return data[row][col];
	}
	
	/**
	 * ���ø�˹�ֲ���ʼ������
	 */
	public void init(double mean, double sigma) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				data[i][j] = Randoms.gaussian(mean, sigma);
			}
		}
	}
	
	/**
	 * ����(0, range)�ľ��ȷֲ���ʼ������
	 */
	public void init(double range) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				data[i][j] = Randoms.uniform(0, range);
			}
		}
	}

	/**
	 * ����(0, 1)�ľ��ȷֲ���ʼ������
	 */
	public void init() {
		init(1.0);
	}
	
	/**
	 * ���ؾ����ض��е�����
	 * �޸ĸ��еȼ����޸�ԭ����
	 */
	public DenseVector getViewRow(int row) {
		return new DenseVector(data[row], false);
	}
	
	/**
	 * ���ؾ����ض���
	 */
	public DenseVector getRow(int row) {
		return new DenseVector(data[row], true);
	}
	
	/**
	 * ���ؾ����ض���
	 */
	public DenseVector getCol(int col) {
		double[] array = new double[M];
		for(int i =0; i<M; i++) {
			array[i] = data[i][col];
		}
		return new DenseVector(array, true);
	}
	
	/**
	 * ������ָ��λ����ֵ
	 */
	public void add(int row, int col, double value) {
		data[row][col] += value;
	}
	
	/**
	 * ������ָ��λ�ü�ֵ
	 */
	public void minus(int row, int col, double value) {
		data[row][col] -= value;
	}
	
	
	
	/**
	 * ���ؾ��������
	 */
	public int getM() {
		return M;
	}
	
	/**
	 * ���ؾ��������
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
