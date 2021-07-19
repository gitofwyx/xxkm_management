package com.xxkm.management.system.bar_code.dao;

import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.system.bar_code.entity.Bar_code;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface Bar_codeDao {

    public List<Bar_code> listDevice(int pageStart, int pageSize);

    public int countDevice();

    public int addBar_code(Bar_code bar_code);

}
