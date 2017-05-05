package com.example.chulift.demoapplication.login;


public class User {
    private String email,password,name,surname;
    private Boolean dataIsSet = false;
    public User(String email,String password,String name,String surname){
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
    public String getEmail() {
        return email;
    }
    public String getName(){ return  name;}
    public String getSurname(){return surname;}
    public String getPassword() {
        return password;
    }
    public Boolean getDataIsSet() {return dataIsSet;}
    void setDataIsSet(Boolean dataIsSet) {
        this.dataIsSet = dataIsSet;
    }
}
