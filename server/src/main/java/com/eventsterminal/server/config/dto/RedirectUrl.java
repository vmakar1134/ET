package com.eventsterminal.server.config.dto;

public class RedirectUrl {

    private String path;

    private String host;

    public RedirectUrl() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "RedirectUrl{" +
                "url='" + path + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

}
