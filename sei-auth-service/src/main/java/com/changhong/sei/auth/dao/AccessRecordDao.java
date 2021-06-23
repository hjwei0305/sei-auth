package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.dto.AccessRecordFeatureResponse;
import com.changhong.sei.auth.dto.AccessRecordUserResponse;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 访问记录(AccessRecord)表数据库访问层
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@Repository
public interface AccessRecordDao extends BaseEntityDao<AccessRecord> {

    /**
     * 指定时间段访问top N的功能
     */
    @Query("SELECT new com.changhong.sei.auth.dto.AccessRecordFeatureResponse(MAX(a.appModule),MAX(a.feature),a.path,COUNT(a.path),MAX(a.accessTime)) " +
            "FROM AccessRecord a WHERE a.tenantCode = :tenant AND a.accessTime >= :time GROUP BY a.path ORDER BY COUNT(a.path) DESC ")
    List<AccessRecordFeatureResponse> getTopFeatures(@Param("tenant") String tenant, @Param("time") LocalDateTime time, Pageable pageable);

    /**
     * 指定时间段访问top N的功能
     */
    @Query("SELECT new com.changhong.sei.auth.dto.AccessRecordFeatureResponse(MAX(a.appModule),MAX(a.feature),a.path,COUNT(a.path),MAX(a.accessTime)) " +
            "FROM AccessRecord a WHERE a.tenantCode = :tenant AND a.type = :type AND a.accessTime >= :time GROUP BY a.path ORDER BY COUNT(a.path) DESC ")
    List<AccessRecordFeatureResponse> getTopFeatures(@Param("tenant") String tenant, @Param("type") String type, @Param("time") LocalDateTime time, Pageable pageable);

    /**
     * 指定时间段访问top N的人
     */
    @Query("SELECT new com.changhong.sei.auth.dto.AccessRecordUserResponse(MAX(a.userId),a.userAccount,MAX(a.userName),COUNT(a.userAccount),MAX(a.accessTime)) " +
            "FROM AccessRecord a WHERE a.tenantCode = :tenant AND a.accessTime >= :time GROUP BY a.userAccount ORDER BY COUNT(a.userAccount) DESC ")
    List<AccessRecordUserResponse> getTopUsers(@Param("tenant") String tenant, @Param("time") LocalDateTime time, Pageable pageable);

    /**
     * 指定时间段访问top N的人
     */
    @Query("SELECT new com.changhong.sei.auth.dto.AccessRecordUserResponse(MAX(a.userId),a.userAccount,MAX(a.userName),COUNT(a.userAccount),MAX(a.accessTime)) " +
            "FROM AccessRecord a WHERE a.tenantCode = :tenant AND a.type = :type AND a.accessTime >= :time GROUP BY a.userAccount ORDER BY COUNT(a.userAccount) DESC ")
    List<AccessRecordUserResponse> getTopUsers(@Param("tenant") String tenant, @Param("type") String type, @Param("time") LocalDateTime time, Pageable pageable);

    /**
     * 指定时间段某一功能访问的人
     */
    @Query("SELECT new com.changhong.sei.auth.dto.AccessRecordUserResponse(MAX(a.userId),a.userAccount,MAX(a.userName),COUNT(a.userAccount),MAX(a.accessTime)) " +
            "FROM AccessRecord a WHERE a.tenantCode = :tenant AND a.feature = :feature AND a.accessTime >= :time GROUP BY a.userAccount ORDER BY a.userAccount DESC ")
    List<AccessRecordUserResponse> getUsersByFeature(@Param("tenant") String tenant, @Param("feature") String feature, @Param("time") LocalDateTime time, Pageable pageable);

    /**
     * 指定时间段某人访问的功能
     */
    @Query("SELECT new com.changhong.sei.auth.dto.AccessRecordFeatureResponse(MAX(a.appModule),MAX(a.feature),a.path,COUNT(a.path),MAX(a.accessTime)) " +
            "FROM AccessRecord a WHERE a.tenantCode = :tenant AND a.userAccount = :account AND a.accessTime >= :time GROUP BY a.path ORDER BY a.path DESC ")
    List<AccessRecordFeatureResponse> getFeaturesByUser(@Param("tenant") String tenant, @Param("account") String account, @Param("time") LocalDateTime time, Pageable pageable);

}