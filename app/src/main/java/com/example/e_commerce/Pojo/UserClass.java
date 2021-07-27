package com.example.e_commerce.Pojo;

import java.util.ArrayList;

public class UserClass {
    private String name;
    private String email;
    private String Password;
    private String bithDate;

    public UserClass() {
    }

    public UserClass(String name, String email, String password, String bithDate) {
        this.name = name;
        this.email = email;
        Password = password;
        this.bithDate = bithDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
