package com.yzy.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将数据导入文件的工具类
 */
public class DataImportFileUtil {

    private final static String FILE_SAVE_PATH = "D:/自动任务的结果文件/";
    private final static String EMPTYDATAHINT = "本期脚本运行结果为空";
    private final static String DATAOVERFLOWHINT = "脚本运行结果超过excel最大行数限制（1048576），本期数据无法写入excel" +
            "请通过缩短取数时间范围等方式减少数据量。";

    /**
     * 数据导入excel
     *
     * @param dataMap 数据集合，元素为sheet名称和数据的键值对
     * @param name    文件名
     * @return 返回生成文件的路径
     */
    public static String dataImportExcel(LinkedHashMap<String, List<LinkedHashMap<String, Object>>> dataMap, String name) {
        try {
            // 创建新的workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFFont font = workbook.createFont();  // 设置样式
            font.setBold(true); // 加粗
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);

            FileOutputStream os = new FileOutputStream(FILE_SAVE_PATH + name);

            for (String sheetName : dataMap.keySet()) {
                List<LinkedHashMap<String, Object>> sheetData = dataMap.get(sheetName); // 将要写入sheet的数据

                Sheet sheet = workbook.createSheet(sheetName);
                sheet.setDefaultColumnWidth(20);    // 设置所有列宽为20
                sheet.createFreezePane(0, 1, 0, 1);    // 首行冻结
                Row fieldsRow = sheet.createRow(0);

                if (sheetData.size() == 0) {    // 如果脚本运行结果为空，则只编辑第一个单元格用于提示，不导入数据
                    fieldsRow.createCell(0).setCellValue(EMPTYDATAHINT);
                    continue;
                } else if (sheetData.size() > 1048570) { // 如果运行结果超excel最大行数则提示用户改脚本，不导入数据
                    fieldsRow.createCell(0).setCellValue(DATAOVERFLOWHINT);
                    continue;
                }

                // 如果数据不为空，则将字段和数据导入当前的sheet
                AtomicInteger fieldCount = new AtomicInteger();
                for (Map.Entry<String, Object> entry : sheetData.get(0).entrySet()) {   // 遍历数据的第一行，获取字段名
                    fieldsRow.createCell(fieldCount.get()).setCellValue(entry.getKey());   // 将字段名写入当前sheet的第一行
                    fieldsRow.getCell(fieldCount.get()).setCellStyle(cellStyle);
                    fieldCount.getAndIncrement();
                }

                // 将数据写入当前sheet
                for (AtomicInteger i = new AtomicInteger(); i.get() < sheetData.size(); i.getAndIncrement()) {
                    Row row = sheet.createRow(i.get() + 1);
                    AtomicInteger col = new AtomicInteger();
                    for (Map.Entry<String, Object> entry : sheetData.get(i.get()).entrySet()) {// 遍历数据的每个entry
                        Object item = entry.getValue(); // 获取entry的值
                        if (item == null) {// 值为空则填空字符串
                            row.createCell(col.get()).setCellValue("");
                        } else if (item.getClass().toString().equals("class java.sql.Timestamp")) {// 值为时间戳则做类型转换
                            Date itemDate = new Date(((Timestamp) item).getTime());
                            row.createCell(col.get()).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(itemDate));
                        } else if (item.getClass().toString().equals("class java.math.BigDecimal")) {// 值为BigDecimal则转换成Double
                            row.createCell(col.get()).setCellValue(Double.parseDouble(item.toString()));
                        } else
                            row.createCell(col.get()).setCellValue((String) item);
                        col.getAndIncrement();
                    }
                }
            }

            workbook.write(os);
            workbook.close();
            os.close();
            return FILE_SAVE_PATH + name;
        } catch (Exception e) {
            e.printStackTrace();
            return "结果文件生成失败";
        }
    }

    /**
     * 数据导入txt
     *
     * @param dataList      数据
     * @param name          文件名
     * @param dataSeparator 分隔符
     * @return 返回生成文件的路径
     */
    public static String dataImportTxt(List<LinkedHashMap<String, Object>> dataList, String name, String dataSeparator) {
        try {
            String separator;
            if (dataSeparator != null) {    // 如果传入的数据分隔符不为null，则使用这个分隔符，如果为空则默认用英文逗号
                if (dataSeparator.equals(""))
                    separator = ",";
                else
                    separator = dataSeparator;
            } else {
                separator = ",";
            }

            File txt = new File(FILE_SAVE_PATH + name);
            if (txt.exists()) {
                txt.delete();
            }

            if (txt.createNewFile()) {
                FileWriter fileWriter = new FileWriter(FILE_SAVE_PATH + name);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                if (dataList.size() == 0)
                    bufferedWriter.write(EMPTYDATAHINT);
                else {
                    for (Map.Entry<String, Object> entry : dataList.get(0).entrySet()) { // 将字段名写入第一行
                        // 通过反射获取字段中的最后一个元素
                        Field tail = dataList.get(0).getClass().getDeclaredField("tail");
                        tail.setAccessible(true);
                        if (entry != tail.get(dataList.get(0)))
                            bufferedWriter.write(entry.getKey() + separator);
                        else
                            bufferedWriter.write(entry.getKey() + "\n");
                    }
                    for (LinkedHashMap<String, Object> data : dataList) {
                        // 通过反射获取一行数据中的最后一个元素
                        Field tail = data.getClass().getDeclaredField("tail");
                        tail.setAccessible(true);
                        Object tailEntry = tail.get(data);
                        for (Map.Entry<String, Object> entry : data.entrySet()) { // 遍历数据并写入txt
                            if (entry != tailEntry)
                                bufferedWriter.write(entry.getValue() + separator);
                            else
                                bufferedWriter.write(entry.getValue() + "\n"); // 如果遇到一行最后一个，则换行
                        }
                    }
                }
                bufferedWriter.close();
                fileWriter.close();
                return FILE_SAVE_PATH + name;
            } else
                return "结果文件生成失败";
        } catch (Exception e) {
            e.printStackTrace();
            return "结果文件生成失败";
        }
    }

}
