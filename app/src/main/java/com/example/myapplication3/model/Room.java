package com.example.myapplication3.model;

import java.io.Serializable;

public class Room implements Serializable {
    private String id;
    private String name;
    private double price;
    private boolean isOccupied;
    private String tenantName;
    private String tenantPhone;

    public Room(String id, String name, double price, boolean isOccupied, String tenantName, String tenantPhone) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isOccupied = isOccupied;
        this.tenantName = tenantName;
        this.tenantPhone = tenantPhone;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getTenantPhone() { return tenantPhone; }
    public void setTenantPhone(String tenantPhone) { this.tenantPhone = tenantPhone; }
}
