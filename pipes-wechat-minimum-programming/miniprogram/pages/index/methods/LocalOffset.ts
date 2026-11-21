import { Polyline, Well } from "../../../entity/pipe"
// 这个文件夹是局部偏移的
/**
 * 局部井道偏移
 * @param well 
 * @param lat 
 * @param lon 
 */
export function addWellLoaction(well: Array<Well>, lat: number, lon: number): Array<Well> {
  let newWell: Array<Well> = well.map(item => {
    item.latitude += lat
    item.longitude += lon
    return item
  })
  return newWell
}
/**
 * 局部线偏移
 * @param polyline 
 * @param lat 
 * @param lon 
 */
export function addPolylineLoaction(polyline: Array<Polyline>, lat: number, lon: number): Array<Polyline> {
  let newPolyline: Array<Polyline> = polyline.map(item => {
    item.points.map(item => {
      item.latitude += lat,
        item.longitude += lon
      return item
    })
    return item
  })
  return newPolyline
}