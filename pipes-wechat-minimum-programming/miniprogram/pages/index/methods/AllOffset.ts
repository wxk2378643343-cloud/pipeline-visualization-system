import { Maker } from "../../../entity/amap";
import { Polyline } from "../../../entity/pipe";

let previousLog: number = 0;
let previousLat: number = 0;

export function addMakerLoaction(markers: Array<Maker>, lat: number, lon: number): Array<Maker> {
  let newWell: Array<Maker> = markers.map(item => {
    item.latitude = item.latitude + lat - previousLat
    item.longitude = item.longitude + lon - previousLog
    return item
  })
  return newWell
}

export function addPolylineLoaction(polyline: Array<Polyline>, lat: number, lon: number): Array<Polyline> {
  let newPolyline: Array<Polyline> = polyline.map(ployline => {
    ployline.points = ployline.points.map(point => {
      point.latitude = point.latitude + lat - previousLat
      point.longitude = point.longitude + lon - previousLog
      return point
    })
    return ployline
  })
  previousLog = lon;
  previousLat = lat;
  return newPolyline
}