package com.eventsterminal.server.config.model;

public class Payload {

    private String userId;

    private String email;

    private String name;

    private String pictureUrl;

    private String locale;

    private String familyName;

    private String givenName;

    private boolean emailVerified;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public static Builder builder() {
        return new Payload().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder userId(String userId) {
            Payload.this.setUserId(userId);
            return this;
        }

        public Builder email(String email) {
            Payload.this.setEmail(email);
            return this;
        }

        public Builder name(String name) {
            Payload.this.setName(name);
            return this;
        }

        public Builder pictureUrl(String pictureUrl) {
            Payload.this.setPictureUrl(pictureUrl);
            return this;
        }

        public Builder locale(String locale) {
            Payload.this.setLocale(locale);
            return this;
        }

        public Builder familyName(String familyName) {
            Payload.this.setFamilyName(familyName);
            return this;
        }

        public Builder givenName(String givenName) {
            Payload.this.setGivenName(givenName);
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            Payload.this.setEmailVerified(emailVerified);
            return this;
        }

        public Payload build() {
            return Payload.this;
        }
    }

}
