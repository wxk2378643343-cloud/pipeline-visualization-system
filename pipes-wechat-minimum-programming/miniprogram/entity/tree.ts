export class Coordinate {
  longitude: number;
  latitude: number;
  constructor(longitude: number, latitude: number) {
    this.longitude = longitude;
    this.latitude = latitude;
  }
}

export class Line {
  pipeId!: number;
  first: Coordinate;
  second: Coordinate;
  constructor(first: Coordinate, second: Coordinate) {
    this.first = first;
    this.second = second
  }
}

export class Node {
  wellId: number | undefined;
  point: Coordinate | undefined;
  model: string | undefined;
  left: Node | undefined;
  right: Node | undefined;
  pipes: Array<Line> | undefined;
  constructor(point?: Coordinate) {
    this.point = point
  }
}


export interface MinimumLineInfo {
  line: Line,
  distance: number
} 

export interface MinimumPointInfo {
  wellId: number,
  wellModel: string,
  distance: number
}

export interface NearestInfo {
  line: MinimumLineInfo | null,
  point: MinimumPointInfo | null
}