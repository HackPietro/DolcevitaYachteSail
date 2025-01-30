package com.progetto.ingsw.DataBase;

import com.progetto.ingsw.Model.User;

public class Authentication {
    private static Authentication instance = null;

    private User user = null;

    private Authentication() {}

    public static Authentication getInstance(){
        if (instance == null){
            instance = new Authentication();
        }
        return instance;
    }

    public boolean isAdmin(){
        System.out.println("isAdmin " + this.user.isAdmin());
        return this.user.isAdmin();
    }

    public void login(User user){
        this.user = user;
    }

    public boolean settedUser(){
        System.out.println("settedUser " + !(this.user == null));
        return !(this.user == null);
    }

    public void logout(){
        this.user = null;
    }

    public User getUser() {
        if (user != null){
            return user;
        }else{
            return null;
        }
    }
}

