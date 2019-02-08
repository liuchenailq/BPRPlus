package test.data_structure;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data_structure.SparseVector;

public class SparseVectorTest {

	@Test
	public void testSetValue() {
		SparseVector vector = new SparseVector(10);
		vector.setValue(0, 100d);
		vector.setValue(5, 101d);
		assert vector.getValue(0) == 100d;
		assert vector.getValue(5) == 101d;
		assert vector.getNonzeroCount() == 2;
		vector.setValue(5, 0d);
		assert vector.getValue(0) == 100d;
		assert vector.getValue(5) == 0d;
		assert vector.getNonzeroCount() == 1;
	}
	
	@Test
	public void testToString() {
		SparseVector vector = new SparseVector(10);
		vector.setValue(0, 100d);
		vector.setValue(5, 101d);
		System.out.println(vector);
	}
	
	@Test
	public void testCopy() {
		SparseVector vector = new SparseVector(10);
		vector.setValue(0, 100d);
		vector.setValue(5, 101d);
		SparseVector vector1 = new SparseVector(vector);
		System.out.println(vector1);
	}
	
	@Test
	public void tetsEquals() {
		SparseVector vector = new SparseVector(10);
		vector.setValue(0, 100d);
		vector.setValue(5, 101d);
		SparseVector vector1 = new SparseVector(vector);
		assert vector.equals(vector1);
		vector1.setValue(0, 1d);
		assert !vector.equals(vector1);
	}
	

}
