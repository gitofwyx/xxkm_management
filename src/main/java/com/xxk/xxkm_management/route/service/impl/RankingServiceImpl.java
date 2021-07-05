package com.xxk.xxkm_management.route.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxk.xxkm_management.route.dao.RankingDao;
import com.xxk.xxkm_management.route.entity.Ranking;
import com.xxk.xxkm_management.route.service.RankingService;

@Service
public class RankingServiceImpl implements RankingService{

	@Autowired
	RankingDao dao;
	
	public boolean addRank(Ranking ranking){
		return dao.addRank(ranking) ==1 ? true : false;
	}
	public List<Ranking> listRankByRouteId(String routeId){
		return dao.listRankByRouteId(routeId);
		
	}
	
	@Override
	public Ranking getRankTop(String routeId) {
		List<Ranking> ranking=dao.getRankTop(routeId);
		if(ranking.size()!=1){
			return null;
		}
		return ranking.get(0);
	}
	@Override
	public List<Ranking> RankByCreateUser(String userId, String routeId) {
		return dao.RankByCreateUser(userId, routeId);
		
	}
}
