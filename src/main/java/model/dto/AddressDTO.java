package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class AddressDTO implements Serializable
{
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

    private int id;
    private String street;
    private String additionalInfo;
    private String city;
    private String postalCode;
    private String region;
    private String country;
    private String name;
    private String surname;
    private String phone;
    private AddressType addressType;
    private boolean isDefault;


    public AddressDTO() {}

    public AddressDTO(int id, String street, String additionalInfo, String city, String postalCode, String region, String country, String name, String surname, String phone, AddressType addressType)
    {
        this.id = id;
        this.street = street;
        this.additionalInfo = additionalInfo;
        this.city = city;
        this.postalCode = postalCode;
        this.region = region;
        this.country = country;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.addressType = addressType;
    }


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
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getSurname(){return surname;}
    public void setSurname(String surname){this.surname = surname;}
    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone = phone;}
    public AddressType getAddressType(){return addressType;}
    public void setAddressType(AddressType addressType){this.addressType = addressType;}
    public boolean isDefault(){return isDefault;}
    public void setDefault(boolean aDefault){isDefault = aDefault;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDTO addressDTO)) return false;
        return id == addressDTO.id &&
                street.equals(addressDTO.street) &&
                Objects.equals(additionalInfo, addressDTO.additionalInfo) &&
                city.equals(addressDTO.city) &&
                postalCode.equals(addressDTO.postalCode) &&
                Objects.equals(region, addressDTO.region) &&
                country.equals(addressDTO.country) &&
                name.equals(addressDTO.name) &&
                surname.equals(addressDTO.surname) &&
                Objects.equals(phone, addressDTO.phone) &&
                addressType == addressDTO.addressType;
    }

    @Override
    public String toString() {
        return "Address { id: " + id +
                " street: " + street +
                " additional info: " + additionalInfo +
                " city: " + city +
                " postal code: " + postalCode +
                " region: " + region +
                " country: " + country +
                ", name: " + name +
                ", surname: " + surname +
                ", phone: " + phone +
                ", addressType: " + addressType +
                " }";
    }
}
