package model.dto;

import java.io.Serializable;
import java.security.Timestamp;
import java.time.LocalDate;

public class OrderDTO implements Serializable
{
    private int id;
    private int userID;
    private Timestamp orderDate;
    private String orderStatus;
    private LocalDate deliveryDate;
    private float totalAmount;
    private int shippingAddressId;
    private int billingAddressId;


    // Constructors
    public OrderDTO(){}

    public OrderDTO(int id, int userID, Timestamp orderDate, String orderStatus, LocalDate deliveryDate, int billingAddressId, int shippingAddressId, float totalAmount){
        this.id = id;
        this.userID = userID;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.billingAddressId = billingAddressId;
        this.shippingAddressId = shippingAddressId;
        this.totalAmount = totalAmount;
    }


    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}
    public Timestamp getOrderDate() {return orderDate;}
    public void setOrderDate(Timestamp orderDate) {this.orderDate = orderDate;}
    public String getOrderStatus() {return orderStatus;}
    public void setOrderStatus(String orderStatus) {this.orderStatus = orderStatus;}
    public LocalDate getDeliveryDate() {return deliveryDate;}
    public void setDeliveryDate(LocalDate deliveryDate) {this.deliveryDate = deliveryDate;}
    public int getBillingAddressId() {return billingAddressId;}
    public void setBillingAddressId(int billingAddressId) {this.billingAddressId = billingAddressId;}
    public int getShippingAddressId() {return shippingAddressId;}
    public void setShippingAddressId(int shippingAddressId) {this.shippingAddressId = shippingAddressId;}
    public float getTotalAmount() {return totalAmount;}
    public void setTotalAmount(float totalAmount) {this.totalAmount = totalAmount;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id
                && userID == orderDTO.userID
                && orderDate.equals(orderDTO.orderDate)
                && orderStatus.equals(orderDTO.orderStatus)
                && deliveryDate.equals(orderDTO.deliveryDate)
                && billingAddressId == orderDTO.billingAddressId
                && shippingAddressId == orderDTO.shippingAddressId
                && totalAmount == orderDTO.totalAmount;
    }

    @Override
    public String toString() {
        return "Order { id: " + id +
                ", userID: " + userID +
                ", orderDate: " + orderDate +
                ", orderStatus: " + orderStatus +
                ", deliveryDate: " + deliveryDate +
                ", billingAddressId: " + billingAddressId +
                ", shippingAddressId: " + shippingAddressId +
                ", total: " + totalAmount + " }";
    }
}
