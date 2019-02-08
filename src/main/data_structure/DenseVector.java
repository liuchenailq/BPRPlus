package main.data_structure;

import java.io.Serializable;

import happy.coding.io.Strings;
import happy.coding.math.Randoms;
import happy.coding.math.Stats;

/**
 * 稠密向量的实现
 * 
 * @author liucheng
 */
public class DenseVector implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 向量维度 */
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
	 * 使用高斯分布初始化稠密向量
	 */
	public void init(double mean, double sigma) {
		for (int i = 0; i < dimension; i++) {
			data[i] = Randoms.gaussian(mean, sigma);
		}
	}
	
	/**
	 * 使用（0，1）均匀分布初始化稠密向量
	 */
	public void init() {
		for (int i = 0; i < dimension; i++) {
			data[i] = Randoms.uniform();
		}
	}
	
	/**
	 * 返回向量指定索引处的值
	 */
	public double get(int index) {
		return data[index];
	}
	
	/**
	 * 返回向量的平均值
	 */
	public double mean() {
		return Stats.mean(data);
	}
	
	/**
	 * 返回向量所有元素和
	 */
	public double sum(){
		return Stats.sum(data);
	}
	
	/**
	 * 给向量指定索引处赋值
	 */
	public void set(int index, double value) {
		data[index] = value;
	}
	
	/**
	 * 给向量指定索引处增值
	 */
	public void add(int index, double value) {
		data[index] += value;
	}
	
	/**
	 * 给向量指定索引处减值
	 */
	public void minus(int index, double value) {
		data[index] -= value;
	}
	
	/**
	 * 与另一向量的内积
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
