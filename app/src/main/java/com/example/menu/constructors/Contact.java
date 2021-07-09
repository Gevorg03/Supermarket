package com.example.menu.constructors;

public class Contact {
    int id;
    String name;
    String phone_number;
    String password;
    String gender;

    public Contact(){

    }

    public Contact(String name,String password){
        this.name = name;
        this.password = password;
    }

    public Contact(String name){
        this.name = name;
    }

    public Contact(int id, String name,String password){
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Contact(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
