package com.entity;

import java.util.HashMap;
import java.util.Set;

public class UserRateCollect {
	public HashMap<Integer, UserRate> userCollect;
	
	public UserRateCollect(){
		this.userCollect = new HashMap<Integer, UserRate>();
	}
	
	/** ����user������item���ֵ�ƽ��ֵ */
	public void calcAvg(){
		Set<Integer> userIdSet = this.userCollect.keySet(); //userCollect������keyֵ(��userId)
		for(Integer userId : userIdSet){
			UserRate userRate = this.userCollect.get(userId); //ÿ��userId��Ӧ�����ּ���
			int itemCount = userRate.userItemRate.size(); //item����
			double avg = 0.0;
			Set<Integer> itemIdSet = userRate.userItemRate.keySet(); //userItemRate������keyֵ(��itemId)
			for(Integer itemId : itemIdSet){
				double rate = userRate.userItemRate.get(itemId); //��ÿ��item������(ÿ��itemId����Ӧ��)
				avg += rate / itemCount;
			}
			userRate.avgRate = avg;
		}
	}
	
	public void recordRate(Integer userId, Integer itemId, Double rate){
		if(this.userCollect.containsKey(userId)){
			UserRate userRate = this.userCollect.get(userId);
			userRate.recordRate(itemId, rate);
		}
		else{
			UserRate userRate = new UserRate();
			userRate.recordRate(itemId, rate);
			this.userCollect.put(userId, userRate); //����userId��Ӧ��userRate
		}
	}
	
	public class UserRate{
		public HashMap<Integer, Double> userItemRate;
		public Double avgRate;
		public UserRate(){
			this.userItemRate = new HashMap<Integer, Double>();
			this.avgRate = 0.0;
		}
		public void recordRate(Integer itemId, Double rate){
			if(this.userItemRate.containsKey(itemId)){
				this.userItemRate.remove(itemId);
			}
			 //get rate score of item from itemId
			this.userItemRate.put(itemId, rate);
		}
	}

}
