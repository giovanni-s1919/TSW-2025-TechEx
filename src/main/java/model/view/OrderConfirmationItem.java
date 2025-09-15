package model.view;

import model.dto.OrderItemDTO;
import model.dto.ProductDTO;

public class OrderConfirmationItem {

    private final OrderItemDTO orderItem;
    private final ProductDTO product;

    public OrderConfirmationItem(OrderItemDTO orderItem, ProductDTO product) {
        this.orderItem = orderItem;
        this.product = product;
    }

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    public ProductDTO getProduct() {
        return product;
    }
}