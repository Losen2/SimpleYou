package com.fengshang.im.entity;

import org.springframework.data.annotation.Id;

public class UserSession {
    @Id
    private String id;
    private Integer userId1;
    private Integer userId2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId1() {
        return userId1;
    }

    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }

    public Integer getUserId2() {
        return userId2;
    }

    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id='" + id + '\'' +
                ", userId1=" + userId1 +
                ", userId2=" + userId2 +
                '}';
    }
}
