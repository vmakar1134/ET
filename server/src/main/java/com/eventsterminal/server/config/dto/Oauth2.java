package com.eventsterminal.server.config.dto;

import java.util.ArrayList;
import java.util.List;

public class Oauth2 {

    private List<String> pathParam = new ArrayList<>();

    private String path;

    public List<String> getPathParam() {
        return pathParam;
    }

    public void setPathParam(List<String> pathParam) {
        this.pathParam = pathParam;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Oauth2{" +
                "pathParam=" + pathParam +
                ", path='" + path + '\'' +
                '}';
    }

}

