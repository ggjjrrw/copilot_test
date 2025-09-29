package com.example.myhome.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础Repository接口
 * 提供通用的数据访问方法
 *
 * @param <T>  实体类型
 * @param <ID> 主键类型
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> 
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    
    /**
     * 根据ID判断实体是否存在
     *
     * @param id 实体ID
     * @return 是否存在
     */
    boolean existsById(ID id);
    
    /**
     * 批量删除
     *
     * @param entities 实体集合
     */
    void deleteInBatch(Iterable<T> entities);
}