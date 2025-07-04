package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class ReviewDTO implements Serializable {
    private int id;
    private int userID;
    private int productID;
    private String title;
    private String description;
    private int rating;


    // Constructors
    public ReviewDTO() {}

    public ReviewDTO(int id, int userID, int productID, String title, String description, int rating) {
        this.id = id;
        this.userID = userID;
        this.productID = productID;
        this.title = title;
        this.description = description;
        this.rating = rating;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}
    public int getProductID() {return productID;}
    public void setProductID(int ProductID) {this.productID = ProductID;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public int getRating() {return rating;}
    public void setRating(int rating) {this.rating = rating;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewDTO that)) return false;
        return id == that.id &&
                userID == that.userID &&
                productID == that.productID &&
                rating == that.rating &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public String toString() {
        return "Review { " +
                "id: " + id + ", " +
                "userID: " + userID + ", " +
                "productID: " + productID + ", " +
                "title: '" + title + "', " +
                "description: '" + description + "', " +
                "rating: " + rating +
                " }";
    }
}
