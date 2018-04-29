package com.entity;

import java.util.HashMap;
import java.util.Set;

public class ItemRateCollect {
	public HashMap<Integer, ItemRate> itemCollect; //key:itemId;   value:ÿ��item�������û����ּ���
	
	public ItemRateCollect(){
		this.itemCollect = new HashMap<Integer, ItemRate>();
	}
	
	/** �ڶ�user��һ��item���ֵ�ƽ��ֵ */
	public void calcAvg(){
		Set<Integer> itemIdSet = this.itemCollect.keySet(); //itemCollect������keyֵ(��itemId)
		for(Integer itemId : itemIdSet){
			ItemRate itemRate = this.itemCollect.get(itemId); //��ȡitemId��Ӧ������ֵ��
			Set<Integer> userIdSet = itemRate.itemUserRate.keySet(); //����userId����
			int userIdCount = userIdSet.size();
			double avg = 0.0;
			for(Integer userId : userIdSet){
				double rate = itemRate.itemUserRate.get(userId); //��ȡuserId��Ӧ������ֵ
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
		public HashMap<Integer, Double> itemUserRate; //key: userId;   value:ÿ��user��ĳ��item������
		public Double avgRate;
		public ItemRate(){
			this.itemUserRate = new HashMap<Integer, Double>();
			this.avgRate = 0.0;
		}
		public void recordRate(Integer userId, Double rate){
			if(this.itemUserRate.containsKey(userId)){
				this.itemUserRate.remove(userId); //ɾ���ظ�userIdӳ���ֵ
			}
			this.itemUserRate.put(userId, rate); //��userIdӳ��rateֵ
		}
		
	}

}
