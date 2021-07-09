package com.example.menu.constructors;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ListItem {
    private String name;
    private int price;
    private String price1;
    private String price2;
    private String price3;
    private double price4;
    private String totalPrice3;
    private String datetime;
    private String img;
    private String img1;
    private String img3;
    private String img4;
    private String img5;
    private String count;
    private int count1;
    private String count2;
    private String count3;
    private String count4;
    private String description;
    private String description1;
    private String address;
    private String phone;
    private String id;
    private String id1;
    private String id2;

    public ListItem(String id2,String name, int price, String count2, String img,String img4,String description1){
        this.name = name;
        this.price = price;
        this.count2 = count2;
        this.img = img;
        this.img4 = img4;
        this.description1 = description1;
        this.id2 = id2;
    }

    public ListItem(String count3,String price3,String totalPrice3,String img3){
        this.count3 = count3;
        this.price3 = price3;
        this.totalPrice3 = totalPrice3;
        this.img3 = img3;
    }

    public ListItem(String id,String name, String count, String price1, String img,String img5){
        this.id = id;
        this.name = name;
        this.count = count;
        this.price1 = price1;
        this.img = img;
        this.img5 = img5;
    }

    public ListItem(String id1,String address,String phone){
        this.id1 = id1;
        this.address = address;
        this.phone = phone;
    }

    public ListItem(String address,String phone){
        this.address = address;
        this.phone = phone;
    }

    public ListItem(String name,int count1,double price4,String img){
        this.name = name;
        this.count1 = count1;
        this.price4 = price4;
        this.img = img;
    }

    /*public ListItem(String name, double price1, String img){
        this.name = name;
        this.price1 = price1;
        this.img = img;
    }*/

    public ListItem(String description,String img1,double a){
        this.img1 = img1;
        this.description = description;
    }

    public ListItem(String price2,String datetime,int a){
        this.price2 = price2;
        this.datetime = datetime;
    }

    public String getId() { return id; }

    public String getId1() { return id1; }

    public String getId2() { return id2; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getDescription1() { return description1; }

    public int getPrice() { return price; }

    public String getPrice1() { return price1; }

    public String getPrice2() { return price2; }

    public String getPrice3() { return price3; }

    public double getPrice4() { return price4; }

    public String getTotalPrice3() { return totalPrice3; }

    public String getDatetime() { return datetime; }

    public String getCount() { return count; }

    public int getCount1() { return count1; }

    public String getCount2() { return count2; }

    public String getCount3() { return count3; }

    public String getCount4() { return count4; }

    public String getImg() { return img; }

    public String getImg1() { return img1; }

    public String getImg3() { return img3; }

    public String getImg4() { return img4; }

    public String getImg5() { return img5; }

    public String getAddress() { return address; }

    public String getPhone() { return phone; }
}
