package com.util;

import java.util.Set;
import com.entity.ItemRateCollect;
import com.entity.ItemRateCollect.ItemRate;

/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * @filename ItemSimDegree.java
 * @version  1.0
 * @note     Calculate item similarity:
 *              ----getSimDegree(); ----��item1,item2��������
 * @author   DianaCody
 * @since    2014-11-16 15:23:28
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class ItemSimDegree {
	/** ��item1,item2�������� */
	public static double getSimDegree(ItemRateCollect collect, Integer itemId1, Integer itemId2){
		if(itemId1.equals(itemId2)){
			return 1.0;
		}
		
		double item1AvgRate = collect.itemCollect.get(itemId1).avgRate; //��Ӧitem1���������ֵľ�ֵ
		double item2AvgRate = collect.itemCollect.get(itemId2).avgRate;
		
		if(collect.itemCollect.containsKey(itemId1)==false || collect.itemCollect.containsKey(itemId2)==false){
			return 0.0;
		}
		
		ItemRate item1Rate = collect.itemCollect.get(itemId1); //��itemid1��Ӧ������ֵ��
		ItemRate item2Rate = collect.itemCollect.get(itemId2);
		Set<Integer> userIdSet1 = item1Rate.itemUserRate.keySet(); //���ض�item1�����ֵ�userid
		Set<Integer> userIdSet2 = item2Rate.itemUserRate.keySet();
		double fenzi = 0.0;
		double fenmu1 = 0.0;
		double fenmu2 = 0.0;
		
		for(Integer userId1 : userIdSet1){ //ȡ���ж�item1�й�������
			double item1UserRate = item1Rate.itemUserRate.get(userId1); //ÿ��user��item1������
			if(userIdSet2.contains(userId1)){
				double item2UserRate = item2Rate.itemUserRate.get(userId1); //ÿһ��item1�����۵�user��Item2������
				fenzi += (item1UserRate-item1AvgRate) * (item2UserRate-item2AvgRate);
			}
			fenmu1 += (item1UserRate-item1AvgRate) * (item1UserRate-item1AvgRate);
		}
		
		for(Integer userId2 : userIdSet2){ //ȡ���ж�item2�й�������
			double item2UserRate = item2Rate.itemUserRate.get(userId2);
			fenmu2 += (item2UserRate-item2AvgRate) * (item2UserRate-item2AvgRate);
		}
		
		if(fenmu1==0 && fenmu2==0){
			if(item1AvgRate == item2AvgRate)
				return 0.0;
			else
				return 0.0;
		}
		else if(fenmu1==0 || fenmu2==0){
			return 0.0;
		}
		double fenmu = Math.sqrt(fenmu1) * Math.sqrt(fenmu2);
		return fenzi / fenmu;
	}

}
