package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable {
    public enum Category {
        DISPLAY,
        CAMERA,
        BATTERY,
        MICROPHONE,
        SPEAKER,
        CASE,
        BUTTON,
        SENSOR
    }

    public enum Grade {
        ORIGINAL,
        EXCELLENT,
        GREAT,
        GOOD
    }

    private int id;
    private String name;
    private String description;
    private String brand;
    private float price;
    private Category category;
    private Grade grade;
    private int stockQuantity;
    private float vat;

    // Constructors
    public ProductDTO(){}

    public ProductDTO(int id, String name, String description, String brand, float price, Category category, Grade grade, int stockQuantity, float vat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.grade = grade;
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
    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}
    public Grade getGrade() {return grade;}
    public void setGrade(Grade grade) {this.grade = grade;}
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
                Objects.equals(grade, that.grade);
    }


    @Override
    public String toString() {
        return "Product { " +
                "id: " + id + ", " +
                "name: '" + name + "', " +
                "description: '" + description + "', " +
                "brand: '" + brand + "', " +
                "price: " + price + ", " +
                "category: '" + category + "', " +
                "grade: '" + grade + "', " +
                "stockQuantity: " + stockQuantity + ", " +
                "vat: " + vat +
                " }";
    }
}
