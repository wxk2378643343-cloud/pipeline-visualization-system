import { Pipe } from "../../../entity/pipe";
/**
 * 查找管道类型
 * @param well 
 */
export function setPipe(pipe: Array<Pipe>): Array<string> {
  let wellTypeMap = new Map();
  let num = 1;
  for (let i = 0; i < pipe.length; i++) {
    if (wellTypeMap.has(pipe[i].type)) {
      num++
      wellTypeMap.set(pipe[i].type, num)
    } else {
      num = 1
      wellTypeMap.set(pipe[i].type, num)
    }
  }
  let wellTypeArr = []
  for (let key of wellTypeMap.keys()) {
    wellTypeArr.push(key)
  }
  return wellTypeArr
}
/**
 * 
 * @param strArr 
 * @param pipe 
 */
export function partShow(strArr:Array<string>,pipe:Array<Pipe>):Array<Pipe>{
  let pipeArr:Array<Pipe> = []
  for (let index = 0; index < strArr.length; index++) {
    for (let m = 0; m < pipe.length; m++) {      
      if(strArr[index] == pipe[m].type){
        pipeArr.push(pipe[m])
      }
    }
  }
  return pipeArr
}