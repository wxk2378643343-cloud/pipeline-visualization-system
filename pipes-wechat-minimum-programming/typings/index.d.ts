/// <reference path="./types/index.d.ts" />

import { Pipe, Polyline, Well } from "../miniprogram/entity/pipe";

interface IAppOption {
  globalData: {
    userInfo?: WechatMiniprogram.UserInfo,
    amapKey: string,
    wellrRadius: number,
    pipeWidth: number,
    wellColorMap: { [key: string]: string },
    pipeColorMap: { [key: string]: string },
    clickPipeRange: number,
    clickWellRange: number,
    pipeClickedWidth: number,
    allPipes: Array<Pipe>,
    allWells: Array<Well>,
    OffsetPointBoolen:boolean,
    serverHost: string,
    serverPort: number
  },

  userInfoReadyCallback?: WechatMiniprogram.GetUserInfoSuccessCallback,
}