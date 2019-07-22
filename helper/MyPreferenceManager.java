package id.meteor.alfamind.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bodacious-dev-8 on 26/6/17.
 */

public class MyPreferenceManager {

    private Context _context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MyPreferences";



    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void clear() {
      editor.clear();
        editor.commit();
    }

    // Status.............................

    public boolean getIsFirstTime() {
        return pref.getBoolean(Constant.FIRST_TIME,true);
    }

    public boolean getIntipMargin() {
        return pref.getBoolean(Constant.INTIP_MARGIN,false);
    }

    public void setIntipMargin(boolean intipMargin) {
        editor.putBoolean(Constant.INTIP_MARGIN,intipMargin);
        editor.commit();
    }

    public void setIsFirstTime(boolean first) {
        editor.putBoolean(Constant.FIRST_TIME,first);
        editor.commit();
    }

    public boolean getIsLogged() {
        return pref.getBoolean(Constant.IS_LOGGED,false);
    }

    public void setIsLogged(boolean isLogged) {
        editor.putBoolean(Constant.IS_LOGGED,isLogged);
        editor.commit();
    }


    public String getRememberPass() {
        return pref.getString(Constant.REMBER_PASSWORD,null);
    }

    public void setRememberPass(String pass) {
        editor.putString(Constant.REMBER_PASSWORD,pass);
        editor.commit();
    }

    public String getRememberEmail() {
        return pref.getString(Constant.REMEMBER_EMAIL,null);
    }

    public void setRememberEmail(String remEmail) {
        editor.putString(Constant.REMEMBER_EMAIL,remEmail);
        editor.commit();
    }
    public String getAccessToken() {
        return pref.getString(Constant.USER_ACCESS_TOKEN,null);
    }

    public void setAccessToken(String image) {
        editor.putString(Constant.USER_ACCESS_TOKEN,image);
        editor.commit();

    }



    public String getImage() {
        return pref.getString(Constant.imageKey,null);
    }

    public void setImage(String image) {
        editor.putString(Constant.imageKey,image);
        editor.commit();

    }

    // hold user details................................

    public String getEmail() {
        return pref.getString(Constant.email_key,null);
    }

    public void setEmail(String email){
        editor.putString(Constant.email_key,email);
        editor.commit();
    }

    public String getCartID() {
        return pref.getString(Constant.CART_KEY,"0");
    }

    public void setCartId(String id){
        editor.putString(Constant.CART_KEY,id);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(Constant.password_key,null);
    }

    public void setPassword(String password) {
        editor.putString(Constant.password_key,password);
        editor.commit();
    }

    public String getFirstName() {
        return pref.getString(Constant.firstNameKey,null);
    }

    public void setFirstName(String firstName) {
        editor.putString(Constant.firstNameKey,firstName);
        editor.commit();
    }

    public String getLastName() {
        return pref.getString(Constant.lastNameKey,null);
    }

    public void setLastName(String lastName) {
        editor.putString(Constant.lastNameKey,lastName);
        editor.commit();
    }

    public String getGender() {
        return pref.getString(Constant.genderKey,null);
    }

    public void setGender(String gender) {
        editor.putString(Constant.genderKey,gender);
        editor.commit();
    }

    public String getPhoneNumber() {
        return pref.getString(Constant.phoneNumberKey,null);
    }

    public void setPhoneNumber(String phoneNumber) {
        editor.putString(Constant.phoneNumberKey,phoneNumber);
        editor.commit();
    }


    //// DateOfBirth
    public String getDOB() {
        return pref.getString(Constant.DOB,null);
    }

    public void setDOB(String dob) {
        editor.putString(Constant.DOB,dob);
        editor.commit();
    }

    //// COUSTMER_ID.........
    public String getCoustmerId() {
        return pref.getString(Constant.COUSTMER_ID,null);
    }

    public void setCoustmerId(String coustmerId) {
        editor.putString(Constant.COUSTMER_ID,coustmerId);
        editor.commit();
    }


    // COUSTMER_ID_ORA.........
    public String getCoustmerIdOra() {
        return pref.getString(Constant.COUSTMER_ID_ORA,null);
    }

    public void setCoustmerIdOra(String coustmerIdOra) {
        editor.putString(Constant.COUSTMER_ID_ORA,coustmerIdOra);
        editor.commit();
    }

    //STORE_NAME...........
    public String getShopName() {
        return pref.getString(Constant.STORE_NAME,null);
    }

    public void setShopName(String shopName) {
        editor.putString(Constant.STORE_NAME,shopName);
        editor.commit();
    }

    public void setWalletNo(String walletNo) {
        editor.putString(Constant.WALLET_NO,walletNo);
        editor.commit();
    }

    // POINT.......

    public String getPoint() {
        return pref.getString(Constant.POINT,null);
    }

    public void setPoint(String point) {
        editor.putString(Constant.POINT,point);
        editor.commit();
    }

    public String getImagePath() {
        return pref.getString(Constant.imagePath,null);
    }

    public void setImagePath(String city) {
        editor.putString(Constant.imagePath,city);
        editor.commit();
    }


    // Addresss details hold
    public String getCity() {
        return pref.getString(Constant.cityKey,null);
    }

    public void setCity(String city) {
        editor.putString(Constant.cityKey,city);
        editor.commit();
    }



    public String getState() {
        return pref.getString(Constant.stateKey,null);
    }

    public void setState(String stateKEy) {
       editor.putString(Constant.stateKey,stateKEy);
       editor.commit();
    }

    public String getDistrict() {
        return pref.getString(Constant.districtKey,null);
    }

    public void setDistrict(String countryCode) {
        editor.putString(Constant.districtKey,countryCode);
        editor.commit();
    }

    public String getZipCode() {
        return pref.getString(Constant.zipCodeKey,null);
    }

    public void setZipCode(String zipCode) {
        editor.putString(Constant.zipCodeKey,zipCode);
        editor.commit();
    }

    public String getAddress() {
        return pref.getString(Constant.ADDRESS_KEY,null);
    }

    public void setAddress(String address) {
        editor.putString(Constant.ADDRESS_KEY,address);
        editor.commit();
    }


    public String getkecamatan() {
        return pref.getString(Constant.kecamatanKey,null);
    }

    public void setkecamatan(String kecamatan) {
        editor.putString(Constant.kecamatanKey,kecamatan);
        editor.commit();
    }


    public String getNomor() {
        return pref.getString(Constant.NOMOR_KEY,null);
    }

    public void setNomor(String nomor) {
        editor.putString(Constant.NOMOR_KEY,nomor);
        editor.commit();
    }

    //NomorHP
    public String getNomorHP() {
        return pref.getString(Constant.NomorHP, null);
    }

    public void setNomorHP(String address) {
        editor.putString(Constant.NomorHP, address);
        editor.commit();
    }

    //BANK DETAILS
    public String getBankName() {
        return pref.getString(Constant.bankNameKey, null);
    }

    public void setBankName(String bankName) {
        editor.putString(Constant.bankNameKey, bankName);
        editor.commit();
    }

    public String getBankAccountNumber() {
        return pref.getString(Constant.accountNumberKey, null);
    }

    public void setBankAccountNumber(String accountNumber) {
        editor.putString(Constant.accountNumberKey, accountNumber);
        editor.commit();
    }

    public String getBankOwnerName() {
        return pref.getString(Constant.BANK_OWNER_NAME_KEY, null);
    }

    public void setBankOwnerName(String name) {
        editor.putString(Constant.BANK_OWNER_NAME_KEY, name);
        editor.commit();
    }


}
