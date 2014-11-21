package com.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.base.ItemBaseCF;
import com.entity.ItemRateCollect;
import com.entity.UserRateCollect;
import com.util.FileLoader;
import com.util.RecomMethodInterface;

public class TestRecomSystem {
	public static void main(String[] args) throws IOException, FileNotFoundException {
		UserRateCollect userData = FileLoader.readUserTrainData();
		ItemRateCollect itemData = FileLoader.readItemTrainData();
		List<TestDataEntity> testData = FileLoader.readTestData();
		System.out.println("Load data finished...");
		RecomMethodInterface recomMethod = new ItemBaseCF(20);
		TestRecomMethod test = new TestRecomMethod(testData, userData, itemData, recomMethod);
		double mae = test.getMae();
		double rmse = test.getRmse();
		System.out.println("平均绝对误差MAE: " + mae);
		System.out.println("均方根误差RMSE: " + rmse);
	}

}
