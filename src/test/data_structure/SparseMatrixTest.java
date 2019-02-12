package test.data_structure;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data_structure.SparseMatrix;
import main.data_structure.SparseVector;

public class SparseMatrixTest {

	@Test
	public void testGetValueAndSetValue() {
		SparseMatrix matrix = new SparseMatrix(10, 10);
		matrix.setValue(0, 0, 1d);
		matrix.setValue(1, 1, 2d);
		matrix.setValue(9, 9, 3d);
		assert matrix.getValue(0, 0) == 1d;
		assert matrix.getValue(1, 1) == 2d;
		assert matrix.getValue(9, 9) == 3d;
		assert matrix.getNonzeroCount() == 3;
		matrix.setValue(0, 0, 0d);
		assert matrix.getNonzeroCount() == 2;
	}
	
	@Test
	public void testGetViewRow() {
		SparseMatrix matrix = new SparseMatrix(10, 10);
		matrix.setValue(0, 0, 1d);
		matrix.setValue(0, 1, 2d);
		matrix.setValue(0, 9, 3d);
		SparseVector v = matrix.getViewRow(0);
		assert v.getValue(0) == 1d;
		assert v.getValue(1) == 2d;
		assert v.getValue(9) == 3d;
		assert v.getDimension() == 10;
		assert v.getNonzeroCount() == 3;
		v.setValue(0, 10d);
		assert v.getValue(0) == 10d;
		assert matrix.getValue(0, 0) == 10d;
	}
	
	@Test
	public void testGetRow() {
		SparseMatrix matrix = new SparseMatrix(10, 10);
		matrix.setValue(0, 0, 1d);
		matrix.setValue(0, 1, 2d);
		matrix.setValue(0, 9, 3d);
		SparseVector v = matrix.getRow(0);
		assert v.getValue(0) == 1d;
		assert v.getValue(1) == 2d;
		assert v.getValue(9) == 3d;
		assert v.getDimension() == 10;
		assert v.getNonzeroCount() == 3;
		v.setValue(0, 10d);
		assert v.getValue(0) == 10d;
		assert matrix.getValue(0, 0) == 1d;
	}

}
