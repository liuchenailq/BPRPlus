package main.data_structure;

import java.util.List;
import java.util.Set;

import main.util.KeyValue;
import main.util.ListUtil;

/**
 * 推荐列表数据结构
 * 
 * <UserIdx_1, <ItemId_1_1, Value_1_1>, <ItemId_1_2, Value_1_2>, <ItemId_1_3, Value_1_3>,...>,
 * <UserIdx_2, <ItemId_2_1, Value_2_1>, <ItemId_2_2, Value_2_2>, <ItemId_2_3, Value_2_3>,...>,
 * ...,
 * <UserIdx_n, <ItemId_n_1, Value_n_>, <ItemId_n_2, Value_n_2>, <ItemId_n_3, Value_n_3>,...>
 * 
 * @author liucheng
 *
 */
public class RecommendList {
	
	public List<List<KeyValue<Integer, Double>>> data;
	
	public RecommendList() {
		
	}
	
	public RecommendList(List<List<KeyValue<Integer, Double>>> data) {
		this.data = data;
	}
	
	public void setData(List<List<KeyValue<Integer, Double>>> data) {
		this.data = data;
	}
	
	/**
	 * 返回一个用户的推荐列表
	 * @param userId
	 * @return
	 */
	public List<KeyValue<Integer, Double>> getList(int userId){
		rangeCheck(userId);
		return data.get(userId);
	}
	
	public Set<Integer> getKeySet(int userId){
		rangeCheck(userId);
		return ListUtil.getKeySet(getList(userId));
	}
	
	public int size() {
		return data.size();
	}
	
	/**
	 * 检查索引是否出界
	 */
	private void rangeCheck(int contextIdx) {
        int size = size();
        if (contextIdx > size || contextIdx < 0) {
            throw new IndexOutOfBoundsException("没有此用户！");
        }
    }

}
