package com.express.model;

import java.util.Date;

/**
 * 抽象实体基类
 */
public abstract class BaseEntity {
    private Date createTime;    // 创建/入库时间
    private Date updateTime;    // 更新/出库时间

    // 无参构造
    public BaseEntity() {
    }

    // 全参构造
    public BaseEntity(Date createTime, Date updateTime) {
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Getter & Setter
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    // 抽象展示方法，子类实现
    public abstract void displayInfo();
}
