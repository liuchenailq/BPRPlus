package main.data_structure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 稀疏向量的实现
 * 
 * @author liucheng
 */
public class SparseVector implements Iterable<Entry<Integer, Double>>, Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 向量维度 */
	private int dimension;
	/** 向量索引-值 */
	private HashMap<Integer, Double> map;
	
	public SparseVector() {
		this.dimension = 0;
		this.map = new HashMap<Integer, Double>();
	}
	
	public SparseVector(int dimension) {
		this.dimension = dimension;
		this.map = new HashMap<Integer, Double>();
	}
	
	public SparseVector(SparseVector vector) {
		this(vector.dimension);
		for(Entry<Integer, Double> entry : vector) {
			this.setValue(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 返回向量指定索引处的值
	 */
	public double getValue(int index) {
		if(map.containsKey(index)) {
			return map.get(index);
		}else {
			return 0d;
		}
	}
	
	/**
	 * 向量指定索引处赋值
	 */
	public void setValue(int index, double value) {
		if(0d == value) {
			if(map.containsKey(index)) {
				map.remove(index);
			}
		}else {
			map.put(index, value);
		}
	}
	
	/**
	 * 返回非零元个数
	 */
	public int getNonzeroCount() {
		return map.size();
	}

	public int getDimension() {
		return dimension;
	}

	public HashMap<Integer, Double> getMap() {
		return map;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(Entry<Integer, Double> entry : this) {
			s += String.format("(%d: %.6f) ", entry.getKey(), entry.getValue());
		}
        return s;	
	}

	@Override
	public Iterator<Entry<Integer, Double>> iterator() {
		return map.entrySet().iterator();
	}
	
	/** 返回所有非零元的索引**/
	public Set<Integer> getIndexList(){
		return map.keySet();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(obj instanceof SparseVector) {
			SparseVector vector = (SparseVector)obj;
			if(vector.getDimension() != dimension) {
				return false;
			}
			
			if(vector.getNonzeroCount() != this.getNonzeroCount()) {
				return false;
			}
			
			for(Entry<Integer, Double> entry : vector) {
				if(this.getValue(entry.getKey()) != entry.getValue()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
