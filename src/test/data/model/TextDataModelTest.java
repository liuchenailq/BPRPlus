package test.data.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.Test;

import main.configure.Configuration;
import main.data.model.AbstractDataModel;
import main.data.model.TextDataModel;
import main.data_structure.Rating;
import main.data_structure.SparseMatrix;

public class TextDataModelTest {

	@Test
	public void testBuildDataConvert() {
		Configuration conf = new Configuration();
		TextDataModel dataModel = new TextDataModel(conf);
		dataModel.buildDataConvert();
		System.out.println(dataModel.dataConvert.dataFrame.getData().size());
		//System.out.println(dataModel.getUserMapping());
		//System.out.println(dataModel.dataConvert.dataFrame);
	}
	
	@Test
	public void testBuildDataModel() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		AbstractDataModel dataModel = new TextDataModel(conf);
		dataModel.buildDataModel();
		System.out.println("�����ݣ�");
		System.out.println("�û�����"+ dataModel.dataConvert.getUserMapping().size());
		System.out.println("��Ʒ����"+ dataModel.dataConvert.getItemMapping().size());
		int count = 0;
		int index = 0;
		for(ArrayList<Rating> rs : dataModel.dataConvert.dataFrame.getData()) {
			count += rs.size();
			for(Rating r : rs) {
				assert r.userId == index;
			}
			index ++;
		}
		System.out.println("��������"+ count);
		System.out.println("�������ݣ�");
		
		int sum = 0;
		for(String key : dataModel.dataAppenders.keySet()) {
			index= 0;
			for(ArrayList<Rating> rs : dataModel.dataAppenders.get(key).dataFrame.getData()) {
				sum += rs.size();
				for(Rating r : rs) {
					assert r.userId == index;
				}
				index ++;
			}
			System.out.println(dataModel.dataAppenders.get(key).dataFrame.getData().size());
		}
		System.out.println("��������"+sum);
	}
	
	/**
	 * ��Ҫ���Ի�������
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testBuildDataModel1() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		AbstractDataModel dataModel = new TextDataModel(conf);
		dataModel.buildDataModel();
		SparseMatrix test = dataModel.getTestMatrix();
		SparseMatrix train = dataModel.getTrainMatrix();
		assert test.getM() == train.getM();
		for(int u =0;u< test.getM(); u++) {
			System.out.println(test.getViewRow(u));
			System.out.println(train.getViewRow(u));
		}
		
		
	}
	
	/**
	 * ��Ҫ����DataFrame��toSparseMatrix()����
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testBuildDataModel2() throws ClassNotFoundException {
		Configuration conf = new Configuration();
		AbstractDataModel dataModel = new TextDataModel(conf);
		dataModel.buildDataModel();
		SparseMatrix viewmatrix = dataModel.getAuxiliaryMatrix("view");
		System.out.println(viewmatrix.getNonzeroCount());
		System.out.println(viewmatrix.getRow(0));
		System.out.println(viewmatrix.getRow(0).getNonzeroCount());
		
	}

}
