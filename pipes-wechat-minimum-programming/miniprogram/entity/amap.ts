import { Pipe, Well } from "./pipe";
import { Coordinate, Line } from "./tree";



export interface Maker extends Coordinate  {
  height?: number,
  iconPath: string,
  width?: number,
  id:number
}

export interface MinimumLineInfo {
  line: Line,
  distance: number
}
export interface JudDistance{
  line?:Line,
  wellID?:number,
  type:string
}

export interface requistData{
  pipes:Array<Pipe>,
  wells:Array<Well>
}

