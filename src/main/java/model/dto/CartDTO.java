package model.dto;

import java.io.Serializable;

public class CartDTO implements Serializable {
    private int id;
    private int userID;


    // Constructors
    public CartDTO(){}

    public CartDTO(int id, int userID) {
        this.id = id;
        this.userID = userID;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}
}
