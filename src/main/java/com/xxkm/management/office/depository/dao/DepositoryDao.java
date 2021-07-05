package com.xxkm.management.office.depository.dao;

import com.xxkm.management.office.depository.entity.Depository;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.stock.entity.Stock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface DepositoryDao {

    public List<Depository> listDepository(@Param("pageStart") int pageStart, @Param("pageSize") int pageSize, @Param("class_id") String class_id,
                                 @Param("entity_id") String entity_id, @Param("depository_officeId") String depository_officeId, @Param("search_type") int search_type);

    public List<Depository> selectDepositoryWithOfficeEnt(String entity_id,String depository_officeId);

    public int countDepository(String search_type);

    public List<Depository> getDepositoryByEntId(String entity_id);

    public int addDepository(Depository depository);

    public int updateDepository(Depository depository);

    public int deleteListRegUser(List<String> listStr);

    public List<String> getDepositoryIdByIdent(String ident);

    public Depository getDepositoryById(String id);

    public int recoveryDepository(Depository depository);

    public int transferDepositoryForDelivery(OfficesStorage storage);

    public int plusDepositoryNo(Depository depository);

    public int deploymentDeviceWithSingle(@Param("depository_id") String depository_id,
                                          @Param("updateUserId") String updateUserId,
                                          @Param("updateDate") String updateDate);

}
