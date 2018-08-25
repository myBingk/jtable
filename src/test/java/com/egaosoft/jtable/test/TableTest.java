package com.egaosoft.jtable.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.egaosoft.jtable.config.JudgmentType;
import com.egaosoft.jtable.core.Table;
import com.egaosoft.jtable.core.TableManager;
import com.egaosoft.jtable.exception.BusinessException;

/**
 * table对象测试用例
 * 
 * @author DoubleCome
 * @date 2018年7月2日 下午2:55:23
 */
public class TableTest extends TestCase {

	private List<BasicDictionary> basicDictionaryList = new ArrayList<BasicDictionary>();
	
	private Long[] dictionaryIdSet = new Long[2];
	
	@Before
	public void setUp() throws Exception {
		dictionaryIdSet[0] = 1L;
		dictionaryIdSet[1] = 2L;
	}

	@Test
	public void testSave() throws BusinessException {
		Assert.assertTrue(getTable().save(generate(4L)));
	}
	
	@Test
	public void testSaveAsGroup() throws BusinessException {
		BasicDictionary basicDictionaryE1 = generate(5L);
		BasicDictionary basicDictionaryE2 = generate(6L);
		basicDictionaryList.add(basicDictionaryE1);
		basicDictionaryList.add(basicDictionaryE2);
		Assert.assertTrue(getTable().saveAsGroup(basicDictionaryList));
	}
	
	@Test
	public void testDelete() throws BusinessException {
		Assert.assertTrue(getTable().delete(1L));
		 Assert.assertNull(getTable().findById(1L));
		
	}
	
	@Test
	public void testDeleteAsGroup() throws BusinessException {
		Assert.assertTrue(getTable().deleteAsGroup(dictionaryIdSet));
		Assert.assertEquals(0, getTable().findByIdAsGroup(dictionaryIdSet).size());
	}

	@Test
	public void testUpdate() throws BusinessException {
		BasicDictionary basicDictionary = new BasicDictionary().setDictionaryId(1L);
		Assert.assertTrue(getTable().update(basicDictionary.setName("修改过后")));
		basicDictionary = getTable().findById(basicDictionary.getDictionaryId());
		Assert.assertEquals("修改过后", basicDictionary.getName());
	}

	@Test
	public void testUpdateAsGroup() throws BusinessException {
		for (BasicDictionary basicDictionary : basicDictionaryList) {
			basicDictionary.setValue("123@qq.com");
		}
		Assert.assertTrue(getTable().updateAsGroup(basicDictionaryList));
		for (BasicDictionary basicDictionary : basicDictionaryList) {
			basicDictionary = getTable().findById(basicDictionary.getDictionaryId());
			Assert.assertEquals("123@qq.com", basicDictionary.getValue());
		}
	}

	@Test
	public void testFindById() throws BusinessException {
		Assert.assertNotNull(getTable().findById(1L));
		Assert.assertNotNull(getTable().findById(2L));
		Assert.assertNotNull(getTable().findById(3L));
	}

	@Test
	public void testFindByIdAsGroup() throws BusinessException {
		Assert.assertEquals(2, getTable().findByIdAsGroup(dictionaryIdSet).size());
	}
	
	@Test
	public void testFindByIdsAsGroup() throws BusinessException {
		Assert.assertEquals(2, getTable().findByIdsAsGroup(dictionaryIdSet).size());
	}

	@Test
	public void testFindList() throws BusinessException {
		Assert.assertNotNull(getTable().findList());
		Assert.assertEquals(10, getTable().findList().size());
	}

	@Test
	public void testFindListInPage() throws BusinessException {
		Assert.assertEquals(10, getTable().findListInPage(1, 10).getTotalRow());
		Assert.assertEquals(1, getTable().findListInPage(1, 1).getList().size());
		Assert.assertEquals(0, getTable().findListInPage(2, 10).getList().size());
	}

	@Test
	public void testFindListInPageWithKeywords() throws BusinessException {
		BasicDictionary keywords = new BasicDictionary();
		keywords.setValue("1");
		Assert.assertEquals(1, getTable().findListInPageWithKeywords(1, 10, keywords).getList().size());
		BasicDictionary keywords2 = new BasicDictionary();
		keywords2.setValue("2");
		keywords2.put(Table.getSortByKey(), "dictionaryId");
		keywords2.put(Table.getSortOrderKey(), "ASC");
		Assert.assertTrue(getTable().findListInPageWithKeywords(1, 10, keywords2).getList().get(0).getDictionaryId().equals(2L));
	}

