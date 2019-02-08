package main.data.convert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import main.data_structure.DataFrame;
import main.data_structure.Rating;

public class TextDataConvert extends AbstractDataConvert{
	
	/** 数据列格式 UIR、UIRT**/
	private String dataColumnFormat;
	/** 分隔符 */
	private String sep;
	/** 数据文件路径 */
	private String filePath;
	/** 阈值 */
	private double binThold;
	
	

	public TextDataConvert(String dataColumnFormat, String sep, String filePath, double binThold) {
		dataFrame = new DataFrame();
		this.dataColumnFormat = dataColumnFormat;
		this.sep = sep;
		this.filePath = filePath;
		this.binThold = binThold;
	}

	@Override
	public void processData() {
		LOG.info("开始读取主数据[" + filePath + "]");
		try (FileReader reader = new FileReader(filePath); 
			 BufferedReader buffer = new BufferedReader(reader)){
			String temp;
			while((temp = buffer.readLine()) != null) {
				if ("".equals(temp.trim())) {
                    break;
                }
				String[] row = temp.split(sep);
				int userId = dataFrame.setUserId(row[0]);
				int itemId = dataFrame.setItemId(row[1]);
				double score = Double.valueOf(row[2]);
				//二值化评分
				if(binThold != -1d) {
					score = score > binThold ? 1 : 0;
				}
				if("UIR".equals(dataColumnFormat)) {
					dataFrame.addRating(new Rating(userId, itemId, score));
				}else {
					if("UIRT".equals(dataColumnFormat)) {
						dataFrame.addRating(new Rating(userId, itemId, score, Long.valueOf(row[3])));
					}else {
						LOG.error(dataColumnFormat + "是不支持的数据列格式！");
						System.exit(1);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		LOG.info("主数据读取完毕！[" + filePath + "]");
		LOG.info("用户数：" + getUserCount() +"， 物品数：" + getItemCount());
	}

}
