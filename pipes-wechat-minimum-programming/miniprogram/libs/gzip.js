var pako = require('./pako.min');
import{ weAtob} from "./weapp-jwt"
 // 解压
//js解压gzip
export function unzip(obj) {
  let key =  weAtob(obj)
  // 解压
      // 将二进制字符串转换为字符数组
      var charData = key.split('').map(function (x) {
          return x.charCodeAt(0);
      });
      // 将数字数组转换成字节数组
      var binData = new Uint8Array(charData);
      // 解压
      var data = pako.inflate(binData);
      // key = String.fromCharCode.apply(null, new Uint16Array(data));
      var res = '';
      var chunk = 16 * 1024;
      var i;
      for (i = 0; i < data.length / chunk; i++) {
          res += String.fromCharCode.apply(null, data.slice(i * chunk, (i + 1) * chunk));
      }
      res += String.fromCharCode.apply(null, data.slice(i * chunk));
      return decodeURIComponent(escape(res));
  }
