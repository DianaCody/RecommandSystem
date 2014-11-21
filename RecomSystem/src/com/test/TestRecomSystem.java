package com.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.base.ItemBaseCF;
import com.base.ItemBaseCFChanged;
import com.entity.ItemRateCollect;
import com.entity.UserRateCollect;
import com.util.FileLoader;
import com.util.RecomMethodInterface;

public class TestRecomSystem {
	public static void main(String[] args) throws IOException, FileNotFoundException {
		UserRateCollect userData = FileLoader.readUserTrainData();
		ItemRateCollect itemData = FileLoader.readItemTrainData();
		List<TestDataEntity> testData = FileLoader.readTestData();
		System.out.println("Load data finished...\n");
		
		System.out.println("(1) Original item-based CF:");
		RecomMethodInterface recomMethod1 = new ItemBaseCF(30);
		TestRecomMethod test1 = new TestRecomMethod(testData, userData, itemData, recomMethod1);
		double mae = test1.getMae();
		double rmse = test1.getRmse();
		System.out.println("平均绝对误差MAE: " + mae);
		System.out.println("均方根误差RMSE: " + rmse);
		
		System.out.println("\n(2) item-based CF after corrected:");
		RecomMethodInterface recomMethod2 = new ItemBaseCFChanged(30);
		TestRecomMethod test2 = new TestRecomMethod(testData, userData, itemData, recomMethod2);
		mae = test2.getMae();
		rmse = test2.getRmse();
		System.out.println("校正后平均绝对误差MAE: " + mae);
		System.out.println("校正后均方根误差RMSE: " + rmse);
	}

}
