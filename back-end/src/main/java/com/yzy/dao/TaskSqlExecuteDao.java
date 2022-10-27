package com.yzy.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户添加的sql的具体执行
 */
@Mapper
public interface TaskSqlExecuteDao {
    // 运行sql脚本获取数据

    /**
     * 执行用户添加的sql脚本，DS指定是用哪个数据库，timeout控制sql的执行时间。
     * zd指综调数据库
     *
     * @param sql sql脚本
     * @return 返回sql的查询结果数据，用linkedHashMap保证查询字段的顺序
     */
    @DS("oracle-zd")
    @Select("${sql}")
    @Options(timeout = 1)
    public List<LinkedHashMap<String, Object>> getZdResultData(String sql) throws Exception;

    /**
     * 执行用户添加的sql脚本，DS指定是用哪个数据库，timeout控制sql的执行时间。
     * zg指综告数据库
     *
     * @param sql sql脚本
     * @return 返回sql的查询结果数据，用linkedHashMap保证查询字段的顺序
     */
    @DS("oracle-zg")
    @Select("${sql}")
    @Options(timeout = 3600)
    public List<LinkedHashMap<String, Object>> getZgResultData(String sql) throws Exception;

    /**
     * 执行用户添加的sql脚本，DS指定是用哪个数据库，timeout控制sql的执行时间。
     * ty指统一网元库
     *
     * @param sql sql脚本
     * @return 返回sql的查询结果数据，用linkedHashMap保证查询字段的顺序
     */
    @DS("oracle-ty")
    @Select("${sql}")
    @Options(timeout = 3600)
    public List<LinkedHashMap<String, Object>> getTyResultData(String sql) throws Exception;


}
