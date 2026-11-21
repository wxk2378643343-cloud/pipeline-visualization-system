import { Coordinate } from "../entity/pipe";

const hexCharacters: string = '0123456789ABCDEF';

// Return a random rgb string, such as #FFFFFF
export function randomRGBColor(): string {
  let rgb: string = '#';
  for (let i = 0; i < 6; i++) {
    rgb += randomHexCharacter();
  }
  return rgb;
}

// Return a random coordinate which in given circle
export function randomCoordinateAroundPoint(point: Coordinate, radius: number): Coordinate {
  let randomX = randomDouble(-radius, radius)
  let maxY = (radius ^ 2 - randomX ^ 2) ^ 0.5
  let randomY = randomDouble(-maxY, maxY)
  return {
    longitude: point.longitude + randomY,
    latitude: point.latitude + randomX
  }
}

// Get now user's location. If you want to listen variation of user's location, don't use this function.
export async function getCoordinate(): Promise<Coordinate> {
  let res: any = await wx.getLocation({ type: 'gcj02' , interval: 2000,});
  let coordinate: Coordinate = {
    longitude: res.longitude,
    latitude: res.latitude
  }
  return coordinate;
}

// Just like this method's name
function randomHexCharacter(): string {
  let randomNum = randomInt(0, 16);
  return hexCharacters.charAt(randomNum)
}

// Return a pseudorandom number in the range (min,max) excluding max
function randomDouble(min: number, max: number) {
  return Math.random() * (max - min) + min;
}

// Return a pseudorandom number in the range (min,max) excluding max
function randomInt(min: number, max: number) {
  return Math.floor(Math.random() * (Math.floor(max) - Math.floor(min))) + Math.ceil(min);
}