package com.baotruongtuan.rdpserver.model;

import org.java_websocket.client.WebSocketClient;

public class Role {
    private WebSocketClient client;
    private int id;
    private String roleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
