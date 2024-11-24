package com.authorization.life.auth.infra.entity;

import com.authorization.mybatis.start.entity.AuditEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 菜单资源表
 *
 * @author code@code.com
 * @date 2024-11-24 16:29:24
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("lsys_menu")
public class LsysMenu extends AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_MENU_ID = "menu_id";
    public static final String FIELD_PARENT_ID = "parent_id";
    public static final String FIELD_MENU_TYPE = "menu_type";
    public static final String FIELD_LEVEL = "level";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_MENU_NAME = "menu_name";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PATH = "path";
    public static final String FIELD_COMPONENT = "component";
    public static final String FIELD_DESCRIBE = "describe";
    public static final String FIELD_IS_HIDE = "is_hide";
    public static final String FIELD_IS_LINK = "is_link";
    public static final String FIELD_IS_KEEP_ALIVE = "is_keep_alive";
    public static final String FIELD_IS_SPREAD = "is_spread";
    public static final String FIELD_IS_FULL = "is_full";
    public static final String FIELD_IS_AFFIX = "is_affix";
    public static final String FIELD_AUTH = "auth";
    public static final String FIELD_ENABLED_FLAG = "enabled_flag";
    public static final String FIELD_SORTED = "sorted";
    public static final String FIELD_VERSION_NUM = "version_num";

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String menuId;
    /**
     * 父级ID
     */
    private String parentId;
    /**
     * 资源类型:菜单(MENU),目录(CATALOGUE),按钮(BUTTON)
     */
    private String menuType;
    /**
     * 等级:0-顶级;1-(依次排列)
     */
    private String level;
    /**
     * 图标
     */
    private String icon;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 路由名称
     */
    private String name;
    /**
     * 路由路径
     */
    private String path;
    /**
     * 组件页面路径
     */
    private String component;
    /**
     * 资源描述
     */
    private String describe;
    /**
     * 是否隐藏
     */
    private String isHide;
    /**
     * 链接地址
     */
    private String isLink;
    /**
     * 是否缓存
     */
    private String isKeepAlive;
    /**
     * 是否折叠
     */
    private String isSpread;
    /**
     * 是否全屏
     */
    private String isFull;
    /**
     * 是否固定
     */
    private String isAffix;
    /**
     * 权限
     */
    private String auth;
    /**
     * 资源是否启用
     */
    private Boolean enabledFlag;
    /**
     * 排序
     */
    private Integer sorted;
    /**
     * 版本号
     */
    private Long versionNum;

}
