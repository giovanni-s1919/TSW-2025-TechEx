package model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDate;

public class OrderDTO implements Serializable
{
    private int id;
    private int userID;
    private Timestamp orderDate;
    private String orderStatus;
    private LocalDate deliveryDate;
    private float totalAmount;
    private int shippingAddress;
    private int billingAddress;


    // Constructors
    public OrderDTO(){}

    public OrderDTO(int id, int userID, Timestamp orderDate, String orderStatus, LocalDate deliveryDate, int billingAddress, int shippingAddress, float totalAmount){
        this.id = id;
        this.userID = userID;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
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
    public int getBillingAddress() {return billingAddress;}
    public void setBillingAddress(int billingAddress) {this.billingAddress = billingAddress;}
    public int getShippingAddress() {return shippingAddress;}
    public void setShippingAddress(int shippingAddress) {this.shippingAddress = shippingAddress;}
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
                && billingAddress == orderDTO.billingAddress
                && shippingAddress == orderDTO.shippingAddress
                && totalAmount == orderDTO.totalAmount;
    }

    @Override
    public String toString() {
        return "Order { id: " + id +
                ", userID: " + userID +
                ", orderDate: " + orderDate +
                ", orderStatus: " + orderStatus +
                ", deliveryDate: " + deliveryDate +
                ", billingAddress: " + billingAddress +
                ", total: " + totalAmount + " }";
    }
}
