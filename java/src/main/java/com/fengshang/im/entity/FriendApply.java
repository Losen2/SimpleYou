package com.fengshang.im.entity;


import java.io.Serializable;
import java.util.Date;

public class FriendApply implements Serializable {
    private Integer id;
    private Integer applyUserId;
    private Integer appliedUserId;

    private User applyUser;//没法自动get要手动get

    private User appliedUser;
    private Integer acceptOrRefuse;//-1默认 1接收 0拒绝
    private Date createTime;
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "FriendApply{" +
                "id=" + id +
                ", applyUserId=" + applyUserId +
                ", appledUserId=" + appliedUserId +
                ", acceptOrRefuse=" + acceptOrRefuse +
                ", createTime=" + createTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Integer applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Integer getAppliedUserId() {
        return appliedUserId;
    }

    public void setAppliedUserId(Integer appliedUserId) {
        this.appliedUserId = appliedUserId;
    }

    public Integer getAcceptOrRefuse() {
        return acceptOrRefuse;
    }

    public void setAcceptOrRefuse(Integer acceptOrRefuse) {
        this.acceptOrRefuse = acceptOrRefuse;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(User applyUser) {
        this.applyUser = applyUser;
    }

    public User getAppliedUser() {
        return appliedUser;
    }

    public void setAppliedUser(User appliedUser) {
        this.appliedUser = appliedUser;
    }
}
