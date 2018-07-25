package com.plantmer.soilsensor.dao;

public class UserDTO {
    private String login;
    private String email;
    private String name;
    private String orgRole;
    private String theme;
    private String dashid;
    private String parent;
    private String pass;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getOrgRole() {
        return orgRole;
    }

    public void setOrgRole(String orgRole) {
        this.orgRole = orgRole;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDashid() {
        return dashid;
    }

    public void setDashid(String dashid) {
        this.dashid = dashid;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", orgRole='" + orgRole + '\'' +
                ", theme='" + theme + '\'' +
                ", dashid='" + dashid + '\'' +
                ", parent='" + parent + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
