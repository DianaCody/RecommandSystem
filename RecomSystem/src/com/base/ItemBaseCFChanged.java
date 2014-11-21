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
 * @note 根据不同标准分别重写predictionUserItemRate的继承接口方法,此为Item-Based-Change的重写.
 *       (注:1."原始方法": Item-Based CF,见ItemBaseCF.java的getSimDegree()方法
 *           2."改进的": Item-Based CF(changed),对相似度similarity,基于用户数量计算"调整系数")
 * @author DianaCody
 * @since 2014-11-20 08:02:09
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class ItemBaseCFChanged implements RecomMethodInterface{
	//注意这里是item-Based,是topK item! 而不是topK邻居用户(属于user-Based)!
	public int topKCount = 30; //用户最喜欢的topK item"商品"数量
	
	public ItemBaseCFChanged(int topKCount){
		this.topKCount = topKCount;
	}
	
	/** 重写predictionUserItemRate接口方法 */
	@Override
	public double predictionUserItemRate(UserRateCollect trainUserData, ItemRateCollect collect, Integer userId, Integer itemId) {
		ItemRate itemRate = collect.itemCollect.get(itemId); //获取对itemId的评分值类
		if(itemRate == null){ //没有user对目标item做过评分,则取目标user对所做过的评分的平均值为目标user对目标item的评分
			if(trainUserData.userCollect.containsKey(userId))
				return trainUserData.userCollect.get(userId).avgRate;
			else
				return 3.0; //若目标user未做过任何评分,则评分3.0
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
	
	
	/** 得到相似SimilarItem列表 --from TopK评分商品 */
	public List<SimItem> getSimilarItemsFromTopKUser(ItemRateCollect collect, Integer itemId, Integer userId){
		List<SimItem> items = new ArrayList<SimItem>(); //存放相似item的集合
		SimItem[] topKSimilarItem = new SimItem[topKCount]; //存放k个相似item的集合
		int neighbourCount = 0;
		Set<Integer> itemSet = collect.itemCollect.keySet(); //获取所有itemId
		for(Integer eachItem : itemSet){
			//目标item以外的每一个item的评分都存在
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
		for(int i=nearestMaxIndex; i>0; i--){ //冒泡排序...
			if(topKSimilarItem[i].similarDegree > topKSimilarItem[i-1].similarDegree){
				SimItem tempSimilarItem = topKSimilarItem[i];
				topKSimilarItem[i] = topKSimilarItem[i-1];
				topKSimilarItem[i-1] = tempSimilarItem;
			}
		}
	}
}
