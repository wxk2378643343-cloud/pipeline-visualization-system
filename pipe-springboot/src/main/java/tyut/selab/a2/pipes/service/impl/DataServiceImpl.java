package tyut.selab.a2.pipes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tyut.selab.a2.pipes.exception.CalulateException;
import tyut.selab.a2.pipes.exception.DataIsNullException;
import tyut.selab.a2.pipes.exception.WellMissException;
import tyut.selab.a2.pipes.service.*;
import tyut.selab.a2.pipes.utils.ExcelUtil;
import tyut.selab.a2.pipes.vo.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements IDataService {

    Node head = null;//树的头节点

    final static int FREE = 0;
    final static int RUNNING = 1;
    public int status = FREE;
    public int threadStatus = FREE;

    @Autowired
    private IPipeService pipeService;
    @Autowired
    private IWellService wellService;
    @Autowired
    private IConverterService converterService;
    @Autowired
    private IExcelService iExcelService;


    @Override
    public DataVo getAllPipeAndWellFromDB() {
        return new DataVo(converterService.pipeListTransformationDataPipeVoList(pipeService.getAllPipes()),
                converterService.WellListTransformationDataWellList(wellService.getAllWells()));
    }

    public DataVo multipartFileToExcelVo(MultipartFile multipartFile) {
        return iExcelService.resolveExcel(multipartFile);
    }

    public void excelVoIsNullThrowException(Object excelVo) {
        if (excelVo == null)
            throw new DataIsNullException("Object Is Null");

    }

    public void delectAllPipeAndWellDBData() {
        pipeService.delectPipes();
        wellService.delectAllWellsInDB();
    }

    public void excelPipeVoListInstertDB(List<DataPipeVo> pipeVos) {
        pipeService.importPipes(converterService.DataPipeVoListTransformationPipeList(pipeVos));
    }

    public void excelWellVoListInstertDB(List<DataWellVo> wellVos) {
        wellService.importWells(converterService.DataWellVoListTransformationWellList(wellVos));
    }

    public void resetTreeHeadIsNull() {
        head = null;
    }


    public void beforeImportDataToDB(DataVo dataVo) {
        excelVoIsNullThrowException(dataVo); //判断是否为空
        verifyDataCompleteness(dataVo); //数据校验
        delectAllPipeAndWellDBData(); //清除数据库的数据
        resetTreeHeadIsNull();
    }

    /**
     * 校验数据，防止有漏井的事件发生
     *
     * @param dataVo
     */
    public void verifyDataCompleteness(DataVo dataVo) {
        List<DataPipeVo> pipes = dataVo.getPipes();
        List<DataWellVo> wells = dataVo.getWells();
        for (int i = 0; i < pipes.size(); i++) {
           
            for (int j = 0; j < pipes.get(i).getWellsId().size(); j++) {
                int m = 0;
                try {
                    m = Math.toIntExact(pipes.get(i).getWellsId().get(j) - 1);
                wells.get(m);
                }catch (Exception e){
                    throw new WellMissException("第"+(i+2+ ExcelUtil.pipe_black_count) +"行管道数据中的第"+(m+1)+"号井口数据丢失");
                }
            }
            
        }
    }

    @Override
    public DataVo importExcelDataToDB(MultipartFile multipartFile) {
        if (threadStatus == RUNNING) {
            throw new CalulateException("Calulating");
        }
        iExcelService.beforeExcelResolve(multipartFile);
        DataVo dataVo = multipartFileToExcelVo(multipartFile);
        List<DataWellVo> wells = dataVo.getWells();
        for (int i = 0; i < wells.size(); i++) {
            wells.get(i).setId((long) (i + 1));
        }
        List<DataPipeVo> pipes = dataVo.getPipes();
        for (int i = 0; i < pipes.size(); i++) {
            pipes.get(i).setId((long) i + 1);
        }
        beforeImportDataToDB(dataVo);
        head = null;
        //计算树
        new Thread(() -> {
            threadStatus = RUNNING;
            calculateTree(dataVo);
            threadStatus = FREE;
        }).start();
        //上传到数据库
        new Thread(() -> {
            excelPipeVoListInstertDB(dataVo.getPipes());
            excelWellVoListInstertDB(dataVo.getWells());
        }).start();
        return dataVo;
    }

    @Override
    public Node calculateTree(DataVo dataVo) {
        if (status == RUNNING) {
            throw new CalulateException("Calulating");
        }
        status = RUNNING;
        //生成树
        if (head == null) {
            if (dataVo == null) {
                dataVo = new DataVo(converterService.pipeListTransformationDataPipeVoList(pipeService.getAllPipes()),
                        converterService.WellListTransformationDataWellList(wellService.getAllWells()));
            }
            Node node = new Node();
            recurision(node, true, culOriginLine(dataVo.getPipes(), dataVo.getWells()), dataVo.getWells());
            head = node;
        }
        status = FREE;
        return head;
    }

    public Node setEndNodeValue(Node node, List<Line> lines) {
        node.setLeft(null);
        node.setRight(null);
        node.setPipes(lines);
        node.setPoint(null);
        return node;
    }

    public List<DataWellVo> sortByLongitude(List<DataWellVo> coordinateList) {
        return coordinateList.stream().sorted(Comparator.comparing(DataWellVo::getLongitude)).collect(Collectors.toList());
    }

    public List<DataWellVo> sortByLatitude(List<DataWellVo> coordinateList) {
        return coordinateList.stream().sorted(Comparator.comparing(DataWellVo::getLatitude)).collect(Collectors.toList());
    }

    public Node resetNextNode(Node node, DataWellVo centerPoint) {
        node.setWellId(centerPoint.getId());
        //中点存入节点内
        node.setPoint(new Coordinate(centerPoint.getId(), centerPoint.getLongitude(),centerPoint.getLatitude()));
        node.setRight(new Node());
        node.setLeft(new Node());

        return node;
    }

    /**
     * 递归构造树
     *
     * @param node  节点
     * @param is    true-纬度  ， false-经度
     * @param lines 线的集合（关系）
     * @return
     */
    public Node recurision(Node node, boolean is, List<Line> lines, List<DataWellVo> pointList) {
        List<DataWellVo> pointList2;
        //非空判断,如果点集的size为0，则到达叶子节点，叶子节点只存储关系线，其余为空对象
        if (pointList.size() == 0) {
            return setEndNodeValue(node, lines);
        }
        //开始计算
        if (is) {
            //排序（根据经度）
            pointList = sortByLongitude(pointList);
            pointList2 = sortByLongitude(pointList);
            //中点的位置
            int size = pointList.size() / 2;
            node.setModel(pointList.get(size).getModel());
            Node nestNod = resetNextNode(node, pointList.get(size));
            //递归,先小，后大
            recurision(nestNod.getLeft(), false, culLitterLine(nestNod.getPoint(), lines, true), pointList.subList(0, size));
            recurision(nestNod.getRight(), false, culBigLine(nestNod.getPoint(), lines, true), pointList2.subList(size + 1, pointList2.size()));
        } else {
            //排序（根据纬度）
            pointList = sortByLatitude(pointList);
            pointList2 = sortByLatitude(pointList);
            //中点的位置
            int size = pointList.size() / 2;
            node.setModel(pointList.get(size).getModel());
            Node nestNod = resetNextNode(node, pointList.get(size));
            //递归,先小，后大
            recurision(nestNod.getLeft(), true, culLitterLine(nestNod.getPoint(), lines, false), pointList.subList(0, size));
            recurision(nestNod.getRight(), true, culBigLine(nestNod.getPoint(), lines, false), pointList2.subList(size + 1, pointList2.size()));
        }
        return null;
    }


    /**
     * 构造线数据集合
     *
     * @return
     */
    public List<Line> culOriginLine(List<DataPipeVo> allPipes, List<DataWellVo> wellVos) {
        DataPipeVo pipe;
        List<Long> wellsId;
        DataWellVo first, second;
        List<Line> lines = new ArrayList<>();
        Coordinate coordinateSecond = null;
        for (int i = 0; i < allPipes.size(); i++) {
            pipe = allPipes.get(i);
            wellsId = pipe.getWellsId();
            if (wellsId.size() == 1 || wellsId.size() == 0) {
                continue;
            }
            for (int j = 0; j < wellsId.size() - 1; j++) {
                first = wellVos.get(Integer.parseInt(wellsId.get(j).toString()) - 1);
                Line line = new Line();
                line.setPipeId((long) (i + 1));
                if (j == 0) {
                    Coordinate coordinateFirst = new Coordinate();
                    coordinateFirst.setLatitude(first.getLatitude());
                    coordinateFirst.setLongitude(first.getLongitude());
                    line.setFirst(coordinateFirst);
                } else {
                    line.setFirst(coordinateSecond);
                }

                if (j + 1 != wellsId.size()) {
                    coordinateSecond = new Coordinate();
                    second = wellVos.get(Integer.parseInt(wellsId.get(j + 1).toString()) - 1);
                    coordinateSecond.setLatitude(second.getLatitude());
                    coordinateSecond.setLongitude(second.getLongitude());
                    line.setSecond(coordinateSecond);
                }
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 构造点集
     *
     * @return
     */
    public List<Coordinate> culOriginPoint(List<DataWellVo> allWells) {
//        List<Well> allWells = wellService.getAllWells();
        List<Coordinate> coordinates = new ArrayList<>();
        DataWellVo dataWellVo;
        for (int i = 0; i < allWells.size(); i++) {
            dataWellVo = allWells.get(i);
            coordinates.add(new Coordinate(dataWellVo.getId(), dataWellVo.getLongitude(), dataWellVo.getLatitude()));
        }
        return coordinates;
    }


    public List<Line> addLineListByJudgeLineIsCrossLitterAreaLa(Coordinate coordinate, Line line, List<Line> leftLines) {
        //首先判断，第一个节点是否为null
        Coordinate first = line.getFirst();
        Coordinate second = line.getSecond();
        //如果纬度相等的话，意味着该点在线上，或者说就是节点
        if (first.getLatitude().compareTo(coordinate.getLatitude()) == 0) {
            //在哪个区域，看第二个节点在哪边
            if (second.getLongitude().compareTo(coordinate.getLongitude()) < 0) {
                leftLines.add(line);
            }
            //判断该第二个节点是否为在该纬线上
        } else if (second.getLatitude().compareTo(coordinate.getLatitude()) == 0) {
            //在哪个区域，看第一个节点在哪边
            if (first.getLongitude().compareTo(coordinate.getLongitude()) < 0) {
                leftLines.add(line);
            }
            //两个节点都不在该纬线上，则根据老办法（4中情况）
        } else {
            if (first.getLongitude().compareTo(coordinate.getLongitude()) < 0) {
                leftLines.add(line);
            } else if (second.getLongitude().compareTo(coordinate.getLongitude()) < 0) {
                leftLines.add(line);
            }
        }
        return leftLines;
    }


    public List<Line> addLineListByJudgeLineIsCrossLitterAreaLo(Coordinate coordinate, Line line, List<Line> leftLines) {
        //首先判断，第一个节点是否为null
        Coordinate first = line.getFirst();
        Coordinate second = line.getSecond();
        //如果经度相等的话，意味着该点在线上，或者说就是节点
        if (first.getLongitude().compareTo(coordinate.getLongitude()) == 0) {
            //在哪个区域，看第二个节点在哪边
            if (second.getLatitude().compareTo(coordinate.getLatitude()) < 0) {
                leftLines.add(line);
            }
            //判断该第二个节点是否为在该纬线上
        } else if (second.getLongitude().compareTo(coordinate.getLongitude()) == 0) {
            //在哪个区域，看第一个节点在哪边
            if (first.getLatitude().compareTo(coordinate.getLatitude()) < 0) {
                leftLines.add(line);
            }
            //两个节点都不在该纬线上，则根据老办法（4中情况）
        } else {
            if (first.getLatitude().compareTo(coordinate.getLatitude()) < 0) {
                leftLines.add(line);
            } else if (second.getLatitude().compareTo(coordinate.getLatitude()) < 0) {
                leftLines.add(line);
            }
        }
        return leftLines;
    }


    /**
     * 计算小的线的集合
     *
     * @param coordinate
     * @param lines
     * @return
     */
    public List<Line> culLitterLine(Coordinate coordinate, List<Line> lines, boolean is) {
        List<Line> leftLines = new ArrayList<>();
        //纬度
        if (is) {
            lines.stream().filter(line -> {

                addLineListByJudgeLineIsCrossLitterAreaLa(coordinate, line, leftLines);
                return true;
            }).collect(Collectors.toList());
        } else {
            //经度
            lines.stream().filter(line -> {
                addLineListByJudgeLineIsCrossLitterAreaLo(coordinate, line, leftLines);
                return true;
            }).collect(Collectors.toList());
        }
        return leftLines;
    }


    public List<Line> addLineListByJudgeLineIsCrossBigAreaLa(Coordinate coordinate, Line line, List<Line> leftLines) {
        //首先判断，第一个节点是否为null
        Coordinate first = line.getFirst();
        Coordinate second = line.getSecond();
        //如果相等的话，意味着该点在线上，或者说就是节点
        if (first.getLatitude().compareTo(coordinate.getLatitude()) == 0) {
            //在哪个区域，看第二个节点在哪边
            if (second.getLongitude().compareTo(coordinate.getLongitude()) >= 0) {
                leftLines.add(line);
            }
            //判断该第二个节点是否为在该纬线上
        } else if (second.getLatitude().compareTo(coordinate.getLatitude()) == 0) {
            //在哪个区域，看第一个节点在哪边
            if (first.getLongitude().compareTo(coordinate.getLongitude()) >= 0) {
                leftLines.add(line);
            }
            //两个节点都不在该纬线上，则根据老办法（4中情况）
        } else {
            if (first.getLongitude().compareTo(coordinate.getLongitude()) > 0) {
                leftLines.add(line);
            } else if (second.getLongitude().compareTo(coordinate.getLongitude()) > 0) {
                leftLines.add(line);
            }
        }
        return leftLines;
    }


    public List<Line> addLineListByJudgeLineIsCrossBigAreaLo(Coordinate coordinate, Line line, List<Line> leftLines) {
        //首先判断，第一个节点是否为null
        Coordinate first = line.getFirst();
        Coordinate second = line.getSecond();
        //如果经度相等的话，意味着该点在线上，或者说就是节点
        if (first.getLongitude().compareTo(coordinate.getLongitude()) == 0) {
            //在哪个区域，看第二个节点在哪边
            if (second.getLatitude().compareTo(coordinate.getLatitude()) >= 0) {
                leftLines.add(line);
            }
            //判断该第二个节点是否为在该纬线上
        } else if (second.getLongitude().compareTo(coordinate.getLongitude()) == 0) {
            //在哪个区域，看第一个节点在哪边
            if (first.getLatitude().compareTo(coordinate.getLatitude()) >= 0) {
                leftLines.add(line);
            }
            //两个节点都不在该纬线上，则根据老办法（4中情况）
        } else {
            if (first.getLatitude().compareTo(coordinate.getLatitude()) > 0) {
                leftLines.add(line);
            } else if (second.getLatitude().compareTo(coordinate.getLatitude()) > 0) {
                leftLines.add(line);
            }
        }
        return leftLines;
    }


    /**
     * 计算大的线的集合
     *
     * @param coordinate
     * @param lines
     * @return
     */
    public List<Line> culBigLine(Coordinate coordinate, List<Line> lines, boolean is) {
        List<Line> leftLines = new ArrayList<>();
        //纬度
        if (is) {
            lines.stream().filter(line -> {
                addLineListByJudgeLineIsCrossBigAreaLa(coordinate, line, leftLines);
                return true;
            }).collect(Collectors.toList());
        } else {
            lines.stream().filter(line -> {
                addLineListByJudgeLineIsCrossBigAreaLo(coordinate, line, leftLines);
                return true;
            }).collect(Collectors.toList());
        }

        return leftLines;
    }
}
