BPRPlus
==========

**BPRPlus**  是一个开源的Java库，专注于**推荐排序**问题，目标是为基于多种隐式反馈的各种BPR变形算法提供快速实现.

系统架构借鉴[LibRec](https://github.com/guoguibing/librec)，一个领先的推荐系统开源库.

|作者|刘臣|
|---|---
|E-mail|2422701543@qq.com, hdu.liuchen2020@gmail.com

*****

## 运行环境

* 1.Java 1.8：基础的Java环境
* 2.python3：调用python脚本完成结果可视化

*****
## BPRPlus主要特性如下

* 1.基于配置文件：通过配置文件控制系统的所有参数
* 2.并行计算：系统内部应用并行计算解决性能问题
* 3.支持多种隐式反馈：支持多种隐式反馈数据的读取
* 4.结果可视化：训练过程的loss可视化、评价指标可视化

<div style="text-align:center;"><img src="https://github.com/liuchenailq/lcrs/blob/master/resource/BPRPlus%E7%B3%BB%E7%BB%9F%E6%B5%81%E7%A8%8B.png" width="500" /> </div>

*****
## 代码示例

通过简单的几行代码，你就可以执行基本的BPR算法.
<pre>
public void main(String[] args) throws Exception {
       Configuration conf = new Configuration();
       conf.addResource("recommender/bpr.properties");
       RecommenderJob job = new RecommenderJob(conf);
       job.runJob();
}
</pre>



