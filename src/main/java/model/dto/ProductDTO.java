package model.dto;

import java.io.Serializable;

public class ProductDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String brand;
    private float price;
    private String category;
    private String seller;
    private int stockQuantity;
    private float vat;


    // Constructors
    public ProductDTO(){}

    public ProductDTO(int id, String name, String description, String brand, float price, String category, String seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.seller = seller;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getBrand() {return brand;}
    public void setBrand(String brand) {this.brand = brand;}
    public float getPrice() {return price;}
    public void setPrice(float price) {this.price = price;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
    public String getSeller() {return seller;}
    public void setSeller(String seller) {this.seller = seller;}
}
