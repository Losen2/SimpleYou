package com.fengshang.im.utils;


/**
 * 封装返回结果集
 *

 */
public class JsonWrapper<T> {
    /**
     * 返回状态码
     */

    private Integer code;

    /**
     * 返回的数据
     */

    private T data;

    /**
     * 总条数
     */

    private Long total;

    /**
     * 成功与否
     */

    private Boolean success;

    /**
     * 消息提示
     */

    private String msg;

    /**
     * 错误描述
     */

    private String errDesc;

    /**
     * 用户token
     */

    private String token;

    public JsonWrapper() {
    }

    /**
     * 操作失败
     * @param errDesc 错误信息
     *

     */
    public static JsonWrapper<?> failure(String errDesc) {
        JsonWrapper<Object> objectAjaxResult = new JsonWrapper<>();
        objectAjaxResult.setErrDesc(errDesc);
        objectAjaxResult.setSuccess(false);
        objectAjaxResult.setCode(-1);
        return objectAjaxResult;
    }

    /**
     * 操作成功
     * @param msg  返回消息
     * @param total 总条数
     * @param data 返回的数据
     *

     */
    public static <T> JsonWrapper<T> success(String msg, long total, T data){
        JsonWrapper<T> result = new JsonWrapper<>();
        result.setSuccess(true);
        result.setTotal(total);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 操作成功
     * @param total 总条数
     * @param data 返回的数据
     *

     */
    public static <T> JsonWrapper<T> success(T data, long total){
        JsonWrapper<T> result = new JsonWrapper<>();
        result.setSuccess(true);
        result.setTotal(total);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 操作成功
     * @param data 返回的数据
     *

     */
    public static <T> JsonWrapper<T> success(T data){
        JsonWrapper<T> result = new JsonWrapper<>();
        result.setSuccess(true);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 操作成功
     * @param msg  返回消息
     *

     */
    public static <T> JsonWrapper<T> success(String msg){
        return success(msg,0,null);
    }

    /**
     * 操作成功
     * @param msg  返回消息
     * @param total 总条数
     *

     */
    public static <T> JsonWrapper<T> success(String msg, Integer total){
        return success(msg,total,null);
    }

    /**
     * 操作成功
     *

     */
    public static <T> JsonWrapper<T> success(){
        return success("操作成功",0,null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}