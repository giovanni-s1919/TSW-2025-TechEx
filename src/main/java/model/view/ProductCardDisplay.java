package model.view;

import model.dto.ProductDTO;

public class ProductCardDisplay {
    private ProductDTO product;

    public ProductCardDisplay(ProductDTO product) {
        this.product = product;
    }
    public ProductDTO getProduct() {return product;}
}
