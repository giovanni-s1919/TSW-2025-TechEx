package model.dto;

import java.io.Serializable;

public class WishlistDTO implements Serializable {
    private int id;
    private int userID;

    public WishlistDTO(){}

    public WishlistDTO(int id, int userID) {
        this.id = id;
        this.userID = userID;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WishlistDTO that)) return false;
        return id == that.id && userID == that.userID;
    }

    @Override
    public String toString() {
        return "WishlistDTO { " +
                "id: " + id + ", " +
                "userID: " + userID +
                " }";
    }
}
