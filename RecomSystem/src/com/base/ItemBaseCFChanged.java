package com.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.entity.ItemRateCollect;
import com.entity.ItemRateCollect.ItemRate;
import com.entity.SimItem;
import com.entity.UserRateCollect;
import com.util.ItemSimDegree;
import com.util.RecomMethodInterface;

/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * @filename ItemBaseCFChanged.java
 * @version 2.0
 * @note ���ݲ�ͬ��׼�ֱ���дpredictionUserItemRate�ļ̳нӿڷ���,��ΪItem-Based-Change����д.
 *       (ע:1."ԭʼ����": Item-Based CF,��ItemBaseCF.java��getSimDegree()����
 *           2."�Ľ���": Item-Based CF(changed),�����ƶ�similarity,�����û���������"����ϵ��")
 * @author DianaCody
 * @since 2014-11-20 08:02:09
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class ItemBaseCFChanged implements RecomMethodInterface{
	//ע��������item-Based,��topK item! ������topK�ھ��û�(����user-Based)!
	public int topKCount = 30; //�û���ϲ����topK item"��Ʒ"����
	
	public ItemBaseCFChanged(int topKCount){
		this.topKCount = topKCount;
	}
	
	/** ��дpredictionUserItemRate�ӿڷ��� */
	@Override
	public double predictionUserItemRate(UserRateCollect trainUserData, ItemRateCollect collect, Integer userId, Integer itemId) {
		ItemRate itemRate = collect.itemCollect.get(itemId); //��ȡ��itemId������ֵ��
		if(itemRate == null){ //û��user��Ŀ��item��������,��ȡĿ��user�������������ֵ�ƽ��ֵΪĿ��user��Ŀ��item������
			if(trainUserData.userCollect.containsKey(userId))
				return trainUserData.userCollect.get(userId).avgRate;
			else
				return 3.0; //��Ŀ��userδ�����κ�����,������3.0
		}
		if(itemRate.itemUserRate.containsKey(userId)){
			return itemRate.itemUserRate.get(userId);
		}
		
		double fenzi = 0.0;
		double fenmu = 0.0;
		List<SimItem> similarItemList = getSimilarItemsFromTopKUser(collect, itemId, userId);
		for(SimItem simItem : similarItemList){
			double similarItemAvgRate = collect.itemCollect.get(simItem.itemId).avgRate;
			double similarUserItemRate = collect.itemCollect.get(simItem.itemId).itemUserRate.get(userId);
			fenzi += simItem.similarDegree * (similarUserItemRate-similarItemAvgRate);
			fenmu += Math.abs(simItem.similarDegree);
		}
		if(similarItemList==null || similarItemList.size()==0){
			return collect.itemCollect.get(itemId).avgRate;
		}
		if(fenmu == 0){
			return collect.itemCollect.get(itemId).avgRate;
		}
		return collect.itemCollect.get(itemId).avgRate + (fenzi / fenmu);
	}
	
	
	/** �õ�����SimilarItem�б� --from TopK������Ʒ */
	public List<SimItem> getSimilarItemsFromTopKUser(ItemRateCollect collect, Integer itemId, Integer userId){
		List<SimItem> items = new ArrayList<SimItem>(); //�������item�ļ���
		SimItem[] topKSimilarItem = new SimItem[topKCount]; //���k������item�ļ���
		int neighbourCount = 0;
		Set<Integer> itemSet = collect.itemCollect.keySet(); //��ȡ����itemId
		for(Integer eachItem : itemSet){
			//Ŀ��item�����ÿһ��item�����ֶ�����
			if(eachItem.equals(itemId)==false && collect.itemCollect.get(eachItem).itemUserRate.containsKey(userId)){
				double itemSimDegree = ItemSimDegree.getSimDegreeChanged(collect, itemId, eachItem);
				if(neighbourCount < topKCount){
					insertTopKArray(topKSimilarItem, eachItem, itemSimDegree, neighbourCount);
					neighbourCount++;
				}
				else{
					insertTopKArray(topKSimilarItem, eachItem, itemSimDegree, topKSimilarItem.length);
				}
			}
		}
		for(int i=0; i<neighbourCount; i++){
			items.add(topKSimilarItem[i]);
		}
		return items;
	}
	
	private static void insertTopKArray(SimItem[] topKSimilarItem, Integer itemId, Double simDegree, int nearestCount){
		int nearestMaxIndex = 0;
		if(nearestCount < topKSimilarItem.length){
			nearestMaxIndex = nearestCount;
		}
		else{
			if(topKSimilarItem[topKSimilarItem.length-1].similarDegree >= simDegree)
				return;
			nearestMaxIndex = topKSimilarItem.length - 1;
		}
		SimItem oneSimilarItem = new SimItem();
		oneSimilarItem.itemId = itemId;
		oneSimilarItem.similarDegree = simDegree;
		topKSimilarItem[nearestMaxIndex] = oneSimilarItem;
		for(int i=nearestMaxIndex; i>0; i--){ //ð������...
			if(topKSimilarItem[i].similarDegree > topKSimilarItem[i-1].similarDegree){
				SimItem tempSimilarItem = topKSimilarItem[i];
				topKSimilarItem[i] = topKSimilarItem[i-1];
				topKSimilarItem[i-1] = tempSimilarItem;
			}
		}
	}
}
