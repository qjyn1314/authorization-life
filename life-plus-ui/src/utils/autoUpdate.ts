import { ElMessageBox } from "element-plus";

// 自动更新方式一：比较构建文件的hash值
// let lastScriptUrl: string[] | null = null;

// const scriptReg = /\<script.*src=["'](?<src>[^"']+)/gm;

// const extractNewScripts: () => Promise<string[]> = async () => {
//   try {
//     const response = await fetch(`/?_timestamp=${Date.now()}`);
//     const html = await response.text();
//     scriptReg.lastIndex = 0;
//     let result: string[] = [];
//     let match: RegExpExecArray | null;

//     while ((match = scriptReg.exec(html))) {
//       result.push(match.groups!.src);
//     }

//     return result;
//   } catch (error) {
//     console.error("Failed to fetch HTML:", error);
//     return [];
//   }
// };

// const needUpdate: () => Promise<boolean> = async () => {
//   const newScriptUrl = await extractNewScripts();
//   if (!lastScriptUrl) {
//     lastScriptUrl = newScriptUrl;
//     return false;
//   }

//   let result = false;
//   if (lastScriptUrl.length !== newScriptUrl.length) {
//     result = true;
//   } else {
//     for (let i = 0; i < lastScriptUrl.length; i++) {
//       if (lastScriptUrl[i] !== newScriptUrl[i]) {
//         result = true;
//         break;
//       }
//     }
//   }

//   lastScriptUrl = newScriptUrl;
//   return result;
// };

// const duration = 60 * 1000;

// export const autoRefresh: () => void = () => {
//   setTimeout(async () => {
//     try {
//       const willUpdate = await needUpdate();
//       if (willUpdate) {
//         ElMessageBox.confirm("检测到新版本，建议立即更新以确保平台正常使用?", "温馨提示", {
//           confirmButtonText: "确定更新",
//           cancelButtonText: "稍后更新",
//           type: "warning"
//         })
//           .then(() => {
//             location.reload();
//           })
//           .catch(() => {});
//       } else {
//         autoRefresh();
//       }
//     } catch (error) {
//       console.error("Failed to check for updates:", error);
//     }
//   }, duration);
// };

// 自动更新方式二：利用HTTP协议的缓存机制，比较Etag或last-modified前后是否一致 
let versionTag: any = null; // 版本标识
let timer: any = undefined;

/**
 * 获取首页的ETag 或 Last-Modified值，作为当前版本标识
 * @returns {Promise<string|null>} 返回ETag或Last-Modified值
 */
const getVersionTag = async () => {
  const response = await fetch("/", {
    cache: "no-cache"
  });
  return response.headers.get("etag") || response.headers.get("last-modified");
};

/**
 * 比较当前的ETag 或 Last-Modified值与最新获取的值
 */
const compareTag = async () => {
  const newVersionTag = await getVersionTag();

  if (versionTag === null) {
    // 初次运行时，存储当前的ETag或Last-Modified值
    versionTag = newVersionTag;
  } else if (versionTag !== newVersionTag) {
    //  如果ETag或Last-Modified发生变化，则认为有更新
    console.info("更新了", {
      oldVersionTag: versionTag,
      newVersionTag: newVersionTag
    });
    // 清除定时器
    clearInterval(timer);
    // 提示用户更新
    ElMessageBox.confirm("检测到新版本，建议立即更新以确保平台正常使用?", "温馨提示", {
      confirmButtonText: "确定更新",
      cancelButtonText: "稍后更新",
      type: "warning"
    })
      .then(() => {
        location.reload();
      })
      .catch(() => {});
  } else {
    //没有更新
    console.info("没更新", {
      oldVersionTag: versionTag,
      newVersionTag: newVersionTag
    });
  }
};

// 每60秒检查一次是否有新的ETag或Last-Modified值
export const autoRefresh = () => {
  timer = setInterval(compareTag, 60000);
};
