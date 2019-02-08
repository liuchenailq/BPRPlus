package test.data_structure;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data_structure.DenseMatrix;

public class DenseMatrixTest {

	@Test
	public void testSetAndGet() {
		DenseMatrix matrix = new DenseMatrix(10,10);
		matrix.set(0, 0, 1d);
		matrix.set(9, 9, 10d);
		assert matrix.getM() == 10;
		assert matrix.getN() == 10;
		assert matrix.get(0, 0) == 1d;
		assert matrix.get(9, 9) == 10d;
		matrix.add(0, 0, 1d);
		assert matrix.get(0, 0) == 2d;
		matrix.minus(0, 0, 1d);
		assert matrix.get(0, 0) == 1d;
	}

}
