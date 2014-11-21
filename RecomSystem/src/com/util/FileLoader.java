package com.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.entity.ItemRateCollect;
import com.entity.UserRateCollect;
import com.test.TestDataEntity;

public class FileLoader {
	public static UserRateCollect readUserTrainData() throws IOException, FileNotFoundException {
		UserRateCollect userRateCollect = new UserRateCollect();
		String dataPath = "res/train.txt";
		FileReader fr = new FileReader(dataPath);
		BufferedReader br = new BufferedReader(fr);
		String recordLine;
		while((recordLine=br.readLine()) != null){ 
			String[] recordArray = recordLine.split("\t");
			if(recordArray.length == 4){
				Integer userId = Integer.parseInt(recordArray[0]);
				Integer itemId = Integer.parseInt(recordArray[1]);
				Integer rateInt = Integer.parseInt(recordArray[2]);
				Double rate = rateInt.doubleValue();
				userRateCollect.recordRate(userId, itemId, rate);
			}
		}
		userRateCollect.calcAvg(); //单个user对所有item评分的平均值
		return userRateCollect;
	}
	
	public static ItemRateCollect readItemTrainData() throws IOException, FileNotFoundException {
		ItemRateCollect itemRateCollect = new ItemRateCollect();
		String dataPath = "res/train.txt";
		FileReader fr = new FileReader(dataPath);
		BufferedReader br = new BufferedReader(fr);
		String recordLine;
		while((recordLine=br.readLine()) != null){
			String[] recordArray = recordLine.split("\t");
			if(recordArray.length == 4){
				Integer userId = Integer.parseInt(recordArray[0]);
				Integer itemId = Integer.parseInt(recordArray[1]);
				Integer rateInt = Integer.parseInt(recordArray[2]);
				Double rate = rateInt.doubleValue();
				itemRateCollect.recordRate(itemId, userId, rate);
			}
		}
		itemRateCollect.calcAvg(); //众多user对一个item评分的平均值
		return itemRateCollect;
	}
	
	public static List<TestDataEntity> readTestData() throws IOException, FileNotFoundException {
		List<TestDataEntity> testdata = new ArrayList<TestDataEntity>();
		String dataPath = "res/test.txt";
		FileReader fr = new FileReader(dataPath);
		BufferedReader br = new BufferedReader(fr);
		String recordLine;
		while((recordLine=br.readLine()) != null){
			String[] recordArray = recordLine.split("\t");
			if(recordArray.length == 4){
				TestDataEntity onetestdata = new TestDataEntity();
				onetestdata.userId = Integer.parseInt(recordArray[0]);
				onetestdata.itemId = Integer.parseInt(recordArray[1]);
				Integer rateInt = Integer.parseInt(recordArray[2]);
				onetestdata.realRate = rateInt.doubleValue();
				testdata.add(onetestdata); //增加一个元素
			}
		}
		return testdata;
		
	}

}
