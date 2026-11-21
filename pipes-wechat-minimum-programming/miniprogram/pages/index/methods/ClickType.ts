import { JudDistance } from "../../../entity/amap";
import { Coordinate } from "../../../entity/pipe";
import { Node } from "../../../entity/tree";
import { findNearestPointInfo, judgeLeafNode } from "../../../utils/FindNearestInfo";

/**
 * 判断返回井口还是管道
 * @param point 
 * @param node 
 * @param isLatitude 
 * @param typeList 
 */
export function makerRange(point: Coordinate, node: Node, isLatitude: boolean, typeList: Array<string> | null = null): JudDistance | null {
  // 先执行判断井口函数
  let findWell = findNearestPointInfo(point, node, isLatitude);  
  
  if (findWell !== null) {
    return {
      type: 'well',
      wellID: findWell.wellId
    }
  }
  // 判断管道
  let fineLine = judgeLeafNode(point, node, isLatitude, typeList)
  
  //  如果存在
  if (fineLine !== null) {
    return {
      line: fineLine.line,
      type: "line" 
    }
  }
  return null
}
