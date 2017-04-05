
## 个性化用户推荐系统

------

### * Personal Recommand System (Item-Based CF) *
#### * 用户推荐系统 *
##### 1. 基于商品的协同过滤（Item-Based CF）

> * 基于商品Item-Based CF, 保证在数据稀疏性的情况下, 减小算法扩展性差的特点（如果仅基于用户的协同过滤 User-Based CF）
> * 调整的余弦相似度计算, 基于关联计算相似度similarity, 并添加系数调整因子factor
> * “加权求和”的预测打分值计算
> * TopK商品item推荐
> * 数据： 训练集80000,测试集20000

##### 2. 模型结果

![file-list](https://github.com/DianaCody/RecommandSystem/blob/master/RecomSystem/icon/result.PNG)
