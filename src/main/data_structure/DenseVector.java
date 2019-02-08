package main.data_structure;

import java.io.Serializable;

import happy.coding.io.Strings;
import happy.coding.math.Randoms;
import happy.coding.math.Stats;

/**
 * ����������ʵ��
 * 
 * @author liucheng
 */
public class DenseVector implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** ����ά�� */
	private int dimension;
	private double[] data;
	
	public DenseVector(int dimension) {
		this.dimension = dimension;
		this.data = new double[dimension];
	}
	
	public DenseVector(DenseVector vector) {
		this.dimension = vector.dimension;
		this.data = vector.data.clone();
	}
	
	/**
	 * 
	 * @param array
	 * @param deep
	 */
	public DenseVector(double[] array, boolean deep) {
		this.dimension = array.length;
		if(deep) {
			this.data = array.clone();
		}else {
			this.data = array;
		}
		
	}
	
	/**
	 * ʹ�ø�˹�ֲ���ʼ����������
	 */
	public void init(double mean, double sigma) {
		for (int i = 0; i < dimension; i++) {
			data[i] = Randoms.gaussian(mean, sigma);
		}
	}
	
	/**
	 * ʹ�ã�0��1�����ȷֲ���ʼ����������
	 */
	public void init() {
		for (int i = 0; i < dimension; i++) {
			data[i] = Randoms.uniform();
		}
	}
	
	/**
	 * ��������ָ����������ֵ
	 */
	public double get(int index) {
		return data[index];
	}
	
	/**
	 * ����������ƽ��ֵ
	 */
	public double mean() {
		return Stats.mean(data);
	}
	
	/**
	 * ������������Ԫ�غ�
	 */
	public double sum(){
		return Stats.sum(data);
	}
	
	/**
	 * ������ָ����������ֵ
	 */
	public void set(int index, double value) {
		data[index] = value;
	}
	
	/**
	 * ������ָ����������ֵ
	 */
	public void add(int index, double value) {
		data[index] += value;
	}
	
	/**
	 * ������ָ����������ֵ
	 */
	public void minus(int index, double value) {
		data[index] -= value;
	}
	
	/**
	 * ����һ�������ڻ�
	 */
	public double dot(DenseVector vector) {
		assert vector.dimension == this.dimension;
		double sum = 0;
		for(int i =0; i< dimension; i++) {
			sum += this.get(i) * vector.get(i);
		}
		return sum;
	}

	public int getDimension() {
		return dimension;
	}
	
	@Override
	public String toString() {
		return Strings.toString(data);
	}
	
	

}