	@Test
	public void testFindListInPageIntIntStringStringLong() throws BusinessException {
		Assert.assertNotNull(getTable().findListInPage(1, 10, "dictionaryId", "ASC"));
		Assert.assertTrue(1L == getTable().findListInPage(1, 10, "dictionaryId", "ASC").getList().get(0).getDictionaryId());
	}

	@Test
	public void testFindListInPageWithKeywordsIntIntStringStringLongBasicDictionaryLong() throws BusinessException {
		BasicDictionary keywords = new BasicDictionary();
		keywords.setName("1");
		keywords.setNameJudgmentType(JudgmentType.ALL_LIKE);
		keywords.setCreationTime(new Date());
		keywords.setCreationTimeJudgmentType(JudgmentType.LESS_THAN);
		Assert.assertTrue(1L == getTable().findListInPageWithKeywords(1, 10, keywords, "dictionaryId", "ASC").getList().get(0).getDictionaryId());
		keywords.setName("3");
		Assert.assertTrue(3L == getTable().findListInPageWithKeywords(1, 10, keywords, "dictionaryId", "DESC").getList().get(0).getDictionaryId());
	}
	
	@Test
	public void testNewInstanceClassOfQextendsModelOfQ() throws BusinessException {
		Table.newInstance(BasicDictionary.class);
	}

	@Test
	public void testSaveForAny() throws BusinessException {
		getTable().saveForAny("6", "6", "6", "6", "6", "6", "6", "6", "2018-4-28 16:20:30", "2018-4-28 16:20:30");
	}

	@Test
	public void testSaveTObjectArray() throws BusinessException {
		getTable().save(generate(8L), "8");
	}

