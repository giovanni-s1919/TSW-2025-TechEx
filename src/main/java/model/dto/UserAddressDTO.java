package model.dto;

import java.io.Serializable;

public class UserAddressDTO implements Serializable
{
    private int addressId;
    private int userId;
    private boolean isDefault;


    // Constructors
    public UserAddressDTO(){}

    public UserAddressDTO(int addressId, int userId, boolean isDefault)
    {
        this.addressId = addressId;
        this.userId = userId;
        this.isDefault = isDefault;
    }


    // Getters and Setters
    public int getAddressId(){return this.addressId;}
    public void setAddressId(int addressId){this.addressId = addressId;}
    public int getUserId(){return this.userId;}
    public void setUserId(int userId){this.userId = userId;}
    public boolean isDefault() { return this.isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAddressDTO that)) return false;
        return addressId == that.addressId &&
                userId == that.userId &&
                isDefault == that.isDefault;
    }

    @Override
    public String toString() {
        return "UserAddress { " +
                "addressId: " + addressId + ", " +
                "userId: " + userId + ", " +
                "isDefault: " + isDefault +
                " }";
    }
}
