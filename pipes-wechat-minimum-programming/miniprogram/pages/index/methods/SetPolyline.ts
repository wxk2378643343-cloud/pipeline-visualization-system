import { IAppOption } from "../../../../typings";
import { Pipe, Polyline, Well } from "../../../entity/pipe";

const app: IAppOption = getApp<IAppOption>()

/**
 * Returns different color segments depending on the type
 * @param pipe 
 * @param well 
 * @param num 
 */
export function drawPolyline(pipe: Array<Pipe>, well: Array<Well>): Array<Polyline> {
  let polylineArr: Array<Polyline> = []
  for (let i = 0; i < pipe.length; i++) {
    let nowPipe: Pipe = pipe[i];
    let polyline: Polyline = {
      points: [],
      color: app.globalData.pipeColorMap[nowPipe.type] == undefined ? app.globalData.pipeColorMap.default : app.globalData.pipeColorMap[nowPipe.type],
      width: app.globalData.pipeWidth,
      borderWidth: 0,
      borderColor: '#FFFFFF'
    };
    polylineArr.push(polyline)
  }
  for (let i = 0; i < pipe.length; i++) {
    for (let temp = 0; temp < pipe[i].wellsId.length; temp++) {
      if (well[pipe[i].wellsId[temp] - 1] !== null) {
        polylineArr[i].points.push({
          latitude: well[pipe[i].wellsId[temp] - 1].latitude,
          longitude: well[pipe[i].wellsId[temp] - 1].longitude
        })
      }
    }
  }
  let newPolylineArr = polylineArr
  return newPolylineArr
}
