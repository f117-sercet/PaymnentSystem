package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_config")
public class SysConfig implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 配置KEY
     */
    private String configKey;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 描述信息
     */
    private String configDesc;

    /**
     * 分组key
     */
    private String groupKey;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 配置内容项
     */
    private String configVal;

    /**
     * 类型: text-输入框, textarea-多行文本, uploadImg-上传图片, switch-开关
     */
    private String type;

    /**
     * 显示顺序
     */
    private Long sortNum;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
