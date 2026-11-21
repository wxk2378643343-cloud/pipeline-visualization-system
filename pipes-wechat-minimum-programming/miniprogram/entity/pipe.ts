export interface Coordinate {
  longitude: number,
  latitude: number
}

export interface Well extends Coordinate {
  id: number,
  model: string,
  color?: string
  type: string
}

export interface Pipe {
  id: number,
  type: string,
  wellsId: Array<number>,
  material: string,
  diameter: number,
  color?: string
  deep: number
}

export interface PipeServiceResponse {
  code: number,
  msg: string,
  data?: object
}

export interface Polyline {
  points: Array<Coordinate>,
  color: string,
  width: number,
  borderWidth?: number,
  borderColor?: string
};

export interface Circle extends Coordinate {
  fillColor: string,//填充颜色
  color: string,//描边的颜色
  radius: number,//半径
  strokeWidth: number//描边的宽度
}

export interface ShowPipe {
  pipe?: Pipe
  polyline?: Polyline
  wellType?: Array<string>
  wellId?: number,
  type?: string
}


export interface PictureResponse {
  code: number,
  msg: string,
  data?: object
}