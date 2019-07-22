package id.meteor.alfamind.extra;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

import id.meteor.alfamind.Model.ProductModel;

/**
 * Created by bodacious on 11/12/17.
 */

public class ProductHolder {

    private static ProductHolder instance;
    private SparseArray<ProductModel> qbUserSparseArray;
    private HashMap<Integer,ArrayList<ProductModel>> pList;

    public static synchronized ProductHolder getInstance() {
        if (instance == null) {
            instance = new ProductHolder();
        }
        return instance;
    }

    private ProductHolder() {
        qbUserSparseArray = new SparseArray<>();
        pList= new HashMap<>();
    }
    public void putProductDetail(ProductModel model) {
        qbUserSparseArray.put(Integer.parseInt(model.getPruductId()), model);
    }

    public ProductModel getProductDetail(int id) {
        return qbUserSparseArray.get(id);
    }

    public void putProductList(int id,ArrayList<ProductModel> model) {
        pList.put(id, model);
    }

    public ArrayList<ProductModel> getProductList(int id) {
        return pList.get(id);
    }
}
