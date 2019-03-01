package com.qhjys.springcloud.util;

import com.qhjys.springcloud.exception.ValidationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CSVUtil {

    /**
     * 从浏览器中下载
     * 必须是Bean,必须存在get方法,headersIds对应Bean中属性名
     * 输出字段需要替换的请在Bean对应的get方法中实现
     *
     * @param title        文件名
     * @param headersNames 表头名称
     * @param headersIds   表头对应的属性名
     * @param dtoList      数据集合
     * @param response
     */
    public static <T> void exportCSV(String title, String[] headersNames, String[] headersIds,
                                     List<T> dtoList, HttpServletResponse response) {
        StopWatch stopWatch = new StopWatch("CSV数据导出");

        stopWatch.start("初始化表头");
        StringBuilder sb = new StringBuilder();
        //表头
        int headSize = headersNames.length;
        for (int i = 0; i < headSize; i++) {
            sb.append(headersNames[i]);
            if ((i + 1) < headSize) {
                sb.append(",");
            } else if ((i + 1) == headSize) {
                sb.append("\r");
            }
        }
        stopWatch.stop();

        stopWatch.start("添加数据列表");
        //列表
        for (T t : dtoList) {
            Class clazz = (Class) t.getClass();
            int headIdSize = headersIds.length;
            for (int i = 0; i < headIdSize; i++) {
                String getMethodName = "get" + headersIds[i].substring(0, 1).toUpperCase() + headersIds[i].substring(1);
                Object val = null;

                try {
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    val = getMethod.invoke(t, new Object[]{});
                    if (null == val) {
                        val = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("--导出反射异常--", e);
                }

                sb.append("\t" + val);
                if ((i + 1) < headIdSize) {
                    sb.append(",");
                } else if ((i + 1) == headIdSize) {
                    sb.append("\r");
                }

            }
        }
        stopWatch.stop();

        stopWatch.start("输出到浏览器");
        OutputStreamWriter bos = null;
        try {
            // 设置输出的格式
            response.reset();
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String((title + ".csv").getBytes(), "iso-8859-1"));
            response.setBufferSize(81920);
            bos = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            bos.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            bos.write(sb.toString());
        } catch (final IOException e) {
            e.printStackTrace();
            log.error("--导出写入文件异常--", e);
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("--导出写入文件异常--", e);
            }
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    /**
     * CSV文件导入
     * 原导出的模板列头必须与templateColumnName名称、顺序一致
     * getColumnName可以只获取一个列
     * getColumnName与getColumnId顺序必须一致
     * @param templateColumnName 原导出模板的所有列-名称
     * @param getColumnName 需要获取模板中列-名称
     * @param getColumnId 需要获取模板中列-id
     * @param is csv
     * @return key：getColumnId；value：模板内容
     */
    @SneakyThrows
    public static List<Map<String, String>> importCSV(String[] templateColumnName, String[] getColumnName, String[] getColumnId, InputStream is) {
        List<String> oldColumnNameList = Arrays.asList(templateColumnName);
        // 读取csv内容
        List<String> contentList = IOUtils.readLines(new InputStreamReader(new BOMInputStream(is), "UTF-8"));
        if (contentList.size() <= 1) {
            throw new IOException("导入的数据为空");
        }
        List<String> newColumnNameList = Arrays.asList(contentList.get(0).split(","));

        // 判断模板是否一致
        if (!oldColumnNameList.equals(newColumnNameList)) {
            throw new IOException("模板列头不一致");
        }

        // 移除导入模板的列头
        contentList.remove(0);

        // 装载需要获取的值
        List<Map<String, String>> listMap = new ArrayList<>();
        Map<String, String> map = null;
        for (String line : contentList) {
            List<String> arrayList = Arrays.asList(line.split(","));
            map = new HashMap<>();
            listMap.add(map);
            for (int i=0; i<getColumnName.length; i++) {
                if (oldColumnNameList.indexOf(getColumnName[i]) > -1) {
                    map.put(getColumnId[i], replaceBlank(StringUtils.trim(arrayList.get(oldColumnNameList.indexOf(getColumnName[i])))));
                }
            }
        }

        return listMap;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("").replace("\"","");
        }
        return dest;
    }



    //==============================================导入====================
    /**
     * @Title: createExcelTemplate
     * @Description: 生成Excel导入模板
     * @param @param fileName  Excel文件名称
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     * @return void
     * @throws
     */
    public static void exportExcelTemplate(String fileName, String[] handers,
                                           List<String[]> downData, String[] downRows,
                                           HttpServletResponse response, HttpServletRequest request){
        try {
            //fileName = encodeChineseDownloadFileName(request,fileName);

            HSSFWorkbook wb = new HSSFWorkbook();//创建工作薄

            //表头样式
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            //字体样式
            HSSFFont fontStyle = wb.createFont();
            fontStyle.setFontName("微软雅黑");
            fontStyle.setFontHeightInPoints((short)12);
            fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(fontStyle);

            //新建sheet
            HSSFSheet sheet1 = wb.createSheet("Sheet1");
            HSSFSheet sheet2 = wb.createSheet("Sheet2");
            HSSFSheet sheet3 = wb.createSheet("Sheet3");

            //生成sheet1内容
            HSSFRow rowFirst = sheet1.createRow(0);//第一个sheet的第一行为标题
            //写标题
            for(int i=0;i<handers.length;i++){
                HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
                sheet1.setColumnWidth(i, 4000); //设置每列的列宽
                cell.setCellStyle(style); //加样式
                cell.setCellValue(handers[i]); //往单元格里写数据
            }

            //设置下拉框数据
            String[] arr = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
            int index = 0;
            HSSFRow row = null;
            for(int r=0;r<downRows.length;r++){
                String[] dlData = downData.get(r);//获取下拉对象
                int rownum = Integer.parseInt(downRows[r]);

                if(dlData.length < 5){ //255以内的下拉
                    //255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                    sheet1.addValidationData(setDataValidation(sheet1, dlData, 1, 50000, rownum ,rownum)); //超过255个报错
                } else { //255以上的下拉，即下拉列表元素很多的情况

                    //1、设置有效性
                    //String strFormula = "Sheet2!$A$1:$A$5000" ; //Sheet2第A1到A5000作为下拉列表来源数据
                    String strFormula = "Sheet2!$"+arr[index]+"$1:$"+arr[index]+"$5000"; //Sheet2第A1到A5000作为下拉列表来源数据
                    sheet2.setColumnWidth(r, 4000); //设置每列的列宽
                    //设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                    sheet1.addValidationData(SetDataValidation(strFormula, 1, 50000, rownum, rownum)); //下拉列表元素很多的情况

                    //2、生成sheet2内容
                    for(int j=0;j<dlData.length;j++){
                        if(index==0){ //第1个下拉选项，直接创建行、列
                            row = sheet2.createRow(j); //创建数据行
                            sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                            row.createCell(0).setCellValue(dlData[j]); //设置对应单元格的值

                        } else { //非第1个下拉选项

                            int rowCount = sheet2.getLastRowNum();
                            //System.out.println("========== LastRowNum =========" + rowCount);
                            if(j<=rowCount){ //前面创建过的行，直接获取行，创建列
                                //获取行，创建列
                                sheet2.getRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值

                            } else { //未创建过的行，直接创建行、创建列
                                sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                                //创建行、创建列
                                sheet2.createRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值
                            }
                        }
                    }
                    index++;
                }
            }

            //输入文件到浏览器
            // 设置输出的格式
            response.reset();
            response.setContentType("application/x-msdownload;charset=utf-8");
            //response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes(), "iso-8859-1"));

            //3.通过response获取OutputStream对象(out)
            OutputStream out = new BufferedOutputStream(response.getOutputStream());

            out.flush();
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @Title:
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     * @param @param strFormula
     * @param @param firstRow   起始行
     * @param @param endRow     终止行
     * @param @param firstCol   起始列
     * @param @param endCol     终止列
     * @param @return
     * @return HSSFDataValidation
     * @throws
     */
    private static HSSFDataValidation SetDataValidation(String strFormula,
                                                        int firstRow, int endRow, int firstCol, int endCol) {

        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);

        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }


    /**
     *
     * @Title: setDataValidation
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     * @param @param sheet
     * @param @param textList
     * @param @param firstRow
     * @param @param endRow
     * @param @param firstCol
     * @param @param endCol
     * @param @return
     * @return DataValidation
     * @throws
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {

        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        //DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);

        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);

        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        //DataValidation data_validation = new DataValidation(regions, constraint);

        return data_validation;
    }

    /**
     * 读取excel
     * @param is
     * @param fields 对应表头的字段名
     * @return
     * @throws IOException
     */
    public static List<Map> readXlsx(InputStream is, String[] fields) throws IOException {
        String key = null;
        String value = null;

        List<Map> datas = new ArrayList<>();
        Map row = null;

        //InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        //只读取第一个sheet
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        if (hssfSheet != null) {
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                 HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                 if (hssfRow != null) {
                     row = new HashMap();
                     for (int i = 0; i < fields.length; i++) {
                         HSSFCell cell = hssfRow.getCell(i);
                         value = getValue(cell);
                         key = fields[i];
                         row.put(key,value);
                     }
                     datas.add(row);
                 }
            }
            return datas;
        }
        return null;
    }

    private static String getValue(Cell c) {
        String value = "";
        if(c != null) {
            short format = c.getCellStyle().getDataFormat();
            if (format == 14 || format == 31 || format == 57 || format == 58) {
                value = DateUtil.formatDateByFormat(c.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
            } else {
                c.setCellType(HSSFCell.CELL_TYPE_STRING);
                value = c.toString().trim();
            }

        }
        return value;
    }


    /**
     *
     * @param sheetNum
     * @param sheetName
     * @param workbook
     * @param header
     * @param headers
     * @param headersIds
     * @param dtoList
     * @param <T>
     * @throws ValidationException
     */
    public static <T> void exportExcelFormat(Integer sheetNum, String sheetName, HSSFWorkbook workbook, String[] header, String[] headers, String[] headersIds, List<T> dtoList) throws ValidationException{
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();

        workbook.setSheetName(sheetNum, sheetName);

        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth((short) 25.57);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
           // sheet.createRow(0);
            HSSFCell cell = row.createCell((short) i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            cell.setCellValue(text.toString());
        }
        row = sheet.createRow(header.length > 0 ? 1 : 0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }

        int index = header.length > 0 ? 2 : 1;
        for (T t : dtoList) {
            row = sheet.createRow(index);
            int cellIndex = 0;

            Class clazz = (Class) t.getClass();
            int headIdSize = headersIds.length;
            for (int i = 0; i < headIdSize; i++) {
                String getMethodName = "get" + headersIds[i].substring(0, 1).toUpperCase() + headersIds[i].substring(1);
                Object val = null;

                try {
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    val = getMethod.invoke(t, new Object[]{});
                    if (null == val) {
                        val = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("--导出反射异常--", e);
                }

                HSSFCell cell = row.createCell((short) cellIndex);
                cell.setCellValue(val.toString());
                cellIndex++;
            }
            index++;
        }
    }

    public static void exprot(HttpServletResponse response, HSSFWorkbook workbook, String fileName) throws ValidationException {
        try {
            //输入文件到浏览器
            // 设置输出的格式
            response.reset();
            response.setContentType("application/x-msdownload;charset=utf-8");
            //response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes(), "iso-8859-1"));

            //3.通过response获取OutputStream对象(out)
            OutputStream out = new BufferedOutputStream(response.getOutputStream());

            out.flush();
            workbook.write(out);
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
            log.error("--导出写入文件异常--", e);
        }
    }


}
