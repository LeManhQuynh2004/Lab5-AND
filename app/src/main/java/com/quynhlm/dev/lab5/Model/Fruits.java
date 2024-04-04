package com.quynhlm.dev.lab5.Model;

public class Fruits {
    private String _id;
    private String name;
    private Integer price;
    private Integer quantity;
    private Integer status;
    private String description;
    private String distributor;
    private String [] image;

    public Fruits() {
    }

    public Fruits(String _id,String name, Integer price, Integer quantity, Integer status, String description, String distributor, String[] image) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.distributor = distributor;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }
}
