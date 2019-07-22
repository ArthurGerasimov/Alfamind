package id.meteor.alfamind.Model;

/**
 * Created by bodacious on 21/12/17.
 */

public class SubProductModel {


    private String plu;
    private String productId;
    private String id_product_alfacart;
    private String name;
    private String base_price;
    private String discounted_price;
    private String stock;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getId_product_alfacart() {
        return id_product_alfacart;
    }

    public void setId_product_alfacart(String id_product_alfacart) {
        this.id_product_alfacart = id_product_alfacart;
    }

    public String getPlu() {
        return plu;
    }

    public void setPlu(String plu) {
        this.plu = plu;
    }
}
