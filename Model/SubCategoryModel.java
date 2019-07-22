package id.meteor.alfamind.Model;

/**
 * Created by bodacious on 11/12/17.
 */

public class SubCategoryModel {

    private String cattegoryName;
    private String image;
    private int id;
    private String name;
    private String url;
    private String product_count;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCattegoryName() {
        return cattegoryName;
    }

    public void setCattegoryName(String cattegoryName) {
        this.cattegoryName = cattegoryName;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }
}
