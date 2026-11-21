package tyut.selab.a2.pipes.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tyut.selab.a2.pipes.exception.*;
import tyut.selab.a2.pipes.vo.ExcelPipeVo;
import tyut.selab.a2.pipes.vo.ExcelVo;
import tyut.selab.a2.pipes.vo.ExcelWellVo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static tyut.selab.a2.pipes.utils.LonlatConverUtil.wgs84_To_Gcj02;
import static tyut.selab.a2.pipes.utils.StringUtil.judgeCellType;


@Slf4j
@Component
public class ExcelUtil {

    private static String regixOfTheFirstColum1 = "井编号井类型经度纬度型号相连成线的井编号管道类型深度纬度偏移量经度偏移量";

    private static String regixOfTheFirstColum2 = "井编号井类型纬度经度型号相连成线的井编号管道类型深度纬度偏移量经度偏移量";
    private static int BACKCOUNT = 10;
    private static int theColumFirstNull = 5;
    private static int columFirstNullEnd = 0;

    public static int pipe_black_count = 0;
    @Value("${pipe.splitSign}")
    private String splitSign;
    private Double latitudeOffset = 0d;//纬度
    private Double longitudeOffset = 0d; //经度


    /**
     * 根据文件魔数判断是否为Excel文件
     *
     * @param inputStream
     * @return
     */
    public void isExcelFile(InputStream inputStream) throws IOException {
        try {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);
            if (!Objects.equals(fileMagic, FileMagic.XML)
                    && !Objects.equals(fileMagic, FileMagic.OOXML)) {
                throw new FileTypeException("上传的文件格式错误");
            }
        } catch (IOException e) {
            throw new IOException("excel文件转换为FileMagic，发生IO错误");
        }
    }

    /**
     * 将MultipartFile转化Workbook
     *
     * @param multipartFile
     * @return
     */
    public Workbook multipartFileToWorkbook(MultipartFile multipartFile) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(multipartFile.getInputStream());
        } catch (Exception e) {
            throw new FileTypeException("读取excel文件，发生IO错误");
        }
        return workbook;
    }


    public int judgeRealSheet(Sheet sheet) {
        //根据列名判断，将列名存入StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        //获取第一行数据
        Row row = sheet.getRow(0);

        if (row == null){
            //本页为空页
            return -1;
        }
        //最后一行的列数
        int lastCellNum = row.getLastCellNum();
        //遍历第一行每一个单元格
        for (int j = 0; j < lastCellNum; j++) {
            //直到空为止
            if (judgeCellIsValid(row.getCell(j))) {
                continue;
            }
            //stringBuilder追加每一个cell值
            stringBuilder.append(row.getCell(j).getStringCellValue());
        }
        //比对正则，找到即返回
        if (regixOfTheFirstColum1.contains(stringBuilder)) {
            //符合——"井编号井类型经度纬度型号相连成线的井编号管道类型深度"
            return 1;
        } else if (regixOfTheFirstColum2.contains(stringBuilder)) {
            //符合——"井编号井类型纬度经度型号相连成线的井编号管道类型深度"
            return 2;
        }
        throw new ContextException("在上传的excel中，未找到符合约定的页面");
    }


    /**
     * 判断经度范围 0-180
     *
     * @return
     */
    public boolean judgeLongitude(String s) {
        double v;
        try {
            v = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new ConvertException("经度转换正常数值异常");
        }
        if (v >= 0 && v <= 180) {
            return true;
        }
        throw new ConvertException("经度范围异常");
    }

    /**
     * 判断纬度范围 0-90
     *
     * @return
     */
    public boolean judgeLatitude(String s) {
        double v;
        try {
            v = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new ConvertException("纬度转换正常数值异常");
        }
        if (v >= 0 && v <= 90) {
            return true;
        }
        throw new ConvertException("纬度范围异常");
    }

    /**
     * 判断单元格是否有效
     *
     * @param cell
     * @return 无效返回true，有效返回false
     */
    public boolean judgeCellIsValid(Cell cell) {
        //非空判断
        if (cell == null) return true;
        //将单元格类型改为String
        cell.setCellType(CellType.STRING);
        //存储数据是否为null
        if (judgeCellType(cell.getStringCellValue())) return true;
        return false;
    }

    /**
     * 找出为空的终点位置
     *
     * @param sheet
     * @return
     */
    public void judgeEndNull(Sheet sheet, int firstNull) {
        //获取第一行
        Row row = sheet.getRow(0);
        //总列数
        int cols = row.getLastCellNum();

        for (int i = firstNull; i < cols; i++) {
            //判断cell是否有效
            if (!judgeCellIsValid(row.getCell(i))) { //有效
                columFirstNullEnd = i;
                return;
            }
        }
    }


    /**
     * 将行转为Well对象
     *
     * @param row 行数
     * @param num 第一次为null的位置
     * @return
     */
    public ExcelWellVo serviceWellVo(Row row, int num, int kind) {
        //经纬度顺序可能是反的

        ExcelWellVo wellVo = new ExcelWellVo();
        //有效继续运行，无效则抛出异常
        judgeWellIsGood(row, num);
        //供测量经纬度是否有效使用
        String stringCellValue;
        String stringCellValue1;
        {
            if (kind == 1) {
                //经度
                stringCellValue = row.getCell(2).getStringCellValue();
                //纬度
                stringCellValue1 = row.getCell(3).getStringCellValue();
            } else {
                //经度
                stringCellValue = row.getCell(3).getStringCellValue();
                //纬度
                stringCellValue1 = row.getCell(2).getStringCellValue();
            }
            int s = row.getRowNum() + 1;
            if (judgeLongitude(stringCellValue) && judgeLatitude(stringCellValue1)) {
                BigDecimal[] bigDecimals = elimateOffset(stringCellValue, stringCellValue1);
                wellVo.setLongitude(bigDecimals[0]);
                wellVo.setLatitude(bigDecimals[1]);
                //井类型
                wellVo.setWellType(getCellValueAsString(row.getCell(1), "第" + s + "行井类型没有数据"));
                //型号
                wellVo.setModel(getCellValueAsString(row.getCell(4), "第" + s + "行型号没有数据"));
                return wellVo;
            }

        }
        return null;
    }

    public String getCellValueAsString(Cell cell, String s) {
        if (cell == null) {
            log.info(s);
            return null;
        }
        cell.setCellType(CellType.STRING);
        if (judgeCellType(cell.getStringCellValue())) {
            log.info(s);
            return null;
        }
        return cell.getStringCellValue();
    }

    public BigDecimal[] elimateOffset(String lo, String la) {
        BigDecimal[] bigDecimals = new BigDecimal[2];
        double[] doubles = wgs84_To_Gcj02(Double.parseDouble(lo), Double.parseDouble(la));
        if (doubles == null) {
            throw new ConvertException("转换异常");
        }
        bigDecimals[0] = BigDecimal.valueOf(doubles[0] + longitudeOffset);
        bigDecimals[1] = BigDecimal.valueOf(doubles[1] + latitudeOffset);
        return bigDecimals;
    }

    /**
     * 将行转为Pipe对象
     *
     * @param row         行
     * @param theFirtCell 起始位置
     * @return
     */
    int sadsd = 0;
    public ExcelPipeVo servicePipeVo(Row row, int theFirtCell) {
        ExcelPipeVo excelPipeVo = new ExcelPipeVo();
        //有效继续运行，无效则抛出异常
        judgePipeIsGood(row, theFirtCell);
        //相连成线的管道--为null
        if (judgeCellIsValid(row.getCell(theFirtCell))) {
            pipe_black_count++;
            log.info("第"+(row.getRowNum() + 1)+"相连成线的井编号为空");
            return null;
        }
        String stringCellValue = row.getCell(theFirtCell).getStringCellValue();
        int s = row.getRowNum() + 1;
        try {
            List<Long> collect = Arrays.stream(stringCellValue.split(splitSign))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            //判断collect是否为1个，1个进行报错
            if(collect.size() == 1) throw new TestException("第" + s + "相连成线的井编号只有单个井");
            excelPipeVo.setWellsId(collect);
        } catch (Exception e) {
            throw new ConvertException("第" + s + "行，相连成线的井编号格式异常");
        }
        //管道类型
        excelPipeVo.setPipeType(row.getCell(theFirtCell + 1).getStringCellValue());
        //深度
        String s1 = getCellValueAsString(row.getCell(theFirtCell + 2), "第" + (row.getRowNum() + 1) + "行深度没有数据");
        if (s1 != null) {
            excelPipeVo.setDeep(new BigDecimal(s));
        }
        return excelPipeVo;
    }

    /**
     * 判断pipe在这行是否有效
     *
     * @param row 行
     * @param num 起始点
     * @return
     */
    public void judgePipeIsGood(Row row, int num) {
        //遍历，这行需要提取的数据是否都有效
        for (int i = num; i < num + 3; i++) {
            if (i == num + 2 || i == num) continue;
            if (judgeCellIsValid(row.getCell(i))) {
                int s = row.getRowNum() + 1;
                throw new WellMissException("第" + s + "行，管道类型没有数据");
            }
            row.getCell(i).setCellType(CellType.STRING);
        }
    }

    /**
     * 判断Well在这行是否有效
     *
     * @param row 行
     * @param num 结束列数
     * @return
     */
    public void judgeWellIsGood(Row row, int num) {
        //遍历，这行需要提取的数据是否都有效
        for (int i = 0; i < num; i++) {
            Cell cell = row.getCell(i);
            if (judgeCellIsValid(cell)) {
                if (i == 1 || i == 4) {
                    continue;
                } else {
                    int s = row.getRowNum() + 1;
                    throw new CellTypeException("第" + s + "行必要数据缺失");
                }
            }
            row.getCell(i).setCellType(CellType.STRING);
        }
    }

    /**
     * 获取well实际行数
     *
     * @param sheet
     * @return
     */
    public int getRealWellRowNum(Sheet sheet) {
        //获取总行数
        int rows = sheet.getPhysicalNumberOfRows();
        //遍历每行
        for (int i = 1; i < rows + 1; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                return i;
            }
            //判断单元格是否有效
            if (judgeCellIsValid(row.getCell(0))) {
                return i;
            }
        }
        throw new DataIsNullException("找不到well真正的行数");
    }

    /**
     * 获取pipe真正的行数
     *
     * @param sheet
     * @return
     */
    public int getRealPipeRowNum(Sheet sheet) {
        //获取总行数
        int rows = sheet.getPhysicalNumberOfRows();
        int ins = 0;
        int in = 0;
        //遍历每行
        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            //判断单元格是否有效
            if (judgeCellIsValid(row.getCell(columFirstNullEnd))) {
                if (judgeCellIsValid(row.getCell(columFirstNullEnd + 1))) {
                    int m = 0;
                    //如果两个都为null的话，记录i并且调boolean为true
                    for (int j = i + 1; j < i + BACKCOUNT + 1 && j < rows; j++) {
                        Row row1 = sheet.getRow(j);
                        if (judgeCellIsValid(row1.getCell(columFirstNullEnd)) && judgeCellIsValid(row1.getCell(columFirstNullEnd + 1))) {
                            m++;
                        }
                    }
                    if (m == BACKCOUNT || m + i + 1 == rows) {
                        return i;
                    }
                }
            }
        }
        throw new DataIsNullException("找不到pipe真正的行数");
    }


    public List removeListNull(List list) {
        List arrayList = new ArrayList<>();
        list.stream().forEach(e -> {
            if (e != null) {
                arrayList.add(e);
            }
        });
        return arrayList;
    }

    /**
     * 读取文件
     *
     * @param multipartFile
     * @return
     */
    public ExcelVo readExcel(MultipartFile multipartFile) {
        ExcelVo excelVo = new ExcelVo();
        //转化为Workbook
        Workbook workbook = multipartFileToWorkbook(multipartFile);
        //找到合法的页数
        List<ExcelWellVo> excelWellVos = new ArrayList<>();
        List<ExcelPipeVo> excelPipeVos = new ArrayList<>();
        //表的总页数
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            //获取页
            Sheet sheet = workbook.getSheetAt(i);
            int i1 = judgeRealSheet(sheet);
            if (i1 == -1) continue;
            //间断的终点,改变全局变量的值
            judgeEndNull(sheet, theColumFirstNull);
            //读取excel文件中的偏移量，并赋值给 全局变量，没有数值则默认为0
            setGlobalOffset(sheet);
            //获取表中excel中的wells数据
            excelWellVos = removeListNull(readExcelWell(getRealWellRowNum(sheet), sheet, excelWellVos, i1));
            //获取表中excel中的pipes数据
            excelPipeVos = removeListNull(readExcelPipe(getRealPipeRowNum(sheet), sheet, excelPipeVos));
        }
        excelVo.setWellVos(excelWellVos);
        excelVo.setPipeVos(excelPipeVos);
        return excelVo;
    }

    public void setGlobalOffset(Sheet sheet){
        //获取第一行
        Row row = sheet.getRow(0);
        //总列数
        int cols = row.getLastCellNum();
        //从第一次为空的地方进行遍历
        for (int i = columFirstNullEnd+3; i < cols; i++) {
            Cell cell = row.getCell(i);
            //判断是否有效
            if (!judgeCellIsValid(cell) && "纬度偏移量".equals(cell.getStringCellValue())) { //有效
                //读取下一行的这个列的值
                row = sheet.getRow(1);
                cell = row.getCell(i);
                if (!judgeCellIsValid(cell)){
                    //设置该cell格式为String
                    cell.setCellType(CellType.STRING);
                    try{
                        latitudeOffset = Double.valueOf(cell.getStringCellValue());
                    }catch (NumberFormatException e){
                        throw new ConvertException("纬度偏移量数据非数值");
                    }
                }
                //读取下一个
                cell = row.getCell(i+1);
                if (!judgeCellIsValid(cell)){
                    //设置该cell格式为String
                    cell.setCellType(CellType.STRING);
                    try{
                        longitudeOffset = Double.valueOf(cell.getStringCellValue());
                    }catch (NumberFormatException e){
                        throw new ConvertException("经度偏移量数据非数值");
                    }
                }
                break;
            }
        }
    }

    /**
     * 读取文件中的Well
     *
     * @param rowNumber 实际行数
     * @param sheet     表
     * @return
     */
    public List<ExcelWellVo> readExcelWell(int rowNumber, Sheet sheet, List<ExcelWellVo> excelWellVos, int kind) {
        for (int i = 1; i < rowNumber; i++) {
            Row row = sheet.getRow(i);
            ExcelWellVo wellVo = serviceWellVo(row, theColumFirstNull, kind);
            excelWellVos.add(wellVo);
        }
        return excelWellVos;
    }

    /**
     * 读取文件中的Pipe
     *
     * @param rowNumber 实际行数
     * @param sheet     表
     * @return
     */
    public List<ExcelPipeVo> readExcelPipe(int rowNumber, Sheet sheet, List<ExcelPipeVo> excelPipeVos) {
        for (int i = 1; i < rowNumber; i++) {
            Row row = sheet.getRow(i);
            ExcelPipeVo excelPipeVo = servicePipeVo(row, columFirstNullEnd);
            excelPipeVos.add(excelPipeVo);
        }
        return excelPipeVos;
    }
}
