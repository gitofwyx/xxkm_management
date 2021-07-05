package com.kelan.riding.route.dao;

import java.util.List;

import com.kelan.riding.route.entity.Ranking;


public interface RankingDao {
	
	//添加挑战者记录
	int addRank(Ranking ranking);
		
	//获取挑战者记录，返回前十
	List<Ranking> listRankByRouteId(String routeId);
	
	//根据挑战者id和线路id返回相关数据
	Ranking getRankByUserAndRoute(String userId, String routeId);
		
	List<Ranking> getRankTop(String routeId);
	
	List<Ranking> RankByCreateUser(String userId,String routeId);
}
