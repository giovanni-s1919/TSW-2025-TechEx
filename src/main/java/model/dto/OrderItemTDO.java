package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemTDO implements Serializable {
    private int id;
    private int orderID;
    private String itemName;
    private String itemDescription;
    private String itemBrand;
    private float itemPrice;
    private String itemCategory;
    private String itemSeller;
    private int itemQuantity;
    private float itemVAT;


    // Constructors
    public OrderItemTDO(){}

    public OrderItemTDO(int id, int orderID, String itemName, String itemDescription, String itemBrand, float itemPrice, String itemCategory, String itemSeller, int itemQuantity, float itemVAT) {
        this.id = id;
        this.orderID = orderID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemBrand = itemBrand;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemSeller = itemSeller;
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
    public String getItemCategory(){return itemCategory;}
    public void setItemCategory(String itemCategory){this.itemCategory = itemCategory;}
    public String getItemSeller(){return itemSeller;}
    public void setItemSeller(String itemSeller){this.itemSeller = itemSeller;}
    public int getItemQuantity(){return itemQuantity;}
    public void setItemQuantity(int itemQuantity){this.itemQuantity = itemQuantity;}
    public float getItemVAT(){return itemVAT;}
    public void setItemVAT(float itemVAT){this.itemVAT = itemVAT;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemTDO that = (OrderItemTDO) o;
        return id == that.id
                && orderID == that.orderID
                && Float.compare(that.itemPrice, itemPrice) == 0
                && Objects.equals(itemName, that.itemName)
                && Objects.equals(itemDescription, that.itemDescription)
                && Objects.equals(itemBrand, that.itemBrand)
                && Objects.equals(itemCategory, that.itemCategory)
                && Objects.equals(itemSeller, that.itemSeller)
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
                "itemSeller: '" + itemSeller + "', " +
                "itemQuantity: " + itemQuantity + ", " +
                "itemVAT: " + itemVAT +
                " }";
    }
}
