package id.meteor.alfamind.extra;

import java.util.ArrayList;
import java.util.HashMap;

import id.meteor.alfamind.Model.SubCategoryModel;

/**
 * Created by bodacious on 15/12/17.
 */

public class SubCatHolder {

    private static SubCatHolder instance;
    private HashMap<Integer, ArrayList<SubCategoryModel>> pList;

    public static synchronized SubCatHolder getInstance() {
        if (instance == null) {
            instance = new SubCatHolder();
        }
        return instance;
    }

    private SubCatHolder() {
        pList = new HashMap<>();
    }

    public void putSubCatList(String id, ArrayList<SubCategoryModel> model) {
        pList.put(Integer.parseInt(id), model);
    }

    public ArrayList<SubCategoryModel> getSubCatList(String id) {
        return pList.get(Integer.parseInt(id));
    }
}
