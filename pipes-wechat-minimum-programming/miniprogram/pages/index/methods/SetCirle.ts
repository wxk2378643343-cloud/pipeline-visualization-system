import { IAppOption } from "../../../../typings";
import { Well, Circle } from "../../../entity/pipe";
//Value for color and type
const app: IAppOption = getApp<IAppOption>()

/**
 * Return circle
 * @param num 
 * @param wells 
 */
export function drawCircle(wells: Array<Well>): Array<Circle> {
  let circles1: Array<Circle> = []
  let wellsData = wells;
  for (let i = 0; i < wellsData.length; i++) {
    let oneWell = wellsData[i];
    circles1.push({
      //在地图上显示圆
      latitude: oneWell.latitude,
      longitude: oneWell.longitude,
      fillColor: app.globalData.wellColorMap[oneWell.model] == undefined ? app.globalData.wellColorMap["default"] : app.globalData.wellColorMap[oneWell.model],//填充颜色
      color: "#7cb5ec88",//描边的颜色
      radius: app.globalData.wellrRadius,//半径
      // strokeWidth: //描边的宽度
    } as Circle)
  }
  return circles1
}