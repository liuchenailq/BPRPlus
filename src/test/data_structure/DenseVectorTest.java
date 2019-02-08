package test.data_structure;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data_structure.DenseVector;

public class DenseVectorTest {

	@Test
	public void testSetAndGet() {
		DenseVector vector = new DenseVector(10);
		vector.set(0, 1d);
		vector.set(1, 2d);
		vector.set(2, 3d);
		assert vector.getDimension() == 10;
		assert vector.get(0) == 1d;
		assert vector.get(1) == 2d;
		assert vector.get(2) == 3d;
		assert vector.sum() == 6d;
		assert vector.mean() == 0.6d;
		
		vector.add(0, 11d);
		assert vector.get(0) == 12d;
		vector.minus(0, 6d);
		assert vector.get(0) == 6d;
	}

}
