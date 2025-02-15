package com.ruqimobility.precisiontest.entity.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiTestCaseResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test ID
     */
    private String id;

    /**
     * Project ID this test belongs to
     */
    private String projectId;

    /**
     * Test case name
     */
    private String name;

    /**
     * priority
     */
    private String priority;

    /**
     * api definition id
     */
    private String apiDefinitionId;

    /**
     * Test description
     */
    private String description;

    /**
     * request (JSON format)
     */
    private String request;

    /**
     * User ID
     */
    private String createUserId;

    /**
     * User ID
     */
    private String updateUserId;

    /**
     * Create timestamp
     */
    private Long createTime;

    /**
     * Update timestamp
     */
    private Long updateTime;

    /**
     * api test case ID
     */
    private Integer num;

    private String tags;

    /**
     * Last ApiDefinitionExecResult ID
     */
    private String lastResultId;

    private String status;

    private String originalStatus;

    /**
     * Delete timestamp
     */
    private Long deleteTime;

    /**
     * Delete user id
     */
    private String deleteUserId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 自定义排序，间隔5000
     */
    @TableField("`order`")
    private Long order;

    /**
     * 用例状态等同场景的status
     */
    private String caseStatus;

    private String versionId;

    /**
     * 是否需要同步
     */
    private Boolean toBeUpdated;

    /**
     * 需要同步的开始时间
     */
    @TableField("to_be_update_Time")
    private Long toBeUpdateTime;

    /**
     * 创建用例所属周
     */
    private String week;


}
