package id.meteor.alfamind.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by bodacious on 22/12/17.
 */

public class RewardModel {


        private String id;
        private String category;
        private String title;
        private String description;
        private String image;
        private String imageHD;
        private String required_points;
        private String offer_validity_start;
        private String offer_validity_end;
        private long stock;
        private Bitmap bitmap;
        private String status;
        private ArrayList<String> imageList=new ArrayList<>();
        private ArrayList<SubProductModel> subProductList=new ArrayList<>();

        public String getPruductId() {
            return id;
        }

        public void setPruductId(String pruductId) {
            this.id = pruductId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String name) {
            this.title = name;
        }

        public void setStatus(String name){
          this.status = name;
        }

        public String getDescription() {
            return description;
        }
    public String getStatus() {
        return status;
    }


        public void setDescription(String type) {
            this.description = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }



        public String getRequired_points() {
            return required_points;
        }

        public void setRequired_points(String imagePath) {
            this.required_points = imagePath;
        }

        public ArrayList<String> getImageList() {
            return imageList;
        }

        public void setImageList(ArrayList<String> imageList) {
            this.imageList = imageList;
        }

        public String getOffer_validity_start() {
            return offer_validity_start;
        }

        public void setOffer_validity_start(String bestPrice) {
            this.offer_validity_start = bestPrice;
        }

        public String getOffer_validity_end() {
            return offer_validity_end;
        }

        public void setOffer_validity_end(String brandName) {
            this.offer_validity_end = brandName;
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
            return category;
        }

        public void setCattegoryName(String cattegoryName) {
            this.category = cattegoryName;
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

        public ArrayList<SubProductModel> getSubProductList() {
            return subProductList;
        }

        public void setSubProductList(ArrayList<SubProductModel> subProductList) {
            this.subProductList = subProductList;
        }
}
