package main.data.splitter;

import java.util.ArrayList;

import org.apache.commons.logging.LogFactory;
import org.jfree.util.Log;

import main.data_structure.Rating;
import main.data_structure.SparseMatrix;

/**
 * 留一法:将每个用户最新的评分记录作为测试集
 * 
 * @author liucheng
 */
public class LOODataSplitter extends AbstractDataSplitter{
	
	public LOODataSplitter() {
		
	}

	@Override
	public void splitData() {
		LOG.info("开始划分数据");
		//将每个用户的评分按时间降序排列
		dataConvert.dataFrame.sortDataByTime(true);
		trainMatrix = new SparseMatrix(dataConvert.getUserCount(), dataConvert.getItemCount());
		testMatrix = new SparseMatrix(dataConvert.getUserCount(), 1);
		for(int u = 0; u < dataConvert.getUserCount(); u++) {
			ArrayList<Rating> rs = dataConvert.dataFrame.getData().get(u);
			if(rs.size() > 1) { 
				//LOG.info("用户" + u + ":" + rs.get(0).itemId);
				testMatrix.setValue(u, 0, rs.get(0).score);
			}
			for(int i = 1; i < rs.size(); i++) {
				trainMatrix.setValue(u, rs.get(i).itemId, rs.get(i).score);
			}
		}
		LOG.info("划分数据成功！");
		LOG.info("训练集大小：" + trainMatrix.getNonzeroCount() + "， 测试集大小" + testMatrix.getNonzeroCount());
	}

}
