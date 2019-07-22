package id.meteor.alfamind.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bodacious on 12/12/17.
 */

public class ImagesHolder {

    private static ImagesHolder instance;
    private HashMap<String, Bitmap> imageHash;
    private SparseArray<ArrayList<Bitmap>> qbUserSparseArray;

    public static synchronized ImagesHolder getInstance() {
        if (instance == null) {
            instance = new ImagesHolder();
        }
        return instance;
    }

    private ImagesHolder() {
        qbUserSparseArray = new SparseArray<>();
        imageHash = new HashMap<>();
    }

    public void putProductDetail(ArrayList<Bitmap> imageList, int id) {
        qbUserSparseArray.put(id, imageList);
    }

    public ArrayList<Bitmap> getProductDetail(int id) {
        return qbUserSparseArray.get(id);
    }

    public void putImageBitmap(String id, Bitmap bitmap) {
        imageHash.put(id, bitmap);
    }

    public void putImageBitmap(Context context, String id) {
    }

    public Bitmap getImageBitmap(String id) {
        return imageHash.get(id);
    }

}
