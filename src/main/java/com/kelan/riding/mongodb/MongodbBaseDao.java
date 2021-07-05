package com.kelan.riding.mongodb;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;

import com.mongodb.WriteResult;

public abstract class MongodbBaseDao<T> {
	@Autowired
	protected MongoTemplate mongoTemplate;
	@Autowired
	protected SpringDataPageable pageable;
	private static Logger log = Logger.getLogger(MongodbBaseDao.class);
	
	public void save(T t) {
		this.mongoTemplate.save(t);
	}

	public void delete(T t){
        log.info("[Mongo Dao ]delete:" + t);
        this.mongoTemplate.remove(t);
    }
	
	public WriteResult upsert(Query query, Update update) {
		log.info("[Mongo Dao ]upsert:query(" + query + "),update(" + update + ")");
		return this.mongoTemplate.upsert(query, update, this.getEntityClass());
	}

	public void updateFirst(Query query, Update update) {
		log.info("[Mongo Dao ]updateFirst:query(" + query + "),update(" + update + ")");
		this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
	}

	public void updateMulti(Query query, Update update) {
		log.info("[Mongo Dao ]updateMulti:query(" + query + "),update(" + update + ")");
		this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
	}

	public T query(Query query) {
		//log.info("[Mongo Dao ]queryById:" + query);
		return this.mongoTemplate.findOne(query, this.getEntityClass());
	}
	
	public List<T> queryList(Query query) {
		log.info("[Mongo Dao ]queryById:" + query);
		return this.mongoTemplate.find(query, this.getEntityClass());
	}
	
	public Page<T> getPage(Query query, int start, int size,Sort sort) {
		// 开始页
		pageable.setPagenumber(start);
		// 每页条数
		pageable.setPagesize(size);
		// 排序
		pageable.setSort(sort);
		// 查询出一共的条数
		Long count = this.mongoTemplate.count(query, this.getEntityClass());
		// 查询
		List<T> list = this.mongoTemplate.find(query.with(pageable), this.getEntityClass());
		log.info("查询出一共的条数"+ count);
		// 将集合与分页结果封装
		Page<T> pagelist = new PageImpl<T>(list, pageable, count);
		return  pagelist;
	}

	protected abstract Class<T> getEntityClass();
}
