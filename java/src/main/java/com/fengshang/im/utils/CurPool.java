package com.fengshang.im.utils;

import com.fengshang.im.entity.User;
import com.fengshang.im.controller.WebSocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一管理session、websocket
 */
public class CurPool {

//    public static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    public static Map<Integer, WebSocket> webSockets = new ConcurrentHashMap<>();

}
