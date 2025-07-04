package model.dto;

import java.io.Serializable;

public class WishlistItemDTO implements Serializable {
    private int id;
    private int wishlistID;
    private int productID;


    // Constructors
    public WishlistItemDTO(){}

    public WishlistItemDTO(int id, int wishlistID, int productID) {
        this.id = id;
        this.wishlistID = wishlistID;
        this.productID = productID;
    }


    // Getters and Setters
    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public int getWishlistID(){return wishlistID;}
    public void setWishlistID(int id){this.wishlistID = id;}
    public int getProductID(){return productID;}
    public void setProductID(int id){this.productID = id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WishlistItemDTO that)) return false;
        return id == that.id &&
                wishlistID == that.wishlistID &&
                productID == that.productID;
    }

    @Override
    public String toString() {
        return "WishlistItemDTO { " +
                "id: " + id + ", " +
                "wishlistID: " + wishlistID + ", " +
                "productID: " + productID +
                " }";
    }
}
