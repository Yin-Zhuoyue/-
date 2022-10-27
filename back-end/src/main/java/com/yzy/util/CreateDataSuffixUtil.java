package com.yzy.util;

import com.yzy.entity.TaskResultFile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 创建日期后缀的工具类
 */
public class CreateDataSuffixUtil {

    /**
     * 根据结果文件信息生成日期后缀
     *
     * @param taskResultFile 结果文件信息
     * @return 返回日期后缀字符串
     */
    public static String createDataSuffix(TaskResultFile taskResultFile) {
        String dateSuffix = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // 日期后缀由基准日和偏移量决定，基准日为执行日当日、执行日当月1日、执行日上月1日、执行日昨日当月1日四种。
        switch (taskResultFile.getDate_suffix_type()) {
            case "单日期":
                setCalender(taskResultFile.getStart_date(), taskResultFile.getStart_date_offset(), calendar);
                dateSuffix = new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime());
                break;
            case "日期区间":
                setCalender(taskResultFile.getStart_date(), taskResultFile.getStart_date_offset(), calendar);
                String startDate = new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime());   // 开始日期
                calendar.setTime(new Date());   // 重置时间
                setCalender(taskResultFile.getEnd_date(), taskResultFile.getEnd_date_offset(), calendar);
                String endDate = new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime());    // 结束日期
                dateSuffix = startDate + "-" + endDate;
                break;
            case "不加后缀日期":
                break;
        }
        return dateSuffix;
    }

    /**
     * 编辑日期
     *
     * @param baseDate 基准日
     * @param offset   偏移量
     * @param calendar 日历单例
     */
    private static void setCalender(String baseDate, Integer offset, Calendar calendar) {
        switch (baseDate) {
            case "执行日当日":
                break;
            case "执行日当月1日":
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case "执行日上月1日":
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case "执行日昨日当月1日":
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.add(Calendar.DAY_OF_MONTH, offset);
    }
}
