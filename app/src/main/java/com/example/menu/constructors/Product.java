package com.example.menu.constructors;

public class Product {
    int id;
    String name;
    String price;
    String address;
    String count;
    String img;

    public Product(){

    }

    public Product(String name,String count){
        this.name = name;
        this.count = count;
    }

    public Product(String name,String address,String count,String price){
        this.name = name;
        this.address = address;
        this.count = count;
        this.price = price;
    }

    public Product(int id, String name,String address,String count,String price){
        this.id = id;
        this.name = name;
        this.address = address;
        this.count = count;
        this.price = price;
    }
    public Product(String name,String address,String count,String price,String img){
        this.name = name;
        this.address = address;
        this.count = count;
        this.price = price;
        this.img = img;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddrees() {
        return address;
    }

    public void setAddrees(String address) {
        this.address = address;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImg() { return img; }

    public void setImg(String img) { this.img = img; }
}
