package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable {
    public enum Category {
        Display,
        Camera,
        Battery,
        Microphone,
        Speaker,
        Case,
        Button,
        Sensor;

        @Override
        public String toString() {
            return switch (this) {
                case Display -> "Display & Touchscreen";
                case Camera -> "Fotocamera";
                case Battery -> "Batteria";
                case Microphone -> "Microfono";
                case Speaker -> "Altoparlante";
                case Case -> "Scocca";
                case Button -> "Tasto";
                case Sensor -> "Sensore";
                default -> name();
            };
        }
    }

    public enum Grade {
        Original,
        Excellent,
        Great,
        Good;

        @Override
        public String toString() {
            return switch (this) {
                case Original -> "Originale";
                case Excellent -> "Eccellente";
                case Great -> "Ottimo";
                case Good -> "Buono";
                default -> name();
            };
        }
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

    public String getCategoryTranslated() {
        return this.category != null ? this.category.toString() : "";
    }
    public String getGradeTranslated() {
        return this.grade != null ? this.grade.toString() : "";
    }

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
