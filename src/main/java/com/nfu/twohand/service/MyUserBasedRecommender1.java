package com.nfu.twohand.service;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

public interface MyUserBasedRecommender1 {
  List<RecommendedItem> userBasedRecommender(long userID, int size);

}