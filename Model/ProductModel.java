package id.meteor.alfamind.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by bodacious on 11/12/17.
 */

public class ProductModel {

    private String plu;
    private String weight;
    private String margin_header_human;
    private String margin_human;
    private String margin_tambahan_header_human;
    private String margin_tambahan_human;
    private String margin_group_margin;
    private String points;
    private String id_product_alfacart;
    private String department_id;
    private String subdepartment_id;
    private String cattegoryName;
    private String subCategoryName;
    private String pruductId;
    private String name;
    private String type;
    private String image;
    private String imageHD;
    private String description;
    private String imagePath;
    private String bestPrice;
    private String brandName;
    private String discount;
    private long stock;
    private Bitmap bitmap;
    private int quantity;
    private String discountPercent;
    private String discounted_price;
    private String discount_percentage;
    private String begin_discount;
    private String end_discount;
    private String catId;
    private String catName;
    private boolean subProduct;
    private String subCatId;
    private String subCatName;
    private ArrayList<String> wobblerImageList=new ArrayList<>();
    private ArrayList<String> imageList=new ArrayList<>();
    private ArrayList<SubProductModel> subProductList=new ArrayList<>();

    public String getPruductId() {
        return pruductId;
    }

    public void setPruductId(String pruductId) {
        this.pruductId = pruductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(String bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {

        this.discount = discount;

        /*String temp = discount.replaceAll("%","");
        if(!TextUtils.isEmpty(temp)){
            this.discount = (long) (Long.parseLong(price) - (Long.parseLong(price) * (Double.parseDouble(temp) / 100)));;
        }else {
            this.discount = 0L;
        }*/
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageHD() {
        return imageHD;
    }

    public void setImageHD(String imageHD) {
        this.imageHD = imageHD;
    }

    public String getCattegoryName() {
        return cattegoryName;
    }

    public void setCattegoryName(String cattegoryName) {
        this.cattegoryName = cattegoryName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }


    public long getStock() {
        return stock;
    }

    public void setStock(String stock) {
        if(stock!=null && stock.equals("null") && stock.equals("") ){
            this.stock =0;
        }else {
            this.stock = Long.parseLong(stock);
        }

    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public ArrayList<SubProductModel> getSubProductList() {
        return subProductList;
    }

    public void setSubProductList(ArrayList<SubProductModel> subProductList) {
        this.subProductList = subProductList;
    }

    public String getPlu() {
        return plu;
    }

    public void setPlu(String plu) {
        this.plu = plu;
    }

    public String getId_product_alfacart() {
        return id_product_alfacart;
    }

    public void setId_product_alfacart(String id_product_alfacart) {
        this.id_product_alfacart = id_product_alfacart;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMargin_header_human() {
        return margin_header_human;
    }

    public void setMargin_header_human(String margin_header_human) {
        this.margin_header_human = margin_header_human;
    }

    public String getMargin_human() {
        return margin_human;
    }

    public void setMargin_human(String margin_human) {
        this.margin_human = margin_human;
    }

    public String getMargin_tambahan_header_human() {
        return margin_tambahan_header_human;
    }

    public void setMargin_tambahan_header_human(String margin_tambahan_header_human) {
        this.margin_tambahan_header_human = margin_tambahan_header_human;
    }

    public String getMargin_tambahan_human() {
        return margin_tambahan_human;
    }

    public void setMargin_tambahan_human(String margin_tambahan_human) {
        this.margin_tambahan_human = margin_tambahan_human;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getBegin_discount() {
        return begin_discount;
    }

    public void setBegin_discount(String begin_discount) {
        this.begin_discount = begin_discount;
    }

    public String getEnd_discount() {
        return end_discount;
    }

    public void setEnd_discount(String end_discount) {
        this.end_discount = end_discount;
    }

    public String getMargin_group_margin() {
        return margin_group_margin;
    }

    public void setMargin_group_margin(String margin_group_margin) {
        this.margin_group_margin = margin_group_margin;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getSubdepartment_id() {
        return subdepartment_id;
    }

    public void setSubdepartment_id(String subdepartment_id) {
        this.subdepartment_id = subdepartment_id;
    }

    public ArrayList<String> getWobblerImageList() {
        return wobblerImageList;
    }

    public void setWobblerImageList(ArrayList<String> wobblerImageList) {
        this.wobblerImageList = wobblerImageList;
    }

    public boolean getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(boolean subProduct) {
        this.subProduct = subProduct;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
