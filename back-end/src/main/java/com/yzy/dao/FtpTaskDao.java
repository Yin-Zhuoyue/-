package com.yzy.dao;

import com.yzy.entity.FtpTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * ftp信息处理
 */
@Mapper
public interface FtpTaskDao {

    /**
     * 保存ftp任务中的ftp相关信息
     *
     * @param ftpTask 任务的ftp信息
     * @return 返回sql影响的行数
     */
    @Insert("insert into ftp_task values(#{task_id}, #{ftp_host}, #{ftp_port}, #{ftp_user}, #{ftp_pwd}, #{file_save_catalog}) ")
    public int saveFtpTaskInfo(FtpTask ftpTask);

    /**
     * 更新ftp任务中的ftp相关信息
     *
     * @param ftpTask 任务的ftp信息
     * @return 返回sql影响的行数
     */
    @Update("update ftp_task set ftp_host = #{ftp_host}, ftp_port = #{ftp_port}, ftp_user = #{ftp_user}, ftp_pwd = #{ftp_pwd}, " +
            "file_save_catalog = #{file_save_catalog} where task_id = #{task_id}")
    public int updateFtpTaskInfo(FtpTask ftpTask);

    /**
     * 通过任务id查询任务的ftp信息
     *
     * @param taskId 任务id
     * @return 返回任务的ftp信息
     */
    @Select("select * from ftp_task where task_id = #{taskId}")
    public FtpTask getFtpTaskInfoById(Integer taskId);
}
