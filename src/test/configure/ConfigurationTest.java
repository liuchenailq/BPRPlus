package test.configure;

import org.junit.Test;

import main.configure.Configuration;

public class ConfigurationTest {

	@Test
	public void testAddDefaultResource() {
		Configuration conf = new Configuration();
		System.out.println(conf.get("data.column.format"));
	}
	
	@Test
	public void testAddResource() {
		Configuration conf = new Configuration();
		conf.addResource("bpr.properties");
		System.out.println(conf.get("rec.recommender.class"));
	} 
	
	@Test
	public void testSetStrings() {
		Configuration conf = new Configuration();
		conf.setStrings("test", "1");
		System.out.println(conf.get("test"));
	} 
	
	@Test
	public void testSetInt() {
		Configuration conf = new Configuration();
		conf.setInt("age", 11);
		int age = conf.getInt("age");
		assert age == 11;
	} 
	
	@Test
	public void testSetDouble() {
		Configuration conf = new Configuration();
		conf.setDouble("age", 11d);
		double age = conf.getDouble("age");
		assert age == 11d;
	} 
	
	@Test
	public void testSetBoolean() {
		Configuration conf = new Configuration();
		conf.setBoolean("test", true);
		boolean test = conf.getBoolean("test");
		assert test == true;
	} 
	
	
	
	

}
