package model.dto;

import java.io.Serializable;

public class CartItemDTO implements Serializable {
    private int id;
    private int cartID;
    private int productID;
    private int quantity;


    // Constructors
    public CartItemDTO() {}

    public CartItemDTO(int id, int cartID, int productID, int quantity) {
        this.id = id;
        this.cartID = cartID;
        this.productID = productID;
        this.quantity = quantity;
    }


    // Getters and Setters
    public int  getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getCartID() {return cartID;}
    public void setCartID(int cartID) {this.cartID = cartID;}
    public int getProductID() {return productID;}
    public void setProductID(int productID) {this.productID = productID;}
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemDTO cartItemDTO)) return false;
        return id == cartItemDTO.id &&
                cartID == cartItemDTO.cartID &&
                productID == cartItemDTO.productID &&
                quantity == cartItemDTO.quantity;
    }

    @Override
    public String toString() {
        return "CartItem { " +
                "id: " + id + ", " +
                "cartID: " + cartID + ", " +
                "productID: " + productID + ", " +
                "quantity: " + quantity +
                " }";
    }
}
