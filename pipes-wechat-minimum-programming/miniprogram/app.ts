import {IAppOption} from "../typings"
import {Pipe, Well} from "./entity/pipe"

// app.ts
App<IAppOption>({
    globalData: {
        amapKey: '52681ab1d3a0d33cfb102ba630637080',
        wellrRadius: 1,
        pipeWidth: 1,
        wellColorMap: {
            // "A220PE":"https://kuangshen.xyz:1027/pipe-static/blue.png",
            // "DN1200":"https://kuangshen.xyz:1027/pipe-static/orange.png",
            // "A160PE":"https://kuangshen.xyz:1027/pipe-static/green.png",
            // "A110PE":"https://kuangshen.xyz:1027/pipe-static/purple.png",
            // "DN400钢":"https://kuangshen.xyz:1027/pipe-static/shallowPink.png",
            // "A200PE":"https://kuangshen.xyz:1027/pipe-static/brilliantGreen.png",
            // "100光纤1/1 2根":"https://kuangshen.xyz:1027/pipe-static/darkBlue.png",
            // "50光纤 1/1 7根":"https://kuangshen.xyz:1027/pipe-static/brown.png",
            // "50光纤 1/1 1根":"https://kuangshen.xyz:1027/pipe-static/yellow.png"
            "DN": "https://kuangshen.xyz:1027/pipe-static/blue.png",
            "PE": "https://kuangshen.xyz:1027/pipe-static/orange.png",
            "钢": "https://kuangshen.xyz:1027/pipe-static/green.png",
            "铝": "https://kuangshen.xyz:1027/pipe-static/purple.png",
            "default": "https://kuangshen.xyz:1027/pipe-static/yellow.png"
        },
        pipeColorMap: {
            "雨污水": "#FFD700",
            "通信": "#00FFFF",
            "交通信号": "#7CFC00",
            "供电": "#4B0082",
            "给水": "#DEB887",
            "热力": '#00BFFF',
            "天燃气": "#808080",
            "default": "#F7B23F"
        },
        clickPipeRange: 0.00002,
        clickWellRange: 0.00002,
        pipeClickedWidth: 1,
        allPipes: new Array<Pipe>(),
        allWells: new Array<Well>(),
        OffsetPointBoolen: false,
        serverHost: "<YOUR_SERVER_HOST>",
        serverPort: "<YOUR_SERVER_PORT>"
    },
    onLaunch() {
        // 展示本地存储能力
        const logs = wx.getStorageSync('logs') || []
        logs.unshift(Date.now())
        wx.setStorageSync('logs', logs)
        // 登录
        wx.login({
            success: res => {
                console.log(res)
                // 发送 res.code 到后台换取 openId, sessionKey, unionId
            },
        })
    },
})
