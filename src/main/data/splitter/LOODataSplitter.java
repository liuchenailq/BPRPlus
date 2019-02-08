package main.data.splitter;

import java.util.ArrayList;

import org.apache.commons.logging.LogFactory;
import org.jfree.util.Log;

import main.data_structure.Rating;
import main.data_structure.SparseMatrix;

/**
 * ��һ��:��ÿ���û����µ����ּ�¼��Ϊ���Լ�
 * 
 * @author liucheng
 */
public class LOODataSplitter extends AbstractDataSplitter{
	
	public LOODataSplitter() {
		
	}

	@Override
	public void splitData() {
		LOG.info("��ʼ��������");
		//��ÿ���û������ְ�ʱ�併������
		dataConvert.dataFrame.sortDataByTime(true);
		trainMatrix = new SparseMatrix(dataConvert.getUserCount(), dataConvert.getItemCount());
		testMatrix = new SparseMatrix(dataConvert.getUserCount(), 1);
		for(int u = 0; u < dataConvert.getUserCount(); u++) {
			ArrayList<Rating> rs = dataConvert.dataFrame.getData().get(u);
			if(rs.size() > 1) { 
				//LOG.info("�û�" + u + ":" + rs.get(0).itemId);
				testMatrix.setValue(u, 0, rs.get(0).score);
			}
			for(int i = 1; i < rs.size(); i++) {
				trainMatrix.setValue(u, rs.get(i).itemId, rs.get(i).score);
			}
		}
		LOG.info("�������ݳɹ���");
		LOG.info("ѵ������С��" + trainMatrix.getNonzeroCount() + "�� ���Լ���С" + testMatrix.getNonzeroCount());
	}

}
