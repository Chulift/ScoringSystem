package com.example.chulift.demoapplication.Login;


public class User {
    private String email,password,name,surname;
    private Boolean dataIsset = false;
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
    public Boolean getDataIsset() {return dataIsset;}
    public void setDataIsset(Boolean dataIsset) {
        this.dataIsset = dataIsset;
    }
}
