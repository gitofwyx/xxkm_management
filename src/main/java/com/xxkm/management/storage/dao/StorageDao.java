package com.xxkm.management.storage.dao;

import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.storage.entity.Storage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface StorageDao {

    public List<Storage> listStorage(int pageStart, int pageSize);

    public int countStorage();

    public List<Storage> listStorageByStock(@Param("pageStart") int pageStart,
                                              @Param("pageSize") int pageSize,
                                              @Param("class_id") String class_id,
                                              @Param("entity_id") String entity_id,
                                              @Param("stock_id") String stock_id,
                                              @Param("officeId") String officeId);

    public int addStorage(Storage storage);

    public int addStorageForOfficesStorage(OfficesStorage storage);

    public int deleteListRegUser(List<String> listStr);

    public List<String> getStorageIdByIdent(String ident);

}
