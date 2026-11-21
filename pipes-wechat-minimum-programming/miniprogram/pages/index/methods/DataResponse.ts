import { IAppOption } from "../../../../typings";
import { requistData } from "../../../entity/amap";
import { PipeServiceResponse } from "../../../entity/pipe";
import { Node } from "../../../entity/tree";
import { sleep } from "../../../utils/util";

const app: IAppOption = getApp<IAppOption>()
const requistUrl: string = `https://${app.globalData.serverHost}:${app.globalData.serverPort}`

export function requistPipes(url: string): Promise<requistData> {
  let response = new Promise((resolve,) => {
    wx.request({
      url: requistUrl + url,
      method: "GET",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (response) {
        if (response.statusCode === 500) {
          wx.showToast({
            title: "未知错误",
            icon: 'error',
            duration: 2000
          })
        } else if (response.statusCode === 200) {
          let message: PipeServiceResponse = response.data as PipeServiceResponse
          if (message.code === 200)
            resolve(message.data)
          if (message.code === 500) {
            wx.showToast({
              title: message.msg,
              icon: 'none',
              duration: 2000,
              mask: true
            })
          }
        }
      },
      fail: function () {
        wx.showToast({
          title: "网络错误",
          icon: 'error',
          duration: 2000
        })
      }
    })
  });
  return response as Promise<requistData>
}

export function uploadFild(url: string): Promise<requistData> {
  return new Promise((resolve) => {
    wx.chooseMessageFile({
      count: 10,
      type: 'file',
      extension: ['.xlsx', '.xls', '.XLSX', '.XLS', 'xlsx', 'xls', 'XLSX', 'XLS'],
      success(res) {
        wx.showLoading({
          title: '等待响应',
        });
        const tempFilePaths = res.tempFiles
        for (var i in tempFilePaths) {
          wx.uploadFile({
            url: requistUrl + url, //上传的服务器地址
            filePath: tempFilePaths[i].path,
            name: 'file',
            success: function (response) {
              let body = JSON.parse(response.data)
              wx.hideLoading();
              if (response.statusCode === 500) {
                wx.showToast({
                  title: "未知错误",
                  icon: 'error',
                  duration: 2000
                })
              }
              if (response.statusCode === 200) {
                let unzipBody: PipeServiceResponse = body;
                if (unzipBody.code === 200) {
                  resolve(unzipBody.data as requistData);
                  wx.showToast({
                    title: '上传成功',
                    icon: 'success',
                    duration: 1300
                  })
                }
                if (unzipBody.code === 500) {
                  wx.showToast({
                    title: unzipBody.msg,
                    icon: 'none',
                    duration: 2000,
                    mask: true
                  })
                }
              }
            },
            fail: function () {
              wx.showToast({
                title: "网络错误",
                icon: 'error',
                duration: 3000
              })
            }
          })
        }
      }
    })
  })
}


export function requistDatas(url: string): Promise<PipeServiceResponse> {
  let response: Promise<PipeServiceResponse> = new Promise((resolve) => {
    wx.request({
      url: requistUrl + url,
      method: "GET",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (data) {
        if (data.statusCode === 500) {
          wx.showToast({
            title: "未知错误",
            icon: 'error',
            duration: 2000
          })
        }
        if (data.statusCode === 200) {
          let message: PipeServiceResponse = data.data as PipeServiceResponse;
          if (message.code === 200) {
            let messageContent: PipeServiceResponse = message.data as PipeServiceResponse
            resolve(messageContent);
          }
          if (message.code === 500) {
            wx.showToast({
              title: "未知错误",
              icon: 'error',
              duration: 2000
            })
          }
        }
      },
      fail: function () {
        wx.showToast({
          title: "网络错误",
          icon: 'error',
          duration: 3000
        })
      }
    })
  });

  return response;
}

export async function rerequestTree(url: string): Promise<Node> {
  let count: number = 0;
  let result: PipeServiceResponse = await requistDatas(url);

  while (result.code === 114514 && count < 3) {
    await sleep(3000);
    result = await requistDatas(url);
    count++;
  }
  return result as unknown as Node;
}


