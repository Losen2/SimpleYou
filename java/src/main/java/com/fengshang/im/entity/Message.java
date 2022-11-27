package com.fengshang.im.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * 消息实体
 *
 * @author 小道仙
 */
//@Document(collection="lalala")
public class Message implements Serializable {
    //消息id
    @Id
    private String id;

    //消息发送者id
    private Integer userId;

    //消息接收者id
    private Integer toUserId;
    private Integer toGroupId;

    private String content;
    private String imgUrl;




    private String videoUrl;
    private String fileUrl;
    //已读标记 0表示0人已读 1表示1以此类推（考虑到群聊所以有2345）
    private Integer readCount;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date frontCreateTime;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Indexed
    private long behindTime;


    private Integer tempId;

    private Integer temp;

    private Integer update;


    //getter and setter



    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(Integer toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Date getFrontCreateTime() {
        return frontCreateTime;
    }

    public void setFrontCreateTime(Date frontCreateTime) {
        this.frontCreateTime = frontCreateTime;
    }

    public long getBehindTime() {
        return behindTime;
    }

    public void setBehindTime(long behindTime) {
        this.behindTime = behindTime;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getUpdate() {
        return update;
    }

    public void setUpdate(Integer update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userId=" + userId +
                ", toUserId=" + toUserId +
                ", toGroupId=" + toGroupId +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", readCount=" + readCount +
                ", frontCreateTime=" + frontCreateTime +
                ", behindTime=" + behindTime +
                ", tempId=" + tempId +
                ", temp=" + temp +
                ", update=" + update +
                '}';
    }
    public Message(){}
    public Message(Message msg) {

        this.userId = msg.getUserId();
        this.toUserId = msg.getToUserId();
        this.toGroupId = msg.getToGroupId();
        this.content = msg.getContent();
        this.imgUrl = msg.getImgUrl();
        this.videoUrl = msg.getVideoUrl();
        this.fileUrl = msg.getFileUrl();
        this.readCount = msg.getReadCount();
        this.frontCreateTime = msg.getFrontCreateTime();
        this.behindTime = msg.getBehindTime();
        this.tempId = msg.getTempId();
        this.temp = msg.getTemp();
        this.update = msg.getUpdate();
    }
}