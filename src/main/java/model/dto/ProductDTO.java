package model.dto;

import java.io.Serializable;
import java.util.Objects;

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

    public ProductDTO(int id, String name, String description, String brand, float price, String category, String seller, int stockQuantity, float vat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.seller = seller;
        this.stockQuantity = stockQuantity;
        this.vat = vat;
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
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public float getVat() { return vat; }
    public void setVat(float vat) { this.vat = vat; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDTO that)) return false;
        return id == that.id &&
                Float.compare(that.price, price) == 0 &&
                stockQuantity == that.stockQuantity &&
                Float.compare(that.vat, vat) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(category, that.category) &&
                Objects.equals(seller, that.seller);
    }


    @Override
    public String toString() {
        return "Product { " +
                "id: " + id + ", " +
                "name: '" + name + "', " +
                "description: '" + description + "', " +
                "brand: '" + brand + "', " +
                "price: " + price + ", " +
                "categotoStringry: '" + category + "', " +
                "seller: '" + seller + "', " +
                "stockQuantity: " + stockQuantity + ", " +
                "vat: " + vat +
                " }";
    }
}
