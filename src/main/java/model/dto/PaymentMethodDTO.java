package model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PaymentMethodDTO implements Serializable
{
    private int id;
    private int userID;
    private String number;
    private LocalDate expiration;
    private String name;
    private boolean isDefault;

    // Constructor
    public PaymentMethodDTO() {}

    public PaymentMethodDTO(int id, int userID, String number, LocalDate expiration, String name, boolean isDefault) {
        this.id = id;
        this.userID = userID;
        this.number = number;
        this.expiration = expiration;
        this.name = name;
        this.isDefault = isDefault;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public LocalDate getExpiration() {return expiration;}
    public void setExpiration(LocalDate expiration) {this.expiration = expiration;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public boolean isDefault() {return isDefault;}
    public void setDefault(boolean isDefault) {this.isDefault = isDefault;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethodDTO paymentMethodDTO = (PaymentMethodDTO) o;
        return id == paymentMethodDTO.id &&
                userID == paymentMethodDTO.userID &&
                number.equals(paymentMethodDTO.number) &&
                expiration.equals(paymentMethodDTO.expiration) &&
                name.equals(paymentMethodDTO.name) &&
                isDefault == paymentMethodDTO.isDefault;
    }

    @Override
    public String toString() {
        return "PaymentMethod { id : " + id +
                " user id: " + userID +
                " number: " + number +
                " expiration: " + expiration +
                " name: " + name +
                " isDefault: " + isDefault + " }";
    }
}
