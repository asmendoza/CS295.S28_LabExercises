package edu.admu.cs295s28.LabActivity;

import io.realm.RealmObject;

public class RememberMe extends RealmObject {
    private String savedUser;
    private String savedPassword;
    private boolean rememberMe;

    public String getSavedUser() {
        return savedUser;
    }

    public void setSavedUser(String savedUser) {
        this.savedUser = savedUser;
    }

    public String getSavedPassword() {
        return savedPassword;
    }

    public void setSavedPassword(String savedPassword) {
        this.savedPassword = savedPassword;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "RememberMe{" +
                "savedUser='" + savedUser + '\'' +
                ", savedPassword='" + savedPassword + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
