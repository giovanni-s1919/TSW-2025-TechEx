package model.dto;

import java.io.Serializable;

public class WishlistItemTDO implements Serializable {
    private int id;
    private int wishlistID;
    private int productID;


    // Constructors
    public WishlistItemTDO(){}

    public WishlistItemTDO(int id, int wishlistID, int productID) {
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
}
