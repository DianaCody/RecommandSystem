package com.entity;

import java.util.HashMap;
import java.util.Set;

public class ItemRateCollect {
	public HashMap<Integer, ItemRate> itemCollect; //key:itemId;   value:每个item的所有用户评分集合
	
	public ItemRateCollect(){
		this.itemCollect = new HashMap<Integer, ItemRate>();
	}
	
	/** 众多user对一个item评分的平均值 */
	public void calcAvg(){
		Set<Integer> itemIdSet = this.itemCollect.keySet(); //itemCollect的所有key值(即itemId)
		for(Integer itemId : itemIdSet){
			ItemRate itemRate = this.itemCollect.get(itemId); //获取itemId对应的评分值类
			Set<Integer> userIdSet = itemRate.itemUserRate.keySet(); //返回userId集合
			int userIdCount = userIdSet.size();
			double avg = 0.0;
			for(Integer userId : userIdSet){
				double rate = itemRate.itemUserRate.get(userId); //获取userId对应的评分值
				avg += rate / userIdCount;
			}
			itemRate.avgRate = avg;
		}
	}
	
	public void recordRate(Integer itemId, Integer userId, Double rate){
		if(this.itemCollect.containsKey(itemId)){
			ItemRate itemRate = this.itemCollect.get(itemId);
			itemRate.recordRate(userId, rate);
		}
		else{
			ItemRate itemRate = new ItemRate();
			itemRate.recordRate(userId, rate);
			this.itemCollect.put(itemId, itemRate);
		}
	}
	
	public class ItemRate{
		public HashMap<Integer, Double> itemUserRate; //key: userId;   value:每个user对某个item的评分
		public Double avgRate;
		public ItemRate(){
			this.itemUserRate = new HashMap<Integer, Double>();
			this.avgRate = 0.0;
		}
		public void recordRate(Integer userId, Double rate){
			if(this.itemUserRate.containsKey(userId)){
				this.itemUserRate.remove(userId); //删除重复userId映射的值
			}
			this.itemUserRate.put(userId, rate); //用userId映射rate值
		}
		
	}

}
