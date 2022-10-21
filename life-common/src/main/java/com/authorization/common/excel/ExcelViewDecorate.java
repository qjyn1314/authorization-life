package com.authorization.common.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.style.StyleUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 装饰后的excel视图
 * </p>
 *
 * @author wangjunming
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class ExcelViewDecorate extends AbstractXlsxView {

    /**
     * 仅支持的excel文件格式
     */
    String XLSX_SUFFIX = ".xlsx";

    /**
     * 日期类型默认转换的格式，针对于 Date 、 LocalDateTime
     */
    String TIME_PATTERN = "yyyy/MM/dd HH:mm";

    /**
     * 日期类型默认转换的格式 针对于 LocalDate
     */
    String DATE_PATTERN = "yyyy/MM/dd";

    /**
     * 四舍五入的位数，在导入和导出的时候对小数的默认处理。
     */
    int SCALE = 2;


    /**
     * 导出错误文件的文件桶
     */
    public static final String EXCEL_ERROR_EXPORT_BROKER = "excel_error_export";
    /**
     * 导出正确文件的文件桶
     */
    public static final String EXCEL_SUCCESS_EXPORT_BROKER = "excel_success_export";

    /**
     * 导出的文件名
     */
    private String filename = "excel_data.xlsx";

    /**
     * excel表格转换后的字节数组
     */
    private byte[] excelBytes = new byte[]{};

    /**
     * 多个sheet页
     */
    private List<ExcelSheet> sheets = new ArrayList<>();

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class ExcelSheet {

        /**
         * sheet名称
         */
        private String sheetName;
        /**
         * 标题头字段编码 + 标题头翻译
         */
        private List<ExportField> fields = new ArrayList<>();

        public void setFields(List<ExportField> fields) {
            Assert.notEmpty(fields);
            long fileCodeCount = fields.stream().map(ExportField::getFieldCode).filter(Objects::nonNull).distinct().count();
            Assert.isTrue(fields.size() == fileCodeCount, "字段名出现重复，请检查数据");
            long fileNameCount = fields.stream().map(ExportField::getFieldName).filter(Objects::nonNull).distinct().count();
            Assert.isTrue(fields.size() == fileNameCount, "字段中文名出现重复，请检查数据");
            this.fields = fields;
        }

        public List<ExportField> getFields() {
            return this.fields.stream()
                    .map(field -> field.setFieldName(getFileExportName(field)))
                    .collect(Collectors.toList());
        }

        /**
         * 数据集合
         */
        private Collection<?> data;

        /**
         * 导出的对象
         */
        private Class<?> model;

        private ExcelSheet() {

        }

        private ExcelSheet(String sheetName, Class<?> model, Collection<?> data) {
            this.sheetName = sheetName;
            this.data = data;
            this.model = model;
            fillAnnotationTitles(model, this);
        }

        private ExcelSheet(String sheetName, List<ExportField> fieldList, Collection<?> data) {
            this.sheetName = sheetName;
            this.data = data;
            fillCustomizeTitles(this, fieldList);
        }

        /**
         * 从注解中获取导出字段名
         * <p>
         * 在此处将换掉注解，或者使用自定注解此处可以自定义改进，或者支持多个注解，即第一个注解没找到时，找第二个注解进行构建所需参数
         */
        private static void fillAnnotationTitles(Class<?> model, ExcelSheet sheet) {
            //将通过对model中的 ExcelDecorate注解进行解析，然后设置标题头。包含字段名，第几列，是否必填，用于下载模板时使用
            List<ExportField> decorateColumnFieldList = Arrays.stream(model.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(ExcelDecorate.class))
                    .map(f -> {
                        String fieldCode = f.getName();
                        ExportField exportField = new ExportField();
                        exportField.setFieldCode(fieldCode);

                        ExcelDecorate excelAnnotation = f.getAnnotation(ExcelDecorate.class);
                        String[] values = excelAnnotation.value();
                        exportField.setFieldName(values[0]);

                        int index = excelAnnotation.index();
                        exportField.setOrder(index);

                        exportField.setRequired(excelAnnotation.required());

                        exportField.setFieldNotes(excelAnnotation.fieldNotes());
                        return exportField;
                    }).sorted(ExportField::compareTo).toList();
            sheet.setFields(decorateColumnFieldList);
        }

        /**
         * 自定义标题头
         */
        private static void fillCustomizeTitles(ExcelSheet sheet, List<ExportField> fields) {
            fields = fields.stream().sorted(ExportField::compareTo).toList();
            sheet.setFields(fields);
        }

        /**
         * 导出标题头行的信息
         *
         * @param field 字段信息
         * @return String
         */
        private String getFileExportName(ExportField field) {
            return field.getFieldName();
        }

    }

    /**
     * 创建自定义列的excel
     *
     * @param sheetName sheet页名称
     * @param fieldList 字段集合
     * @param data      数据集
     * @return ExcelSheet
     */
    public static ExcelSheet createSheet(String sheetName, List<ExportField> fieldList, Collection<?> data) {
        return new ExcelSheet(sheetName, fieldList, data);
    }

    /**
     * 创建具备固定列的excel
     *
     * @param sheetName sheet页名称
     * @param model     字段集合
     * @param data      数据集
     * @return ExcelSheet
     */
    public static ExcelSheet createSheet(String sheetName, Class<?> model, Collection<?> data) {
        return new ExcelSheet(sheetName, model, data);
    }

    private ExcelViewDecorate() {
    }

    /**
     * 多个sheet页进行导出
     *
     * @param filename 文件名
     * @param sheets   sheet页数组，可使用静态构建方法： ExcelViewImprove.createSheet()
     */
    public ExcelViewDecorate(String filename, ExcelSheet... sheets) {
        Assert.notBlank(filename, "文件名不能为空。");
        Assert.isTrue(filename.endsWith(XLSX_SUFFIX), "文件名必须是“.xlsx”。");
        Assert.notEmpty(sheets);
        this.filename = filename;
        this.sheets.addAll(Arrays.asList(sheets));
    }

    /**
     * 固定列使用注解参数进行导出
     *
     * @param filename  文件名
     * @param sheetName sheet页名称
     * @param model     数据集合的数据对象
     * @param data      数据集合
     */
    public ExcelViewDecorate(String filename, String sheetName, Class<?> model, Collection<?> data) {
        Assert.notBlank(filename, "文件名不能为空。");
        Assert.isTrue(filename.endsWith(XLSX_SUFFIX), "文件名必须是“.xlsx”。");
        this.filename = filename;
        ExcelSheet sheet = createSheet(sheetName, model, data);
        sheets = Collections.singletonList(sheet);
    }

    /**
     * 动态列作为参数进行导出
     *
     * @param filename  文件名
     * @param sheetName sheet页名称
     * @param fieldList 列字段编码，列字段名称，列排序
     * @param data      数据集合
     */
    public ExcelViewDecorate(String filename, String sheetName, List<ExportField> fieldList, Collection<?> data) {
        Assert.notBlank(filename, "文件名不能为空。");
        Assert.isTrue(filename.endsWith(XLSX_SUFFIX), "文件名必须是“.xlsx”。");
        this.filename = filename;
        ExcelSheet sheet = createSheet(sheetName, fieldList, data);
        sheets = Collections.singletonList(sheet);
    }

    /**
     * springmvc调用的方法，接口将直接进行下载
     *
     * @param map      传参
     * @param workbook 工作簿
     * @param request  请求
     * @param response 响应
     * @throws Exception
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (ExcelSheet sheet : sheets) {
            buildExcel(workbook, sheet);
        }
        //设置响应为文件下载
        response.setContentType("application/force-download");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
    }


    /**
     * 仅构建第一个sheet页面的标题头信息
     *
     * @param fields 字段信息
     */
    public ExcelViewDecorate buildOneSheetTitle(String sheetName, List<ExportField> fields) {
        ExcelSheet sheet = createSheet(sheetName, fields, null);
        this.sheets.add(sheet);
        return this;
    }

    /**
     * 仅构建第一个sheet页面的自定义标题头信息 + 数据信息
     *
     * @param fields 字段信息
     */
    public ExcelViewDecorate buildOneSheetData(String sheetName, List<ExportField> fields, Collection<?> data) {
        ExcelSheet sheet = createSheet(sheetName, fields, data);
        this.sheets.add(sheet);
        return this;
    }

    /**
     * 将构建完成的excel转换为字节数组
     *
     * @return ExcelViewDecorate
     */
    public byte[] buildExcelBytes() {
        //用于创建 .xlsx 的 工作簿类。
        Workbook workbook = new XSSFWorkbook();
        for (ExcelSheet sheet : sheets) {
            buildExcel(workbook, sheet);
        }
        excelBytes = writeOuts(workbook);
        return excelBytes;
    }

    /**
     * 创建excel表格
     *
     * @param workbook 工作簿
     * @param param    所需参数
     */
    private void buildExcel(Workbook workbook, ExcelSheet param) {
        Sheet sheet;
        if (CharSequenceUtil.isNotBlank(param.getSheetName())) {
            sheet = workbook.createSheet(param.getSheetName());
        } else {
            sheet = workbook.createSheet();
        }
        List<ExportField> fields = param.getFields();
        log.debug("标题行的信息是：{}", JSONUtil.toJsonStr(fields));
        int rowNumber = 0;
        if (CollUtil.isNotEmpty(fields)) {
            // 创建标题行-仅单行的标题
            createHeader(sheet, workbook, rowNumber, fields);
            ++rowNumber;
        }
        Collection<?> dataList = param.getData();
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        //创建仅导出的数据行信息
        createDataLine(workbook, sheet, rowNumber, fields, param, dataList);
    }

    /**
     * 创建标题行
     */
    private void createHeader(Sheet sheet, Workbook workbook, int rowNumber, List<ExportField> fields) {
        //进行构建表头
        Row header = sheet.createRow(rowNumber);
        //设置行高
        header.setHeight((short) 420);
        for (int column = 0; column < fields.size(); column++) {
            ExportField exportField = fields.get(column);

            //处理excel标题中的 字段编码与字段名使用  -  进行连接
            String fieldName = exportField.getFieldName();

            Cell cell = header.createCell(column);
            //设置当前单元格的内容
            cell.setCellValue(fieldName);
            //创建样式
            createHeaderCellStyleFont(cell, workbook, exportField);
            //创建批注
            createHeaderComment(cell, sheet, exportField);
            //创建下拉框选项值
            creatHeadSelectCellStyle(workbook, sheet, rowNumber, column, exportField);
            //设置单元格宽度
            sheet.setColumnWidth(column, getColumnWidth(cell));
        }
    }

    /**
     * 获取默认的宽度
     *
     * @param cell 单元格信息
     * @return int
     */
    private int getColumnWidth(Cell cell) {
        return 9000;
    }

    /**
     * 创建下拉框的值
     *
     * @param workbook    工作簿
     * @param sheet       标签页
     * @param rowNumber   第几行
     * @param column      第几列
     * @param exportField 字段信息
     */
    private void creatHeadSelectCellStyle(Workbook workbook, Sheet sheet, int rowNumber, int column, ExportField exportField) {
        List<String> fieldCodeSelect = exportField.getFieldCodeSelect();
        if (CollUtil.isEmpty(fieldCodeSelect)) {
            return;
        }
        String[] fieldSelectArray = fieldCodeSelect.toArray(new String[0]);
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        creatDropDownList(sheet, dataValidationHelper, fieldSelectArray, rowNumber, getLastRowNum(sheet), column, column);
    }

    /**
     * 设置下拉框的最大行是 十万
     *
     * @param sheet 当前sheet对象
     * @return 100000
     */
    private Integer getLastRowNum(Sheet sheet) {
        return 100000;
    }

    /**
     * 设置标题样式和颜色
     *
     * @param cell        单元格
     * @param workbook    工作簿
     * @param exportField 是否必填，如果是必填项则字体颜色是红色
     */
    private void createHeaderCellStyleFont(Cell cell, Workbook workbook, ExportField exportField) {
        CellStyle headCellStyle = StyleUtil.createHeadCellStyle(workbook);
        StyleUtil.setAlign(headCellStyle, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        Boolean required = exportField.getRequired();
        if (Boolean.FALSE.equals(required)) {
            headCellStyle.setFont(blackFont(workbook));
            cell.setCellStyle(headCellStyle);
            return;
        }
        headCellStyle.setFont(redFont(workbook));
        cell.setCellStyle(headCellStyle);
    }

    /**
     * 创建批注
     *
     * @param cell        当前单元格
     * @param sheet       标签页
     * @param exportField 批注信息
     */
    private void createHeaderComment(Cell cell, Sheet sheet, ExportField exportField) {
        String fieldNotes = exportField.getFieldNotes();
        if (CharSequenceUtil.isBlank(fieldNotes)) {
            return;
        }
        createCellComment(cell, sheet, fieldNotes);
    }

    /**
     * 创建批注
     *
     * @param cell       当前单元格
     * @param sheet      标签页
     * @param fieldNotes 批注信息
     */
    private void createCellComment(Cell cell, Sheet sheet, String fieldNotes) {
        if (CharSequenceUtil.isBlank(fieldNotes)) {
            return;
        }
        // 创建绘图对象
        Drawing<?> patriarch = sheet.createDrawingPatriarch();
        // 前四个参数是坐标点,后四个参数是编辑和显示批注时的大小.
        Comment comment = patriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 12, 12, (short) 16, 16));
        // 输入批注信息
        comment.setString(new XSSFRichTextString(fieldNotes));
        // 添加作者,选中B5单元格,看状态栏
        comment.setAuthor("系统");
        cell.setCellComment(comment);
    }

    /**
     * 红色字
     */
    private Font redFont(Workbook workbook) {
        Font font = StyleUtil.createFont(workbook, HSSFColor.HSSFColorPredefined.RED.getIndex(), (short) 14, "宋体");
        font.setBold(true);
        return font;
    }

    /**
     * 黑色字
     */
    private Font blackFont(Workbook workbook) {
        Font font = StyleUtil.createFont(workbook, HSSFColor.HSSFColorPredefined.BLACK.getIndex(), (short) 14, "宋体");
        font.setBold(true);
        return font;
    }

    /**
     * 创建下拉框
     * <p>
     * 参考：https://www.csdn.net/tags/OtDagg3sODUxMTQtYmxvZwO0O0OO0O0O.html
     *
     * @param taskInfoSheet 当前标签页
     * @param helper        用于创建下拉框的工具
     * @param list          下拉框的数据
     * @param firstRow      开始第一行
     * @param lastRow       最后一行
     * @param firstCol      开始第一列
     * @param lastCol       最后一列
     */
    private static void creatDropDownList(Sheet taskInfoSheet, DataValidationHelper helper, String[] list,
                                          Integer firstRow, Integer lastRow, Integer firstCol, Integer lastCol) {
        //获取需要生成下拉框的格
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        //设置下拉框数据
        DataValidationConstraint constraint = null;
        try {
            constraint = helper.createExplicitListConstraint(list);
        } catch (Exception e) {
            log.error("向excel中添加下拉框数据异常，请检查下拉框的列表是否超过255 A valid formula or a list of values must be less than or equal to 255 characters (including separators).", e);
            throw new DecorateExcelException("生成Excel的下拉框选择数据时失败，请检查数据是否过多。");
        }
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        //处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        taskInfoSheet.addValidationData(dataValidation);
    }

    /**
     * 创建数据行信息
     *
     * @param workbook  工作簿
     * @param sheet     标签页
     * @param rowNumber 需要创建第几行
     * @param fields    字段名信息
     * @param param     excel导出所需要的相关参数
     * @param dataList  数据集合
     */
    private void createDataLine(Workbook workbook, Sheet sheet, int rowNumber,
                                List<ExportField> fields, ExcelSheet param, Collection<?> dataList) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        //构建数据行
        for (Object item : dataList) {
            BeanMap beanMap = BeanMap.create(item);
            Row row = sheet.createRow(rowNumber);
            for (int column = 0; column < fields.size(); column++) {
                ExportField exportField = fields.get(column);
                String fieldCode = exportField.getFieldCode();
                Assert.notBlank(fieldCode, "未找到此{}对应的字段值", fieldCode);
                Cell cell = row.createCell(column);
                String filedValue;
                try {
                    filedValue = getValue(item, beanMap, fieldCode, param);
                } catch (Exception e) {
                    log.error("导出数据行异常，字段名：{}，此数据对象信息：{}，赋值异常", JSONUtil.toJsonStr(exportField), JSONUtil.toJsonStr(beanMap), e);
                    filedValue = "";
                }
                cell.setCellStyle(getLineStyle(workbook));
                cell.setCellValue(filedValue);
                sheet.setColumnWidth(column, getColumnWidth(cell));
            }
            ++rowNumber;
        }

    }

    /**
     * 获取数据行的单元格样式
     *
     * @param workbook 工作簿
     * @return CellStyle
     */
    private CellStyle getLineStyle(Workbook workbook) {
        CellStyle lineCellStyle = StyleUtil.createDefaultCellStyle(workbook);
        StyleUtil.setAlign(lineCellStyle, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        return lineCellStyle;
    }

    /**
     * 获取数据行的错误单元格样式，颜色为黄色
     *
     * @param workbook 工作簿
     * @return CellStyle
     */
    private CellStyle getLineWarnStyle(Workbook workbook) {
        CellStyle lineCellStyle = StyleUtil.createDefaultCellStyle(workbook);
        StyleUtil.setAlign(lineCellStyle, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        StyleUtil.setColor(lineCellStyle, HSSFColor.HSSFColorPredefined.YELLOW.getIndex(), FillPatternType.SOLID_FOREGROUND);
        return lineCellStyle;
    }

    /**
     * 获取当前集合对象对应的字段值
     *
     * @param beanMap   当前对象的字段与值的map
     * @param fieldCode 字段名
     * @param param     设置参数
     * @return String
     */
    private String getValue(Object item, BeanMap beanMap, String fieldCode, ExcelSheet param) {
        String value = "";
        Object obj = beanMap.get(fieldCode);
        if (obj == null) {
            return value;
        }
        Class<?> propertyType = beanMap.getPropertyType(fieldCode);
        if (BigDecimal.class.isAssignableFrom(propertyType)) {
            value = ((BigDecimal) obj).setScale(SCALE, RoundingMode.HALF_UP).toString();
        } else if (Date.class.isAssignableFrom(propertyType)) {
            value = new SimpleDateFormat(TIME_PATTERN).format(obj);
        } else if (LocalDate.class.isAssignableFrom(propertyType)) {
            value = ((LocalDate) obj).format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
            value = ((LocalDateTime) obj).format(DateTimeFormatter.ofPattern(TIME_PATTERN));
        } else {
            value = (String.valueOf(obj));
        }
        return value;
    }

    /**
     * 为当前单元格设置不同的value值。
     *
     * @param cell      当前单元格
     * @param item      数据信息
     * @param beanMap   实体类信息
     * @param fieldCode 字段名
     * @param param     参数信息
     */
    private void setDataTypeValue(Cell cell, Object item, BeanMap beanMap, String fieldCode, ExcelSheet param) {
        Object obj = beanMap.get(fieldCode);
        if (obj == null) {
            cell.setCellValue(CharSequenceUtil.EMPTY);
            return;
        }
        Class<?> propertyType = beanMap.getPropertyType(fieldCode);
        if (String.class.isAssignableFrom(propertyType)) {
            cell.setCellValue(obj.toString());
        } else if (Integer.class.isAssignableFrom(propertyType)
                || Long.class.isAssignableFrom(propertyType)
                || BigDecimal.class.isAssignableFrom(propertyType)
        ) {
            cell.setCellValue(Double.parseDouble(obj.toString()));
        } else if (Boolean.class.isAssignableFrom(propertyType)) {
            cell.setCellValue(Boolean.parseBoolean(obj.toString()));
        } else if (Date.class.isAssignableFrom(propertyType)) {
            cell.setCellValue(new SimpleDateFormat(DATE_PATTERN).format(obj));
        } else if (LocalDate.class.isAssignableFrom(propertyType)) {
            cell.setCellValue(((LocalDate) obj).format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
            cell.setCellValue(((LocalDateTime) obj).format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        } else {
            cell.setCellValue(obj.toString());
        }
    }


    /**
     * 将 workbook 转换为 字节数组
     *
     * @param workbook 工作簿
     * @return byte[]
     */
    private static byte[] writeOuts(Workbook workbook) {
        if (Objects.isNull(workbook)) {
            return new byte[]{};
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("写入流异常", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error("关闭workbook异常", e);
            }
        }
        return outputStream.toByteArray();
    }

    /**
     * 指定本地文件目录生成文件信息
     *
     * @param fileAbsolutePath D:/gen_excel
     * @param fileName         文件名 需要带文件格式
     * @return String
     */
    public String downloadLocalExcel(String fileAbsolutePath, String fileName) {
        filename = fileName;
        Assert.notBlank(filename);
        Assert.isTrue(filename.endsWith(XLSX_SUFFIX));
        Assert.notBlank(fileAbsolutePath);
        //创建文件夹
        FileUtil.mkdir(FileUtil.file(fileAbsolutePath));
        fileAbsolutePath = fileAbsolutePath + "\\" + filename;
        fileAbsolutePath = fileAbsolutePath.replaceAll("\\\\", "/");
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileAbsolutePath)) {
            IoUtil.write(fileOutputStream, Boolean.TRUE, excelBytes);
            IoUtil.close(fileOutputStream);
        } catch (Exception e) {
            log.error("生成本地文件失败，请检查文件路径是否正确。", e);
        }
        log.info("生成的文件名...- file:///{}", fileAbsolutePath);
        return fileAbsolutePath;
    }

    /**
     * excel的标题行信息
     */
    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class ExportField implements Comparable<ExportField> {

        private ExportField() {
        }

        public ExportField(String fieldCode, String fieldName) {
            this.fieldCode = fieldCode;
            this.fieldName = fieldName;
        }

        /**
         * 默认构建方式
         *
         * @param fieldCode 字段名
         * @param fieldName 字段名的翻译
         * @return ExportField
         */
        public static ExportField build(String fieldCode, String fieldName) {
            Assert.notBlank(fieldCode);
            Assert.notBlank(fieldName);
            return new ExportField(fieldCode, fieldName);
        }

        /**
         * 字段名
         */
        private String fieldCode;
        /**
         * 字段的中文说明
         */
        private String fieldName;
        /**
         * 字段是否必填
         */
        private Boolean required = Boolean.FALSE;
        /**
         * 字段的批注
         */
        private String fieldNotes;
        /**
         * 字段名所属的下拉框的值
         */
        private List<String> fieldCodeSelect;
        /**
         * 排序
         */
        private int order;

        @Override
        public int compareTo(ExportField exportField) {
            return exportField.order;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExportField that = (ExportField) o;
            return order == that.order && fieldCode.equals(that.fieldCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldCode, fieldName, order);
        }
    }


}
