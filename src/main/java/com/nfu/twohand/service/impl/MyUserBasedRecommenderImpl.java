package com.nfu.twohand.service.impl;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.nfu.twohand.service.MyUserBasedRecommender1;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MyUserBasedRecommenderImpl implements MyUserBasedRecommender1 {

	@Override
	public List<RecommendedItem> userBasedRecommender(long userID,int size) {
		// step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
		List<RecommendedItem> recommendations = null;
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName("localhost");//本地为localhost
			dataSource.setPort(3306);
			dataSource.setUser("root");
			dataSource.setPassword("123456");
			dataSource.setDatabaseName("twohand");//数据库名
			MySQLJDBCDataModel dataModel=new MySQLJDBCDataModel(dataSource,"score_good","user_id","good_id","score","time");


			//UserSimilarity similarity1=new UncenteredCosineSimilarity(model);
			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);//用PearsonCorrelation 算法计算用户相似度
			double v1 = similarity.userSimilarity(1L, 1L);
			double v2 = similarity.userSimilarity(1L, 2L);
			double v3 = similarity.userSimilarity(1L, 3L);
			double v4 = similarity.userSimilarity(1L, 4L);
			log.info(v1+"");log.info(v2+"");log.info(v3+"");log.info(v4+"");
			 UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);//计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
			Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(dataModel, neighborhood, similarity));//采用 CachingRecommender 为 RecommendationItem 进行缓存
			recommendations = recommender.recommend(userID, size);//得到推荐的结果，size是推荐结果的数目

			System.out.println("recommendations="+JSON.toJSONString(recommendations));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return recommendations;
	}



	/*public List<Integer> buyByUser(Integer userID,Integer bookId) {
		List<Integer> bookIdBypass=new ArrayList<>();
		try {
			List<ScoreBook> scoreBookList=scoreService.listByBook(bookId);
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&购买过该商品的用户个数"+scoreBookList.size());
			int cout=0;
			//for(Map.Entry<Integer,Double> mapping:list){//挑选出最相似的五个用户
			for(ScoreBook scoreBook:scoreBookList){//挑选出最相似的五个用户
				*//*cout++;
				if(cout>=5){
					break;
				}*//*

				List<ScoreBook> scoreBookListByUser=scoreService.listByUser(scoreBook.getUserId());
				System.out.println("&&&&&&&&&&根据挑选出的用户在挑选书籍"+scoreBook.getUserId()+" "+scoreBookListByUser.size());
				int cout1=0;
					for (ScoreBook scoreBook1: scoreBookListByUser) {//挑选出每个用户买过的五本评分最高的书
						*//*cout1++;
						if (cout1 >= 5) {
							break;
						}*//*
						if(scoreBook1.getBookId()==bookId){
							continue;
						}

							bookIdBypass.add(scoreBook1.getBookId());

					}
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("&&&&&&&&&&输出bookidpass集合………………………………………………………………………………");
		for(Integer o:bookIdBypass){
			System.out.println(o+"  ");

		}
		HashSet set = new HashSet(bookIdBypass);
       // 清空list集合
		bookIdBypass.clear();
// 将去重后的元素重新添加到list中
		bookIdBypass.addAll(set);
		System.out.println("&&&&&&&&&&最终书籍数量"+bookIdBypass.size());
		return bookIdBypass;
	}*/


}
