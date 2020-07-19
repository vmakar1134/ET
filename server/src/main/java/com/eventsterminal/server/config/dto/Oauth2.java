package com.eventsterminal.server.config.dto;

import java.util.List;

public class Oauth2 {

    private List<RedirectUrl> redirect;

    public Oauth2() {
    }

    public List<RedirectUrl> getRedirect() {
        return redirect;
    }

    public void setRedirect(List<RedirectUrl> redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return "Oauth2{" +
                "redirectUrlList=" + redirect +
                '}';
    }

}
