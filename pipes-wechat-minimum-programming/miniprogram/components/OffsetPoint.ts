import { Coordinate } from "../entity/tree";

// components/OffsetPoint.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    longitude:0,
    latitude:0,
  },
  getOffsetPoint:function():void{
    let coordinate : Coordinate={
      latitude : this.data.latitude,
      longitude : this.data.longitude
    }     
    this.triggerEvent("getOffsetPoint",coordinate)
  },
  null:function():void{
    
  }
})