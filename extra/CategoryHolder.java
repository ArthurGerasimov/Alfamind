package id.meteor.alfamind.extra;

import java.util.ArrayList;
import java.util.HashMap;

import id.meteor.alfamind.Model.CategoryModel;
import id.meteor.alfamind.Model.ProductModel;

/**
 * Created by bodacious on 20/12/17.
 */

public class CategoryHolder {

    private static CategoryHolder instance;

    private HashMap<String,ArrayList<CategoryModel>> catList;



    public static synchronized CategoryHolder getInstance() {
        if (instance == null) {
            instance = new CategoryHolder();
        }

        return instance;
    }

    private CategoryHolder() {

        catList=new HashMap<>();
    }
    public void putCategory(ProductModel model) {

    }

    public ProductModel getProductDetail(int id) {
        return null ;
    }


    public void putCategoryList(String id,ArrayList<CategoryModel> model) {
        catList.put(id, model);
    }

    public ArrayList<CategoryModel> getCategoryList(String id) {
        return catList.get(id);
    }



}
