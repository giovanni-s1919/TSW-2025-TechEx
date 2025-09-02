package model.view;


import model.dto.ProductDTO;

public class CartDisplayItem {
    private ProductDTO product;
    private int quantity;

    public CartDisplayItem(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public int getQuantity(){
        return quantity;
    }

    public Float getSubTotal(){
        return product.getPrice()*quantity;
    }
}
