package main.data_structure;
/**
 * һ�����ּ�¼ʵ����
 * 
 * @author liucheng
 */
public class Rating {
	
	public int userId; 
	public int itemId; 
	public double score;
	public long timestamp;
	
	public int getUserId() {
		return userId;
	}
	public int getItemId() {
		return itemId;
	}
	public double getScore() {
		return score;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public Rating(int userId, int itemId, double score, long timestamp) {
		this.userId = userId;
		this.itemId = itemId;
		this.score = score;
		this.timestamp = timestamp;
	}
	public Rating(int userId, int itemId, double score) {
		this.userId = userId;
		this.itemId = itemId;
		this.score = score;
	}
	
	@Override
	public String toString() {
		return userId +"\t" + itemId + "\t" + score +"\t" + timestamp;
		
	}
	
	
	
	
	
	
	

}
