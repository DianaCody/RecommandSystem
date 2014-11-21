package com.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.entity.ItemRateCollect;
import com.entity.UserRateCollect;
import com.util.RecomMethodInterface;

public class TestRecomMethod {
	public List<Double> preRealDiffList;
	public TestRecomMethod(){
		this.preRealDiffList = new ArrayList<Double>();
	}
	
	public TestRecomMethod(List<TestDataEntity> testDataList, UserRateCollect trainUserData, 
			ItemRateCollect trainItemData, RecomMethodInterface recomMethod){
		long beginTime = getCurrentTime();
		
		this.preRealDiffList = new ArrayList<Double>();
		for(TestDataEntity testData : testDataList){
			double preditRate = recomMethod.predictionUserItemRate(trainUserData, trainItemData, testData.userId, testData.itemId);
			this.preRealDiffList.add(Math.abs(preditRate - testData.realRate));
		}
		
		long endTime = getCurrentTime();
		double duringTime = 1.0 * (endTime - beginTime) / 1000.0;
		System.out.println("计算时间" + duringTime + "秒");
	}
	
	/** 计算平均绝对误差 */
	public double getMae(){
		double mae = 0.0;
		for(Double diff : this.preRealDiffList){
			mae += diff;
		}
		return mae / this.preRealDiffList.size();
	}
	
	/** 计算均方根误差 */
	public double getRmse(){
		double rmse = 0.0;
		for(Double diff : this.preRealDiffList){
			rmse += diff * diff;
		}
		return Math.sqrt(rmse / this.preRealDiffList.size());
	}
	
	private static long getCurrentTime(){
		Date currentDay = new Date();
		long longTime = currentDay.getTime();
		return longTime;
	}

}
