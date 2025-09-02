package model.view;
import model.dto.ProductDTO;

public class WishlistDisplayItem {
    private ProductDTO product;

    public WishlistDisplayItem(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {return product;}
}
