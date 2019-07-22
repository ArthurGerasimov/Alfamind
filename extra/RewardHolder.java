package id.meteor.alfamind.extra;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

import id.meteor.alfamind.Model.RewardModel;

/**
 * Created by bodacious on 22/12/17.
 */

public class RewardHolder {

    private static id.meteor.alfamind.extra.RewardHolder instance;
    private SparseArray<RewardModel> qbUserSparseArray;
    private HashMap<Integer, ArrayList<RewardModel>> pList;

    public static synchronized id.meteor.alfamind.extra.RewardHolder getInstance() {
        if (instance == null) {
            instance = new id.meteor.alfamind.extra.RewardHolder();
        }
        return instance;
    }

    private RewardHolder() {
        qbUserSparseArray = new SparseArray<>();
        pList = new HashMap<>();
    }

    public void putProductDetail(RewardModel model) {
        qbUserSparseArray.put(Integer.parseInt(model.getPruductId()), model);
    }

    public RewardModel getProductDetail(int id) {
        return qbUserSparseArray.get(id);
    }

    public void putProductList(int id, ArrayList<RewardModel> model) {
        pList.put(id, model);
    }

    public ArrayList<RewardModel> getProductList(int id) {
        return pList.get(id);
    }
}
