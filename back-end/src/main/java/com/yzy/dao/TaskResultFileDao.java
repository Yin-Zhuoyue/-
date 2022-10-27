package com.yzy.dao;

import com.yzy.entity.TaskResultFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 任务结果文件信息处理
 */
@Mapper
public interface TaskResultFileDao {

    /**
     * 保存任务结果文件信息
     *
     * @param taskResultFile 结果文件信息
     * @return 返回sql影响的行数，并将自增的file_id写入taskResultFile
     */
    @Options(useGeneratedKeys = true, keyProperty = "file_id")      // 插入结束后会返回自增主键
    @Insert("insert into task_result_file values(null, #{file_name}, #{file_type}, #{data_separator}, " +
            "#{date_suffix_type}, #{start_date}, #{start_date_offset}, #{end_date}, #{end_date_offset}, #{task_id})")
    public int saveTaskResultFile(TaskResultFile taskResultFile);

    /* 结果文件的批量插入。
    # 本来想在任务的整体复制中使用，但因为插入之后的结果文件会失去原数据中与任务sql的对应关系，所以没派上用场。。。
    # 只注释不删是考虑到将来说不定会用上（别问什么需求会用上，问就是有可能，说不定，万一呢），所以留着了。

    @Options(useGeneratedKeys = true, keyProperty = "file_id")      // 插入结束后会返回自增主键
    @Insert("<script>insert into task_result_file values " +
            "<foreach collection='taskResultFiles' item='taskResultFile' separator=','>" +
            "(null, #{taskResultFile.file_name}, #{taskResultFile.file_type}, #{taskResultFile.data_separator}, " +
            "#{taskResultFile.date_suffix_type}, #{taskResultFile.start_date}, #{taskResultFile.start_date_offset}, " +
            "#{taskResultFile.end_date}, #{taskResultFile.end_date_offset}, #{taskResultFile.task_id}) " +
            "</foreach></script>")
    public int saveTaskResultFiles(@Param("taskResultFiles") List<TaskResultFile> taskResultFiles);
    */


    /**
     * 更新结果文件信息
     *
     * @param taskResultFile 结果文件信息
     * @return 返回sql影响的行数
     */
    @Update("update task_result_file set file_name = #{file_name}, file_type = #{file_type}, data_separator = #{data_separator}, " +
            "date_suffix_type = #{date_suffix_type}, start_date = #{start_date}, start_date_offset = #{start_date_offset}, " +
            "end_date = #{end_date}, end_date_offset = #{end_date_offset}  where file_id = #{file_id}")
    public int updateTaskResultFile(TaskResultFile taskResultFile);

    /**
     * 根据任务id查询此任务下的结果文件信息列表
     *
     * @param taskId 任务id
     * @return 返回结果文件信息列表
     */
    @Select("select * from task_result_file where task_id = #{taskId}")
    public List<TaskResultFile> getTaskResultFiles(Integer taskId);

    /**
     * 根据文件id查询此文件的信息
     *
     * @param fileId 文件id
     * @return 返回结果文件信息
     */
    @Select("select * from task_result_file where file_id = #{fileId}")
    public TaskResultFile getTaskResultFileInfoById(Integer fileId);

    /**
     * 删除结果文件信息
     *
     * @param fileId 文件id
     * @return 返回sql影响的行数
     */
    @Delete("delete from task_result_file where file_id = #{fileId};delete from task_sql where file_id = #{fileId};")
    public int deleteTaskResultFile(Integer fileId);

}
