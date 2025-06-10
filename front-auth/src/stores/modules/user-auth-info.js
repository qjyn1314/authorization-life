// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
// 根据当前登录用户的Token获取登录用户的信息
export const userInfoStore = defineStore("userInfoStore", {
    // 开启数据持久化
    persist: {
        enabled: true, // true 表示开启持久化保存
        strategies: [
            {
                key: 'latest-data',
                storage: localStorage,
                // 每次状态变化时自动保存
            }
        ]
    },
    // 存储数据state
    state: () => {
        return {
            accessTokenId: null,
            anonymousFlag: null,
            empEnabledFlag: null,
            language: null,
            realName: null,
            token: null,
            userActivedFlag: null,
            userEmail: null,
            userEnabledFlag: null,
            userGender: null,
            userGroups: null,
            userId: null,
            userLockedFlag: null,
            userPhone: null,
            username: null
        }
    },
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {
        setUserInfo(userInfo) {
            console.log("setUserInfo", userInfo);
            this.accessTokenId = userInfo.accessTokenId
            this.anonymousFlag = userInfo.anonymousFlag
            this.empEnabledFlag = userInfo.empEnabledFlag
            this.language = userInfo.language
            this.realName = userInfo.realName
            this.token = userInfo.token
            this.userActivedFlag = userInfo.userActivedFlag
            this.userEmail = userInfo.userEmail
            this.userEnabledFlag = userInfo.userEnabledFlag
            this.userGender = userInfo.userGender
            this.userGroups = userInfo.userGroups
            this.userId = userInfo.userId
            this.userLockedFlag = userInfo.userLockedFlag
            this.userPhone = userInfo.userPhone
            this.username = userInfo.username
        },
    },
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getUserInfo: state => state
    }
});