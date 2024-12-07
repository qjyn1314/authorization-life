// 用来配置项目的Logo、标题等设置
export default {
  /**
   * 网页标题
   */
  logoTitle: import.meta.env.VITE_WEB_TITLE,

  /**
   * 网页英文标题
   */
  logoEnTitle: import.meta.env.VITE_WEB_EN_TITLE,

  /**
   * 网页标题大小
   */
  loginTitleSize: 18,

  /**
   * 登录页面标题
   */
  loginTitle: import.meta.env.VITE_LOGIN_TITLE,

  /**
   * 登录页面英文标题
   */
  loginEnTitle: import.meta.env.VITE_LOGIN_EN_TITLE,

  /**
   * Logo大小
   */
  logoSize: "34px",

  /**
   * Logo地址
   */
  logoUrl: "images/logo/logo.webp",

  /**
   * 是否显示Logo和网页标题[true-显示，false-隐藏]
   */
  logoShow: true,

  /**
   * 管理平台标题动画
   */
  logoTitleAnimate: "animate__animated animate__fadeInLeft",

  /**
   * 左侧菜单动画
   */
  menuAnimate: "animate__animated animate__fadeInLeft",

  /**
   * 左侧折叠宽度
   */
  asideMenuCollapseWidth: "56px",

  /**
   * 左侧悬浮折叠宽度
   */
  asideMenuHoverCollapse: "60px",

  /**
   * 左侧折叠宽度[双栏布局]
   */
  columnMenuCollapseWidth: "56px",

  /**
   * 左侧悬浮折叠宽度[双栏布局]
   */
  columnMenuHoverCollapse: "56px"
};
