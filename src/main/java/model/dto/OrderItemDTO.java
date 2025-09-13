package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemDTO implements Serializable {
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
    private int orderID;
    private String itemName;
    private String itemDescription;
    private String itemBrand;
    private float itemPrice;
    private Category itemCategory;
    private Grade itemGrade;
    private int itemQuantity;
    private float itemVAT;


    // Constructors
    public OrderItemDTO(){}

    public OrderItemDTO(int id, int orderID, String itemName, String itemDescription, String itemBrand, float itemPrice, Category itemCategory, Grade itemGrade, int itemQuantity, float itemVAT) {
        this.id = id;
        this.orderID = orderID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemBrand = itemBrand;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemGrade = itemGrade;
        this.itemQuantity = itemQuantity;
        this.itemVAT = itemVAT;
    }


    // Getters and Setters
    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public int getOrderID(){return orderID;}
    public void setOrderID(int orderID){this.orderID = orderID;}
    public String getItemName(){return itemName;}
    public void setItemName(String itemName){this.itemName = itemName;}
    public String getItemDescription(){return itemDescription;}
    public void setItemDescription(String itemDescription){this.itemDescription = itemDescription;}
    public String getItemBrand(){return itemBrand;}
    public void setItemBrand(String itemBrand){this.itemBrand = itemBrand;}
    public float getItemPrice(){return itemPrice;}
    public void setItemPrice(float itemPrice){this.itemPrice = itemPrice;}
    public Category getItemCategory(){return itemCategory;}
    public void setItemCategory(Category itemCategory){this.itemCategory = itemCategory;}
    public Grade getItemGrade(){return itemGrade;}
    public void setItemGrade(Grade itemGrade){this.itemGrade = itemGrade;}
    public int getItemQuantity(){return itemQuantity;}
    public void setItemQuantity(int itemQuantity){this.itemQuantity = itemQuantity;}
    public float getItemVAT(){return itemVAT;}
    public void setItemVAT(float itemVAT){this.itemVAT = itemVAT;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDTO that = (OrderItemDTO) o;
        return id == that.id
                && orderID == that.orderID
                && Float.compare(that.itemPrice, itemPrice) == 0
                && Objects.equals(itemName, that.itemName)
                && Objects.equals(itemDescription, that.itemDescription)
                && Objects.equals(itemBrand, that.itemBrand)
                && Objects.equals(itemCategory, that.itemCategory)
                && Objects.equals(itemGrade, that.itemGrade)
                && itemQuantity == that.itemQuantity
                && Objects.equals(itemVAT, that.itemVAT);
    }

    @Override
    public String toString() {
        return "OrderItem { " +
                "id: " + id + ", " +
                "orderID: " + orderID + ", " +
                "itemName: '" + itemName + "', " +
                "itemDescription: '" + itemDescription + "', " +
                "itemBrand: '" + itemBrand + "', " +
                "itemPrice: " + itemPrice + ", " +
                "itemCategory: '" + itemCategory + "', " +
                "itemGrade: '" + itemGrade + "', " +
                "itemQuantity: " + itemQuantity + ", " +
                "itemVAT: " + itemVAT +
                " }";
    }
}