	@Test
	public void testSaveTMapOfStringQ() throws BusinessException {
		getTable().save(generate(9L), generate(10L));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSaveTModelOfTArray() throws BusinessException {
		List<BasicDictionary> list = new ArrayList<BasicDictionary>();
		list.add(generate(11L));
		getTable().save(generate(10L), list);
	}

	@Test
	public void testSaveTListOfTArray() throws BusinessException {
		getTable().save(generate(12L), generate(13L));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSaveAsGroupForAny() throws BusinessException {

		List<Object> object1 = new ArrayList<Object>();
		object1.add("14");
		object1.add("14");
		object1.add("2018-4-28 16:20:30");
		object1.add("14");
		object1.add("14");
		object1.add("1");
		object1.add("14");
		object1.add("2018-4-28 16:20:30");
		object1.add("14");
		object1.add("14");
		
		List<Object> object2 = new ArrayList<Object>();
		object2.add("15");
		object2.add("15");
		object2.add("2018-4-28 16:20:30");
		object2.add("15");
		object2.add("15");
		object2.add("1");
		object2.add("15");
		object2.add("2018-4-28 16:20:30");
		object2.add("15");
		object2.add("15");
		
		getTable().saveAsGroupForAny(object1, object2);
	}

	@Test
	public void testSaveAsGroupListOfTObjectArray() throws BusinessException {
		getTable().saveAsGroup(generateList(16L));
	}

	@Test
	public void testSaveAsGroupListOfTMapOfStringQ() throws BusinessException {
		getTable().saveAsGroup(generateList(17L), "17");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSaveAsGroupListOfT() throws BusinessException {
		getTable().saveAsGroup(generateList(18L), generateList(19L));
	}

	@Test
	public void testSaveAsGroupListOfTListOfTArray() throws BusinessException {
		getTable().saveAsGroup(generateList(20L), generate(21L));
	}

	@Test
	public void testSaveAsGroupListOfTModelOfTArray() throws BusinessException {
		getTable().saveAsGroup(generateList(21L), generate(21L));
	}

	@Test
	public void testDeleteForAny() throws BusinessException {
		getTable().deleteForAny(generate(22L));
	}

	@Test
	public void testDeleteT() throws BusinessException {
		getTable().delete(generate(23L));
	}

	@Test
	public void testDeleteObjectArray() throws BusinessException {
		getTable().delete(24L);
	}

	@Test
	public void testDeleteAsGroupListOfT() throws BusinessException {
		getTable().deleteAsGroup(generateList(25L));
	}

	@Test
	public void testDeleteAsGroupObjectArrayArray() throws BusinessException {
		getTable().deleteAsGroup(new Long[]{26L});
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testDeleteAll() throws BusinessException {
		getTable().deleteAll();
	}

	@Test
	public void testUpdateT() throws BusinessException {
		getTable().update(generate(27L));
	}

	@Test
	public void testUpdateTMapOfStringQ() throws BusinessException {
		getTable().update(generate(28L), generate(29L));
	}

	@Test
	public void testUpdateTModelOfTArray() throws BusinessException {
		getTable().save(generate(30L), generate(31L), generate(32L));
		getTable().update(generate(30L), generate(31L), generate(32L));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testUpdateTListOfModelOfTArray() throws BusinessException {
		getTable().saveAsGroup(generateList(33L, 34L));
		getTable().update(generate(33L), generateList(34L));
	}

	@Test
	public void testUpdateAsGroupListOfT() throws BusinessException {
		getTable().saveAsGroup(generateList(35L));
		getTable().updateAsGroup(generateList(35L));
	}

	@Test
	public void testUpdateAsGroupListOfTMapOfStringQ() throws BusinessException {
		getTable().updateAsGroup(generateList(36L), generate(37L));
	}

	@Test
	public void testUpdateAsGroupListOfTModelOfTArray() throws BusinessException {
		getTable().saveAsGroup(generateList(38L, 39L, 40L));
		getTable().updateAsGroup(generateList(38L), generate(39L), generate(40L));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testUpdateAsGroupListOfTListOfModelOfTArray() throws BusinessException {
		getTable().saveAsGroup(generateList(41L, 42L, 43L));
		getTable().updateAsGroup(generateList(41L), generateList(42L), generateList(43L));
	}

	@Test
	public void testFindForAny() throws BusinessException {
		getTable().findForAny("44");
	}

	@Test
	public void testFindByCondition() throws BusinessException {
		getTable().find(generate(46L));
	}

	@Test
	public void testFindListModelOfT() throws BusinessException {
		getTable().findList(generate(47L));
	}
	
	@Test
	public void testDeleteModelModels() throws BusinessException {
		getTable().saveAsGroup(generateList(48L, 49L, 50L));
		getTable().delete(generate(48L), generate(49L), generate(50L));
		
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteModelModelLists() throws BusinessException {
		getTable().saveAsGroup(generateList(51L, 52L, 53L));
		getTable().delete(generate(51L), generateList(52L), generateList(53L));
		
	}
	
	@Test
	public void testDeleteAsGroupModelListModels() throws BusinessException {
		getTable().saveAsGroup(generateList(54L, 55L));
		getTable().deleteAsGroup(generateList(54L), generate(55L));
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteAsGroupModelListModelLists() throws BusinessException {
		getTable().saveAsGroup(generateList(56L, 57L, 58L));
		getTable().deleteAsGroup(generateList(56L), generateList(57L), generateList(58L));
		
	}

	public Table<BasicDictionary> getTable(){
		 Table<BasicDictionary> table = TableManager.get(BasicDictionary.class);
		 return table;
	}
	
	protected static Long tmpID = 0L;
	
	public synchronized static Long getUniqueId() throws BusinessException {
		long ltime = 0;
		ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(
				new Date()).toString()) * 10000;
		if (tmpID < ltime) {
			tmpID = ltime;
		} else {
			tmpID = tmpID + 1;
			ltime = tmpID;
		}
		return ltime;
	}

	protected BasicDictionary generate(Long value) {
		BasicDictionary basicDictionary = new BasicDictionary();
		basicDictionary.setDictionaryId(value);
		basicDictionary.setSystemId(1L);
		basicDictionary.setName(value.toString());
		basicDictionary.setValue(value.toString());
		basicDictionary.setCode(value.toString());
		basicDictionary.setDescription(value.toString());
		basicDictionary.setParentId(value);
		basicDictionary.setOrderBy(Integer.parseInt(value.toString()));
		basicDictionary.setCreationTime(new Date());
		basicDictionary.setUpdateTime(new Date());
		return basicDictionary;
	}

	protected List<BasicDictionary> generateList(Long... values) {
		List<BasicDictionary> list = new ArrayList<BasicDictionary>();
		for (Long value : values) {
			BasicDictionary basicDictionary = new BasicDictionary();
			basicDictionary.setDictionaryId(value);
			basicDictionary.setSystemId(1L);
			basicDictionary.setName(value.toString());
			basicDictionary.setValue(value.toString());
			basicDictionary.setCode(value.toString());
			basicDictionary.setDescription(value.toString());
			basicDictionary.setParentId(value);
			basicDictionary.setOrderBy(Integer.parseInt(value.toString()));
			basicDictionary.setCreationTime(new Date());
			basicDictionary.setUpdateTime(new Date());
			list.add(basicDictionary);
		}
		return list;
	}
	
}