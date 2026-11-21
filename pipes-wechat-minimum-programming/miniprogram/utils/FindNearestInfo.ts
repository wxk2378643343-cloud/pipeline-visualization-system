// import {TREE_HEAD} from './testNum';
import { Coordinate, Line, Node } from "../entity/tree"
import { calculatedDistance, calculatedLength } from "./CalculateUtils"
import { IAppOption } from "../../typings";
import { Pipe } from "../entity/pipe";
import { MinimumLineInfo, MinimumPointInfo, NearestInfo } from "../entity/tree";

const app: IAppOption = getApp<IAppOption>()
const ERROR_LINE_RANGE = app.globalData.clickPipeRange;
const ERROR_POINT_RANGE = app.globalData.clickWellRange;

/**
 * @deprecated Because this function need visit tree twice, so it is deprecated
 */
export function findNearestInfo(point: Coordinate, node: Node, isLatitude: boolean, typeList: Array<string> | null = null): NearestInfo {
  throw new Error("This function is deprecated");

  const nearestInfo: NearestInfo = {
    line: judgeLeafNode(point, node, isLatitude, typeList),
    point: findNearestPointInfo(point, node, isLatitude)
  }
  return nearestInfo;
}

/**
 * 通过树查找距离最短且在误差范围内的线
 */
export function judgeLeafNode(point: Coordinate, node: Node, isLatitude: boolean, typeList: Array<string> | null = null): MinimumLineInfo | null {
  // 如果是叶子节点
  if (node.point === undefined) {
    let lines: Array<Line> = node.pipes!;
    // 如果此区域没有节点
    if (lines.length == 0) {
      return null;
    }

    // 如果有限制类型
    if (typeList !== null) {
      lines = lines.filter(nowLine => {
        let pipeIndex: number = nowLine.pipeId! - 1;
        let nowPipe: Pipe = app.globalData.allPipes[pipeIndex];
        return typeList.includes(nowPipe.type)
      })
    }

    // 找最近的线
    let minInfo: MinimumLineInfo = judgeMin(lines, point);

    // 如果在误差范围内则返回管道数据，否则返回null
    return minInfo.distance < ERROR_LINE_RANGE ? minInfo : null;
  }

  // 判断下一个子节点
  let isBigRegion: boolean = false;
  if (isLatitude && point.latitude >= ((node.point) as Coordinate).latitude) {
    isBigRegion = true;
  } else if (!isLatitude && point.longitude >= ((node.point) as Coordinate).longitude) {
    isBigRegion = true;
  }

  // walk the tree
  let nextNode: Node = (isBigRegion ? node.right : node.left) as Node;
  let minimumLineInfo: MinimumLineInfo = judgeLeafNode(point, nextNode, !isLatitude, typeList)!;

  // 回溯半径
  let radius: number = minimumLineInfo === null ? ERROR_LINE_RANGE : minimumLineInfo.distance;

  // 如果与切割线不相交
  if (isLatitude && Math.abs(point.latitude - ((node.point) as Coordinate).latitude) > radius) {
    return minimumLineInfo;
  }
  if (!isLatitude && Math.abs(point.longitude - ((node.point) as Coordinate).longitude) > radius) {
    return minimumLineInfo;
  }

  // 如果相交
  nextNode = (!isBigRegion ? node.right : node.left) as Node
  let anotherMinimumLineInfo: MinimumLineInfo | null = judgeLeafNode(point, nextNode, !isLatitude, typeList)

  // 如果没找见
  if (anotherMinimumLineInfo === null) {
    return minimumLineInfo
  }
  // 如果找见了,且之前为null
  if (minimumLineInfo === null) {
    return anotherMinimumLineInfo;
  }
  // 如果找见了，且之前不为null
  return anotherMinimumLineInfo.distance > minimumLineInfo.distance ? minimumLineInfo : anotherMinimumLineInfo;
}

