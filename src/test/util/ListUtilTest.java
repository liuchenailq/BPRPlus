package test.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import main.util.KeyValue;
import main.util.ListUtil;

public class ListUtilTest {
	@Test
	public void testGetKeySet() {
		List<KeyValue<Integer, Double>> list = new ArrayList();
		list.add(new KeyValue<>(1,1.0));
		list.add(new KeyValue<>(2,2.0));
		list.add(new KeyValue<>(3,5.0));
		list.add(new KeyValue<>(4,-1.0));
		Set<Integer> set = ListUtil.getKeySet(list);
		System.out.println(set);
	}
	

	@Test
	public void testSortKeyValueListTopK() {
		List<KeyValue<Integer, Double>> list = new ArrayList();
		list.add(new KeyValue<>(1,1.0));
		list.add(new KeyValue<>(2,2.0));
		list.add(new KeyValue<>(3,5.0));
		list.add(new KeyValue<>(4,-1.0));
		list = ListUtil.sortKeyValueListTopK(list, true, 2);
		for(KeyValue<Integer, Double> entry: list) {
			System.out.println(entry.getKey() +" "+ entry.getValue());
		}
	}

}
