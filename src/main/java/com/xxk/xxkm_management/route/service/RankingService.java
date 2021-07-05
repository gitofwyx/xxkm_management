package com.xxk.xxkm_management.route.service;

import java.util.List;

import com.xxk.xxkm_management.route.entity.Ranking;

public interface RankingService {
		boolean addRank(Ranking ranking);
		
		List<Ranking> listRankByRouteId(String routeId);

		Ranking getRankTop(String routeId);
		
		List<Ranking> RankByCreateUser(String userId,String routeId);
}
