package main.data_structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


/**
 * ������������ʵ��
 * 
 * @author liucheng
 */
public class DataFrame {
	/** user �� userId��˫��ӳ�� */
	private BiMap<String, Integer> userMapping;
	/** item �� itemId��˫��ӳ�� */
	private BiMap<String, Integer> itemMapping;
	/** ��userIdΪ�������û��������� */
	private ArrayList<ArrayList<Rating>> data;
	
	public DataFrame() {
		userMapping = HashBiMap.create();
		itemMapping = HashBiMap.create();
		data = new ArrayList<ArrayList<Rating>>();
	}
	
	public BiMap<String, Integer> getUserMapping() {
		return userMapping;
	}
	
	public void setUserMapping(BiMap<String, Integer> userMapping) {
		this.userMapping = userMapping;
	}
	
	public BiMap<String, Integer> getItemMapping() {
		return itemMapping;
	}
	
	public void setItemMapping(BiMap<String, Integer> itemMapping) {
		this.itemMapping = itemMapping;
	}
	
	public ArrayList<ArrayList<Rating>> getData() {
		return data;
	}
	
	public void setData(ArrayList<ArrayList<Rating>> data) {
		this.data = data;
	}
	
	public void addRating(Rating r) {
		if(data.size()-1 < r.userId) {
			data.add(new ArrayList<Rating>());
		}
		data.get(r.userId).add(r);
	}
	
	public int setUserId(String user) {
		if(!userMapping.containsKey(user)) {
			userMapping.put(user, userMapping.size());
		}
		return userMapping.get(user);
	}
	
	public int setItemId(String item) {
		if(!itemMapping.containsKey(item)) {
			itemMapping.put(item, itemMapping.size());
		}
		return itemMapping.get(item);
	}
	
	public int getUserId(String user) {
		return userMapping.get(user);
	}
	
	public int getItemId(String item) {
		return itemMapping.get(item);
	}
	
	@Override
	public String toString() {
		String str = "";
		for(ArrayList<Rating> rs : data) {
			for(Rating r : rs) {
				str += r + "\n";
			}
		}
		return str;
	}
	
	/** 
	 * ��ÿ���û������ְ�ʱ������
	 */
	public void sortDataByTime(boolean descend) {
		Comparator<Rating> c = new Comparator<Rating>() {
			public int compare(Rating o1, Rating o2) {
				if(!descend) {  // ����
					if(o1.timestamp - o2.timestamp >0) {
						return 1;
					}else {
						if(o1.timestamp - o2.timestamp < 0) {
							return -1;
						}else {
							return 0;
						}
					}
				}else {  //����
					if(o1.timestamp - o2.timestamp >0) {
						return -1;
					}else {
						if(o1.timestamp - o2.timestamp < 0) {
							return 1;
						}else {
							return 0;
						}
					}
				}
			}
		};
		
		List<Integer> indexList = new ArrayList<>();
		for(int u = 0; u<data.size(); u++) {
			indexList.add(u);
		}
		//��������
		indexList.parallelStream().forEach((Integer u) -> {
			Collections.sort(data.get(u), c);
		});
	}
	
	/**
	 * ��dataת��Ϊϵ������
	 */
	public SparseMatrix toSparseMatrix() {
		int M = userMapping.size();
		int N = itemMapping.size();
		SparseMatrix matrix = new SparseMatrix(M, N);
		for(ArrayList<Rating> rs : data) {
			for(Rating r : rs) {
				matrix.setValue(r.getUserId(), r.getItemId(), r.getScore());
			}
		}
		return matrix;
	}
}
