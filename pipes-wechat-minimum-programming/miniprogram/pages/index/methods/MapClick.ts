import { IAppOption } from "../../../../typings";
import { Coordinate, Pipe, Polyline, ShowPipe, Well } from "../../../entity/pipe";
import { Node } from "../../../entity/tree";
import { JudDistance, } from "../../../entity/amap";
import { makerRange } from "./ClickType";

const app: IAppOption = getApp<IAppOption>()

/**
 * 增加点或线
 * @param clickPoint 
 * @param tree 
 * @param pipeType 
 * @param pipes 
 * @param well 
 */
export function addLine(clickPoint: Coordinate, tree: Node, pipeType: Array<string> | null, pipes: Array<Pipe>, well: Array<Well>): ShowPipe | null {
  
  // 获取返回类型（井口/管道）
  let pointLine: JudDistance | null = makerRange(clickPoint, tree, false, pipeType);
  
  // 井口类型
  let wellTypeMap = new Map()
  // 如果有返回类型
  if (pointLine !== null) {
    // 如果返回类型为井口
    if (pointLine.type === "well") {
      return {
        wellId: pointLine.wellID,
        type: "well"
      }
    }
    // 如果返回类型是管道
    if (pointLine.type === "line") {
      // 确保线存在
      if (pointLine.line !== undefined) {        
        // 查询管道id
        let pipe = pipes[pointLine.line.pipeId - 1]
        // 增加新的线
        let polyline: Polyline = {
          points: [],
          color: "#DC143C",
          width: app.globalData.pipeClickedWidth
        }
        
        for (let index = 0; index < pipe.wellsId.length; index++) {
          // 因为数组下标重0开始
          let wellId = pipe.wellsId[index] - 1                    
          if (well[wellId].model !== undefined) {
            let num: number = 1
            if (wellTypeMap.has(well[wellId].model)) {
              num++
              wellTypeMap.set(well[wellId].model, num)
            } else {
              num = 1
              wellTypeMap.set(well[wellId].model, num)
            }
          }
          polyline.points.push({
            longitude: well[wellId].longitude,
            latitude: well[wellId].latitude
          })
        }
        let wellTypeArr :Array<string>= []
        for (let key of wellTypeMap.keys()) {
          wellTypeArr.push(key)
        }
        return {
          pipe: pipe,
          polyline: polyline,
          wellType: wellTypeArr,
          type: 'line'
        }
      }
    }
  }
  return null
}


