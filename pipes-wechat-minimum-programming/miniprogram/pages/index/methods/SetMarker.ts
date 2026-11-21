import { IAppOption } from "../../../../typings";
import { Maker } from "../../../entity/amap";
import {  Pipe, Well } from "../../../entity/pipe";
const app: IAppOption = getApp<IAppOption>()

/**
 * 渲染marker
 * @param well 
 */
export function getMakers(well: Array<Well>): Array<Maker> {
  if (well === undefined || well.length === 0) return new Array<Maker>();
  return well.map(element => {
    let maker: Maker = {
      id: element.id,
      longitude: element.longitude,
      latitude: element.latitude,
      width: 15,
      height: 15,
      iconPath: app.globalData.wellColorMap[element.model] == undefined ? app.globalData.wellColorMap.default : app.globalData.wellColorMap[element.model]
    }
    return maker;
  });
}
/**
 * 删除点击增加的marker
 * @param makers 
 */
export function isMarkerChange(makers:Array<Maker>):Array<Maker>|null{
  let marker: Array<Maker> = makers
  if (makers[makers.length - 1 ].iconPath === "https://kuangshen.xyz:1027/pipe-static/red.png") { 
    marker.pop()
    return marker
  }  
  return null
}
/**
 * 筛选marker类型
 * @param allMarker 
 * @param newPipe 
 */
export function filterMarker(allMarker: Array<Maker>, newPipe: Array<Pipe>): Array<Maker>  {
  if (allMarker.length === 0 || newPipe.length === 0) {
    throw new Error("maker或者管道没有传值！！！")
  }
  let markerSet: Set<Maker> = new Set()
  for (let i = 0; i < newPipe.length; i++) {
    let wells = newPipe[i].wellsId
    for (let index = 0; index < wells.length; index++) {
      const element = wells[index];
      // id值是从1开始的
      markerSet.add(allMarker[element - 1])
    }
  }
  return Array.from(markerSet)
}