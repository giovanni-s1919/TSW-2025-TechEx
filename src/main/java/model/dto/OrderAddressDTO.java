package model.dto;

import java.io.Serializable;

public class OrderAddressDTO implements Serializable
{
    private int id;
    private String street;
    private String city;
    private String postalCode;
    private String region;
    private String country;
    private AddressType addressType;


    // Constructor
    public OrderAddressDTO(){}

    public OrderAddressDTO(int id, String street, String city, String postalCode, String region, String country, AddressType addressType) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.region = region;
        this.country = country;
        this.addressType = addressType;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getStreet() {return street;}
    public void setStreet(String street) {this.street = street;}
    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}
    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public String getRegion() {return region;}
    public void setRegion(String region) {this.region = region;}
    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}
    public AddressType getAddressType() {return addressType;}
    public void setAddressType(AddressType addressType) {this.addressType = addressType;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressDTO that = (OrderAddressDTO) o;
        return id == that.id &&
                street.equals(that.street) &&
                city.equals(that.city) &&
                postalCode.equals(that.postalCode) &&
                region.equals(that.region) &&
                country.equals(that.country) &&
                addressType == that.addressType;
    }

    @Override
    public String toString()
    {
        return "Order Address { id: " + id +
                " street: " + street +
                " city: " + city +
                " postal code: " + postalCode +
                " region: " + region +
                " country: " + country +
                " addressType: " + addressType;
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
