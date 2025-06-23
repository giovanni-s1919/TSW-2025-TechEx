package model.dto;

import java.io.Serializable;

public class UserAddressDTO implements Serializable
{
    private int addressId;
    private int userId;
    private AddressType addressType;
    private String name;
    private String surname;
    private String phone;
    private boolean isDefault;


    // Constructors
    public UserAddressDTO(){}

    public UserAddressDTO(int addressId, int userId, AddressType addressType, String name, String surname, String phone, boolean isDefault)
    {
        this.addressId = addressId;
        this.userId = userId;
        this.addressType = addressType;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.isDefault = isDefault;
    }


    // Getters and Setters
    public int getAddressId(){return this.addressId;}
    public void setAddressId(int addressId){this.addressId = addressId;}
    public int getUserId(){return this.userId;}
    public void setUserId(int userId){this.userId = userId;}
    public AddressType getAddressType(){return this.addressType;}
    public void setAddressType(AddressType addressType){this.addressType = addressType;}
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
    public String getSurname(){return this.surname;}
    public void setSurname(String surname){this.surname = surname;}
    public String getPhone(){return this.phone;}
    public void setPhone(String phone){this.phone = phone;}
    public boolean getIsDefault(){return this.isDefault;}
    public void setIsDefault(boolean isDefault){this.isDefault = isDefault;}


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddressDTO userAddressDTO = (UserAddressDTO) o;
        return addressId == userAddressDTO.addressId &&
                userId == userAddressDTO.userId &&
                addressType == userAddressDTO.addressType &&
                name.equals(userAddressDTO.name) &&
                surname.equals(userAddressDTO.surname) &&
                phone.equals(userAddressDTO.phone) &&
                isDefault == userAddressDTO.isDefault;
    }

    @Override
    public String toString()
    {
        return "UserAddress { address id: " + addressId +
                " user id: " + userId +
                " name: " + name +
                " surname: " + surname +
                " phone: " + phone +
                " is default: " + isDefault + " }";
    }


    public enum AddressType{
        Shipping,
        Billing;

        @Override
        public String toString() {
            return switch (this) {
                case Shipping -> "Spedizione";
                case Billing -> "Fatturazione";
                default -> "";
            };
        }
    }
}
