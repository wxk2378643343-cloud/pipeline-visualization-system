//计算面积
/**
 *  param (x1,y1) (x2,y2) (x3,y3) Three points
 *  Return the triangle area enclosed by three points
 * */
function calculatedSquare(x1:number,y1:number,x2:number,y2:number,x3:number,y3:number):number{
    let square = 0.5*Math.abs(x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2))
    return square
}

/**
 * param (x1,y1)  (x2,y2)  two points
 * Return the distance between two points
 */
export function calculatedLength(x1:number,y1:number,x2:number,y2:number):number{
    let xLength = Math.abs(x1-x2)
    let yLength = Math.abs(y1-y2)
    let length = Math.sqrt(xLength*xLength+yLength*yLength)
    return length
}
/**
 * param square length
 * Returns the distance from a point to a line segment
 * */
function calculatedHeight(square:number,length:number):number{
    let height = (2*square)/length
    return height
}

/**
 * param (x1,y1) ,(x2,y2),(x3,y3)
 * Return the cos value to determine whether the triangle formed by three points is obtuse
 * */
function calculatedCosine(x1:number,y1:number,x2:number,y2:number,x3:number,y3:number):number{
    let aLength = calculatedLength(x2,y2,x3,y3);
    let bLength = calculatedLength(x1,y1,x3,y3)
    let cLength = calculatedLength(x1,y1,x2,y2)
    let cos = (aLength*aLength+cLength*cLength-bLength*bLength)/(2*aLength*cLength)
    return cos
}
/**
 * param (x1,y1) ,(x2,y2),(x3,y3)
 * Returns the distance from a point to a line segment
 * */
export function calculatedDistance(x1:number,y1:number,x2:number,y2:number,x3:number,y3:number):number{
    if(calculatedCosine(x1,y1,x2,y2,x3,y3) >=0 && calculatedCosine(x2,y2,x1,y1,x3,y3)>=0){
        return calculatedHeight(calculatedSquare(x1,y1,x2,y2,x3,y3),calculatedLength(x1,y1,x2,y2))
    }else {
        return calculatedLength(x1,y1,x3,y3)>calculatedLength(x2,y2,x3,y3) ? calculatedLength(x2,y2,x3,y3) : calculatedLength(x1,y1,x3,y3)
    }
}

                                  
