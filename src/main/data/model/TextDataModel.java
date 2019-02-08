package main.data.model;

import main.configure.Configuration;
import main.data.convert.TextDataConvert;

public class TextDataModel extends AbstractDataModel{
	
	public TextDataModel() {
		
	}

	public TextDataModel(Configuration conf) {
		this.conf = conf;
	}
	
	@Override
	public void buildDataConvert() {
		String dataColumnFormat = conf.get("data.column.format");
		String sep = conf.get("data.convert.sep");
		String filePath = System.getProperty("user.dir") + "/" +conf.get("data.input.filepath");
		double binThold = conf.getDouble("data.convert.binarize.threshold");
		dataConvert = new TextDataConvert(dataColumnFormat, sep, filePath, binThold);
		dataConvert.processData();
//		System.out.println(dataConvert.dataFrame);
//		System.out.println(dataConvert.dataFrame.getItemMapping().toString());
//		System.out.println(dataConvert.dataFrame.getUserMapping().toString());
	}
	

	

}