export function findNearestPointInfo(point: Coordinate, node: Node, isLatitude: boolean, typeList: Array<string> | null = new Array<string>()): MinimumPointInfo | null {

  let typeSet: Set<string> | null = null;
  let defaultModel: string | null = null;
  if (typeList != null && typeList.length != 0) {
    let typeSet: Set<string> = new Set<string>();
    typeList.forEach((str) => typeSet.add(str));
    defaultModel = typeList[0];
  }
  let info: MinimumPointInfo = findNearestPointInfoAlgorithm(point, node, isLatitude, typeSet, defaultModel);
  
  return info.distance > ERROR_POINT_RANGE ? null : info;
}

/**
 * 通过树查找点击中最近的点
 */
function findNearestPointInfoAlgorithm(point: Coordinate, node: Node, isLatitude: boolean, typeSet: Set<string> | null, defaultModleInSet: string | null): MinimumPointInfo {
  // 判断下一个子节点
  let isBigRegion: boolean = false;

  if (isLatitude && point.latitude >= node.point!.latitude) {
    isBigRegion = true;
  } else if (!isLatitude && point.longitude >= node.point!.longitude) {
    isBigRegion = true;
  }

  // 获取下一个子节点
  let nextNode: Node = isBigRegion ? node.right! : node.left!;

  // 当前节点信息
  let nowPointInfo: MinimumPointInfo = {
    wellId: node.wellId!,
    wellModel: node.model!,
    distance: distance(node.point!, point)
  }
  if (typeSet != null && !typeSet.has(nowPointInfo.wellModel)) {
    nowPointInfo.distance = 200114514;
    nowPointInfo.wellModel = defaultModleInSet!;
  }

  // 如果没有井了
  if (nextNode.point == undefined) {
    return nowPointInfo
  }

  // walk the tree
  let minimumPointInfo: MinimumPointInfo = findNearestPointInfoAlgorithm(point, nextNode, !isLatitude, typeSet, defaultModleInSet);
  minimumPointInfo = minimumPointInfo.distance < nowPointInfo.distance ? minimumPointInfo : nowPointInfo;



  // 回溯半径
  let radius: number = minimumPointInfo.distance < ERROR_POINT_RANGE ? minimumPointInfo.distance : ERROR_POINT_RANGE;

  // 如果与切割线不相交
  if (isLatitude && Math.abs(point.latitude - node.point!.latitude) >= radius || !isLatitude && Math.abs(point.longitude - node.point!.longitude) >= radius) {
    return minimumPointInfo;
  }

  // 获取另一边
  nextNode = !isBigRegion ? node.right! : node.left!

  // 如果没有井了
  // if (nextNode.point == undefined)
  //   return minimumPointInfo;
  // let anotherMinimumPointInfo: MinimumPointInfo = findNearestPointInfo(point, nextNode, !isLatitude)


  // 如果另一边没有点
  if (nextNode.point == undefined) {
    return minimumPointInfo;
  }

  // 如果另一边有点
  let anotherMinimumPointInfo: MinimumPointInfo = findNearestPointInfoAlgorithm(point, nextNode, !isLatitude, typeSet, defaultModleInSet);
  return anotherMinimumPointInfo.distance > minimumPointInfo.distance ? minimumPointInfo : anotherMinimumPointInfo;
}

/**
 * 判断最小的距离
 */
function judgeMin(line: Array<Line>, point: Coordinate): MinimumLineInfo {
  let temp: number = 0;
  let minDistance: number = 99999999;

  for (let i: number = 0; i < line.length; i++) {
    let distance = calculatingDistance(line[i], point);
    if (minDistance > distance) {
      minDistance = distance;
      temp = i
    }
  }
  return {
    line: line[temp],
    distance: minDistance
  } as MinimumLineInfo
}

/**
 *
 * @param line
 * @param point
 * @constructor
 */
function calculatingDistance(line: Line, point: Coordinate): number {
  return calculatedDistance(line.first.longitude, line.first.latitude, line.second.longitude, line.second.latitude, point.longitude, point.latitude)
}

/**
 *
 * @param nodePoint
 * @param point
 */
function distance(nodePoint: Coordinate, point: Coordinate): number {
  return calculatedLength(nodePoint.longitude, nodePoint.latitude, point.longitude, point.latitude)
}