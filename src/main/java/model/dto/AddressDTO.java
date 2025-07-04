package model.dto;

import java.io.Serializable;

public class AddressDTO implements Serializable
{
    private int id;
    private String street;
    private String additionalInfo; // can be null
    private String city;
    private String postalCode;
    private String region; // can be null
    private String country;


    // Constructors
    public AddressDTO() {}

    public AddressDTO(int id, String street, String additionalInfo, String city, String postalCode, String region, String country)
    {
        this.id = id;
        this.street = street;
        this.additionalInfo = additionalInfo;
        this.city = city;
        this.postalCode = postalCode;
        this.region = region;
        this.country = country;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getStreet() {return street;}
    public void setStreet(String street) {this.street = street;}
    public String getAdditionalInfo() {return additionalInfo;}
    public void setAdditionalInfo(String additionalInfo) {this.additionalInfo = additionalInfo;}
    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}
    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public String getRegion() {return region;}
    public void setRegion(String region) {this.region = region;}
    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDTO addressDTO)) return false;
        return id == addressDTO.id &&
                street.equals(addressDTO.street) &&
                additionalInfo.equals(addressDTO.additionalInfo) &&
                city.equals(addressDTO.city) &&
                postalCode.equals(addressDTO.postalCode) &&
                region.equals(addressDTO.region) &&
                country.equals(addressDTO.country);
    }

    @Override
    public String toString() {
        return "Address { id: " + id +
                " street: " + street +
                " additional info: " + additionalInfo +
                " city: " + city +
                " postal code: " + postalCode +
                " region: " + region +
                " country: " + country;
    }
}
