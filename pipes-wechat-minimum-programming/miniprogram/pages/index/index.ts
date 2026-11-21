// index.ts
import { getCoordinate } from "../../utils/pipes_utils";
import { Maker, requistData, } from "../../entity/amap";
import { Coordinate, Pipe, Polyline, Well,  ShowPipe } from "../../entity/pipe";
// import { drawCircle } from "./methods/wellTransformationCircle"
import { drawPolyline } from "./methods/SetPolyline"
import { requistPipes, rerequestTree, uploadFild } from "./methods/DataResponse"
import { filterMarker, getMakers, isMarkerChange,  } from "./methods/SetMarker";
import { Node } from "../../entity/tree";
import { IAppOption } from "../../../typings";
import { addLine } from "./methods/MapClick";
import { partShow, setPipe } from "./methods/SetPipe";


// import { isMarkerChange } from "./methods/MarkerChange";



const app: IAppOption = getApp<IAppOption>()

Page({
  data: {
    // 微信自带
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    canIUseGetUserProfile: false,
    canIUseOpenData: wx.canIUse('open-data.type.userAvatarUrl') && wx.canIUse('open-data.type.userNickName'),
    // 项目信息
    markers: [] as Array<Maker>,
    latitude: 0 as number,
    longitude: 0 as number,
    // 页面下面展示的信息
    textData: {} as object,
    // 画线
    polylines: [] as Array<Polyline>,
    // 井盖画圆圈展示的圆圈
    // circles: [] as Array<Circle>,
    // 隐藏
    hidden: false as boolean,
    // 井的全部信息
    // wells: [] as Array<Well>,
    // 全部管道类型
    pipeType: [] as Array<string>,
    // 用于展示的管道
    pipes: [] as Array<Pipe>,
    // 查询使用的管道的类型
    searchPipes: [] as Array<string>,
    // 存储全部树
    tree: {} as Node,
    //点击上移
    up: true as boolean,
    OffsetPointBoolen: app.globalData.OffsetPointBoolen,
    markerClickEvent: true as boolean,
  },
  async onLoad() {
    // @ts-ignore
    if (wx.getUserProfile) {
      this.setData({
        canIUseGetUserProfile: true
      })
    }
    await getCoordinate().then((coordinate: Coordinate) => {
      this.setData({
        longitude: coordinate.longitude,
        latitude: coordinate.latitude
      } as object)
    })
    // 获取当前位置
    // 展示全部
    this.initilize();

  },
  initilize: async function (): Promise<void> {
    wx.showLoading({
      title: '加载中',
      mask: true //开启蒙版遮罩
    });
    await this.setMessage();
    wx.hideLoading();
  },
  /**
   * 展示全部信息
   */
  setMessage: async function () {
    let allSData: requistData = await requistPipes("/data/get");
    let requist: Node = await rerequestTree("/data/tree")
    // 管道
    let data: Array<Pipe> = allSData.pipes
    app.globalData.allPipes = data
    // 井盖
    let wellData: Array<Well> = allSData.wells
    app.globalData.allWells = wellData
    // 展示管道 
    this.setData({
      // 页面展示的管道
      pipes: data,
    } as object)
    let marker: Array<Maker> = getMakers(app.globalData.allWells)
    let tree: Node = (requist) as Node
    this.setData({
      tree: tree,
      markers: marker,
    })
    // 展示所有管道
    this.drawPoly(app.globalData.allPipes)
    //画管道
    this.setPipes()
  },
  /**
   * 
   * @param pipe_width 
   */
  drawPoly: function (pipe: Array<Pipe>) {
    let polyline: Array<Polyline> = drawPolyline(pipe, app.globalData.allWells)
    this.setData({
      polylines: polyline,
    })
  },

  /**
   * Upload excel form
   */
  UploadFile: async function (): Promise<void> {
    this.removeDisplay()
    let uploadSData: requistData = await uploadFild("/data/import")
    wx.showLoading({
      title: '加载中',
      mask: true //开启蒙版遮罩
    });
    // 管道
    let data: Array<Pipe> = uploadSData.pipes
    app.globalData.allPipes = data
    // 井盖
    let wellData: Array<Well> = uploadSData.wells
    app.globalData.allWells = wellData
    let requist: Node = await rerequestTree("/data/tree")
    let tree: Node = requist
    let marker: Array<Maker> = getMakers(wellData)
    // 展示管道 
    this.setData({
      // 页面展示的管道
      pipes: data,
      // 全部树
      tree: tree,
      markers: marker,
    } as object)
    // 展示所有管道
    this.drawPoly(app.globalData.allPipes)
    //画管道
    this.setPipes()

  },
  /**
   * Pipeline query
   */
  searchPipe: function () {
    this.removeDisplay()
    this.setData({
      hidden: true,
    })
    
  },
  /**
   * Close pipeline query
   */
  closePipe: function () {
    this.setData({
      hidden: false
    })
  },
  /**
   * 获取点击的管道类型
   */
  checkboxChange: function (e: any) {
    let search: Array<string> = e.detail.value
    this.setData({
      searchPipes: search
    })
  },
  /**
   * 全部展示
   */
  allShow: function () {
    let marker: Array<Maker> = getMakers(app.globalData.allWells)
    // 展示管道 
    this.setData({
      // 页面展示的管道
      pipes: app.globalData.allPipes,
      markers: marker,
    } as object)
    //画管道
    this.setPipes()
    this.drawPoly(app.globalData.allPipes)
    this.closePipe()
  },
  /**
   * 部分展示
   */
  partShow: function () {
    let newPipe: Array<Pipe> = partShow(this.data.searchPipes, app.globalData.allPipes)
    let marker: Array<Maker> = getMakers(app.globalData.allWells)
    let newMarker = filterMarker(marker, newPipe)
    this.setData({
      pipes: newPipe,
      markers: newMarker
    })
    this.drawPoly(this.data.pipes)
    this.closePipe()
  },
  /**
   *  查找管道类型
   */
  setPipes: function () {
    let pipe: Array<Pipe> = this.data.pipes
    let wellTypeArr: Array<string> = setPipe(pipe);
    let polyline1: Array<Polyline> = drawPolyline(pipe, app.globalData.allWells)
    this.setData({
      pipeType: wellTypeArr,
      polylines: polyline1
    })
  },
  /**
   * 点击管道事件
   * @param val 
   */
  getinformation: function (val: any) {
    // head里面原方法，使用的marker自带点击事件
    //     if(!this.data.markerClickEvent){
    //       this.setData({
    //         markerClickEvent:true
    //       })
    //     }else{      
    //       let clickPoint: Coordinate = val.detail
    //       let pol: Array<Polyline> = this.data.polylines
    //       for (let index: number = 0; index < pol.length; index++) {
    //         if (pol[index].color === "#DC143C") {
    //           pol.splice(index, 1)
    //         }
    //       }
    //       this.setData({
    //         // 还原旧管道
    //         polylines: pol,
    //         // 页面上移
    //         up: true,
    //       })
    //       let showLine: ShowPipe | null
    //       if (this.data.searchPipes.length === 0) showLine = addLine(clickPoint, this.data.tree, null, this.data.pipes, this.data.wells)
    //       else showLine = addLine(clickPoint, this.data.tree, this.data.searchPipes, this.data.pipes, this.data.wells)

    //       if(showLine){    
    //         if (showLine?.pipe !== undefined) {
    //           this.setData({
    //             up: false,
    //             textData: {
    //               pipeId:showLine.pipe.id,
    //               diameter: showLine?.pipe.diameter,
    //               material: showLine?.wellType?.join(" ，"),
    //               types: showLine?.pipe.type,
    //               deep: showLine?.pipe.deep
    //             }
    //           })
    //         }
    //         let newPip: Array<Polyline> = this.data.polylines
    //         let polyline: Polyline = showLine?.polyline!
    //         if (polyline !== undefined) newPip.push(polyline)
    //         this.setData({
    //           polylines: newPip
    //         })
    //     }
    //     else{
    //       this.setData({
    //         up:true
    //       })
    //     }
    //     }

    //   },
    //   getOffsetPoint: function (e: any): void {
    //     let coordinate: Coordinate = e.detail
    //     let markers: Array<Maker> = this.data.markers
    //     let polylines: Array<Polyline> = this.data.polylines
    //     let offsetMakers: Array<Maker> = addMakerLoaction(markers, Number(coordinate.latitude), Number(coordinate.longitude))
    //     let offsetPloylines: Array<Polyline> = addPolylineLoaction(polylines, Number(coordinate.latitude), Number(coordinate.longitude))
    //     this.setData({
    //       markers: offsetMakers,
    //       polylines: offsetPloylines
    //     })
    //   },
    //    handleMarkertTap: function (e: any) {
    //      if(this.data.markerClickEvent){
    //       let pol = this.data.polylines
    //       for (let index = 0; index < pol.length; index++) {
    //         if (pol[index].color === "#DC143C") {
    //           pol.splice(index, 1)
    //         }
    //       }
    //       if (app.globalData.allWells.length < this.data.markers.length) {
    //         let marker: Array<Maker> = this.data.markers
    //         marker.pop()
    //         this.setData({
    //           markers: marker
    //         })
    //       }
    //         let well: Well = this.data.wells[e.markerId - 1]
    //         let marker: Array<Maker> = this.data.markers
    // marker.push({
    //   latitude: well.latitude,
    //   longitude: well.longitude,
    //   id: marker.length,
    //   iconPath: "https://kuangshen.xyz:1027/pipe-static/red.png",
    //   width: 20,
    //   height: 20
    // })
    // this.setData({
    //   up: false,
    //   markers:marker,
    //   textData: {
    //     id: well.id,
    //     model: well.model,
    //     type: well.type,
    //   },
    //   markerClickEvent: false,
    //   polylines: pol,
    // })
    // }
    // =======
    let clickPoint: Coordinate = val.detail
    this.removeDisplay()
    let searchPipes: Array<string> | null = this.data.searchPipes.length == 0 ? null : this.data.searchPipes;
    
    let showLine: ShowPipe | null = addLine(clickPoint, this.data.tree, searchPipes,app.globalData.allPipes, app.globalData.allWells)

    if (showLine?.type === "line") {
      if (showLine?.pipe !== undefined) {
        this.setData({
          up: false,
          textData: {
            diameter: showLine?.pipe.diameter,
            material: showLine?.wellType?.join(" ，"),
            types: showLine?.pipe.type,
            deep: showLine?.pipe.deep
          }
        })
      }
      let newPip: Array<Polyline> = this.data.polylines
      let polyline: Polyline = showLine?.polyline!
      if (polyline !== undefined) newPip.push(polyline)
      this.setData({
        polylines: newPip
      })
    } else if (showLine?.type === "well") {
      if (showLine.wellId === undefined) {
        throw new Error("wellId为空！！！！")
      } else {
        let well: Well =app.globalData.allWells[showLine?.wellId - 1]
        let marker: Array<Maker> = this.data.markers
        marker.push({
          latitude: well.latitude,
          longitude: well.longitude,
          id: marker.length + 1,
          iconPath: "https://kuangshen.xyz:1027/pipe-static/red.png",
          width: 20,
          height: 20
        })
        this.setData({
          up: false,
          markers: marker,
          textData: {
            id: well.id,
            model: app.globalData.allWells[well.id - 1].model,
            type: app.globalData.allWells[well.id - 1].type,
          }
        })
      }
    }

  },
  //移除点击管道和井口
  removeDisplay:function(){
    let pol: Array<Polyline> = this.data.polylines
    // 隐藏弹窗
    this.setData({
      up: true
    })
    // 管道是否改变默认为false当发现红色删除红线并改变布尔值变为true
    let is_pol_change: boolean = false
    for (let index: number = pol.length - 1; index >= 0; index--) {
      if (pol[index].color !== "#DC143C") {
        break;
      }
      is_pol_change = true
      pol.splice(index, 1)
    }
    // 如果为true重新渲染
    if (is_pol_change) {
      this.setData({
        polylines: pol
      })
    }
    // marker是否改变通过
    let is_mak_change: Array<Maker> | null = isMarkerChange(this.data.markers)
    if (is_mak_change !== null) {
      this.setData({
        // 还原旧管道
        markers: is_mak_change,
      })
    }
  },
  // handleMarkertTap: function (e: any) {
  //   let pol = this.data.polylines
  //   for (let index = 0; index < pol.length; index++) {
  //     if (pol[index].color === "#DC143C") {
  //       pol.splice(index, 1)
  //     }
  //   }
  //   let marker = getMakers(this.data.wells)
  //   this.setData({
  //     markers: marker,
  //   })
  //   let arrMarker: Array<Maker> = this.data.markers
  //   arrMarker[e.markerId - 1].iconPath = "https://kuangshen.xyz:1027/pipe-static/red.png"
  //   arrMarker[e.markerId - 1].width = 30
  //   arrMarker[e.markerId - 1].height = 30
  //   this.setData({
  //     markers: arrMarker,
  //     textData: {
  //       id: e.markerId,
  //       model: this.data.wells[e.markerId].model,
  //       type: this.data.wells[e.markerId].type,
  //     },
  //     markerClickEvent: false,
  //     up: true,
  //     polylines: pol,
  //   })

  // },
  // >>>>>>> develop
  // 微信自带 事件处理函数
  bindViewTap() {
    wx.navigateTo({
      url: '../logs/logs',
    })
  },
  /**
   * WeChat built-in function
   */
  getUserProfile() {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
  },
  /**
   * WeChat built-in function
   * @param e 
   */
  getUserInfo(e: any) {
    // 不推荐使用getUserInfo获取用户信息，预计自2021年4月13日起，getUserInfo将不再弹出弹窗，并直接返回匿名的用户个人信息
    console.log(e)
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
})