// 定义选项卡tabs小仓库[选择式Api写法]
import { defineStore } from "pinia";
import { PINIA_PREFIX, HOME_URL } from "@/config/index.ts";
import router from "@/routers/index.ts";
import { getUrlWithParams } from "@/utils/index.ts";
import { koiMsgWarning } from "@/utils/koi.ts";
// 导入keepAliveStore仓库，必须使用下方这种导入方法，不然会报错。但是使用下方方法有一个问题就是使用keepAliveStore.addKeepAliveName(tab.name);等方法不能进行持久化缓存。
// 想进行缓存，只能将const keepAliveStore = useKeepAliveStore(); 放到方法里面，不能放置全局变量。
import useKeepAliveStore from "@/stores/modules/keepAlive.ts";

const tabsStore = defineStore("tabs", {
  // 开启数据持久化
  persist: {
    // enabled: true, // true 表示开启持久化保存
    key: PINIA_PREFIX + "tabs", // 默认会以 store 的 id 作为 key
    storage: localStorage
  },
  // 存储数据state
  state: () => {
    return {
      tabList: [] as any[] // 选项卡
    };
  },
  actions: {
    // 添加选项卡数据
    async addTab(tab: any) {
      const keepAliveStore = useKeepAliveStore();
      // 判断是否已经添加过此条数据，只要数组中有一个元素满足条件，就返回 true。
      const isTab = this.tabList.some((item: any) => {
        return item.path === tab.path;
      });
      if (isTab) {
        return;
      } else {
        if (!keepAliveStore.keepAliveName.includes(tab.name) && tab.isKeepAlive == "0") {
          if (tab.name) {
            keepAliveStore.addKeepAliveName(tab.name);
          }
        }
        this.tabList.push(tab);
      }
    },
    // 删除选项卡数据，tabPath: 右键选中的path，selectedPath：当前选项卡被选中的path
    async removeTab(tabPath: string, isCurrent: boolean = true, selectedPath?: string) {
      // 如果关闭的是首页，直接返回，不进行操作
      if (tabPath == HOME_URL) {
        koiMsgWarning("首页禁止关闭🌻");
        return;
      }
      const keepAliveStore = useKeepAliveStore();
      // 删除选项卡路由缓存
      const tabItem = this.tabList.find(item => item.path === tabPath);
      tabItem?.isKeepAlive && keepAliveStore.removeKeepAliveName(tabItem.name);
      const oldTabList = this.tabList;
      // 将这个需要删除的选项卡过滤掉，重新赋值给选项卡数组。
      this.tabList = this.tabList.filter(item => item.path !== tabPath);
      if (isCurrent) {
        // 如果关闭的不是选项卡被选中的，则依旧选择被选中的选项卡。
        const matchingPathObject = this.tabList.find((item: any) => item.path == selectedPath);
        if (matchingPathObject) {
          router.push(matchingPathObject?.path || HOME_URL);
          return;
        }
        // 如果关闭的是选项卡被选中的，则选择上一个或下一个。
        oldTabList.forEach((item, index) => {
          if (item.path !== tabPath) return;
          // 找到下一个选项卡或上一个选项卡。通过计算索引值可以得到下一个选项卡的位置，即 this.tabList[index + 1]；如果不存在下一个选项卡，则返回上一个选项卡的位置，即 this.tabList[index - 1]。
          const nextTab = oldTabList[index + 1] || oldTabList[index - 1];
          if (!nextTab) return;
          // 如果找到了下一个或上一个选项卡，则使用路由导航方法[假设是 router.push]将页面跳转到该选项卡对应的路径。
          router.push(nextTab.path);
        });
      }
    },
    // 用来清空Tabs缓存
    async setTab(tabList: any[]) {
      this.tabList = tabList;
    },
    // 设置选项卡标题
    async setTabTitle(title: string) {
      // 根据当前标签页的path进行替换，tabList持久化数据里面的标签名称
      this.tabList.forEach(item => {
        // console.log("getUrlWithParams()", getUrlWithParams());
        if (item.path == getUrlWithParams()) item.title = title;
      });
    },
    // 关闭左边 OR 右边选项卡
    async closeSideTabs(path: string, type: "left" | "right") {
      const keepAliveStore = useKeepAliveStore();
      const currentIndex = this.tabList.findIndex(item => item.path === path);
      if (currentIndex !== -1) {
        const range = type === "left" ? [0, currentIndex] : [currentIndex + 1, this.tabList.length];
        this.tabList = this.tabList.filter((item, index) => {
          return index < range[0] || index >= range[1] || !item.closeIcon;
        });

        const closeTab = this.tabList.filter((item: any) => {
          return !item.closeIcon;
        });

        if (type === "left") { 
          const nextTab = this.tabList[closeTab.length];
          router.push(nextTab?.path);
        }
  
        if (type === "right") { 
          const nextTab = this.tabList[currentIndex] || this.tabList[currentIndex + 1] || this.tabList[currentIndex - 1];
          router.push(nextTab?.path);
        }
      }
      // 重新设置路由缓存，将新的tabList的name覆盖keepAliveList
      const keepAliveList = this.tabList.filter(item => item.isKeepAlive);
      keepAliveStore.setKeepAliveName(keepAliveList.map(item => item.name));
    },
    // 关闭多个选项卡，若tabValue传递有值并且选项卡数组中存在，则关闭除自己和固定选项卡之外的所有选项卡[关闭其他选项卡]，若tabValue不传值，则关闭除固定选项卡之外的所有选项卡[关闭所有选项卡]。
    async closeManyTabs(tabValue?: string) {
      const keepAliveStore = useKeepAliveStore();
      this.tabList = this.tabList.filter(item => {
        return item.path === tabValue || !item.closeIcon;
      });
      // 重新设置路由缓存，将新的tabList的name覆盖keepAliveList
      const keepAliveList = this.tabList.filter(item => item.isKeepAlive);
      keepAliveStore.setKeepAliveName(keepAliveList.map(item => item.name));
    },
    // 选项卡是否固钉
    async replaceIsAffix(tabPath?: string, closeIcon?: boolean) {
      this.tabList.forEach(item => {
        if (item.path == tabPath) {
          item.closeIcon = closeIcon;
        }
      });
    }
  },
  // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
  getters: {
    // 获取选项卡state数据变量
    getTabs(state) {
      return state.tabList;
    }
  }
});

// 对外暴露方法
export default tabsStore;

// 导入其他pinia仓库时使用
// export function useKeepAliveStoreWithOut() {
//   return useKeepAliveStore(store);
// }
