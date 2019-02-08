package main.data.convert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import main.data_structure.DataFrame;
import main.data_structure.Rating;

public class TextDataConvert extends AbstractDataConvert{
	
	/** �����и�ʽ UIR��UIRT**/
	private String dataColumnFormat;
	/** �ָ��� */
	private String sep;
	/** �����ļ�·�� */
	private String filePath;
	/** ��ֵ */
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
		LOG.info("��ʼ��ȡ������[" + filePath + "]");
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
				//��ֵ������
				if(binThold != -1d) {
					score = score > binThold ? 1 : 0;
				}
				if("UIR".equals(dataColumnFormat)) {
					dataFrame.addRating(new Rating(userId, itemId, score));
				}else {
					if("UIRT".equals(dataColumnFormat)) {
						dataFrame.addRating(new Rating(userId, itemId, score, Long.valueOf(row[3])));
					}else {
						LOG.error(dataColumnFormat + "�ǲ�֧�ֵ������и�ʽ��");
						System.exit(1);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		LOG.info("�����ݶ�ȡ��ϣ�[" + filePath + "]");
		LOG.info("�û�����" + getUserCount() +"�� ��Ʒ����" + getItemCount());
	}

}
