package com.util;

import com.entity.ItemRateCollect;
import com.entity.UserRateCollect;

public interface RecomMethodInterface {
	public double predictionUserItemRate(UserRateCollect trainUserData, 
			ItemRateCollect trainItemData, Integer userId, Integer itemId);

}
