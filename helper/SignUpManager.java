package id.meteor.alfamind.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by bodacious on 16/12/17.
 */

public class SignUpManager {

    private Context _context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MyPreferencesSign";
    private Calendar calendar = Calendar.getInstance();

    public SignUpManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void clear() {
        Log.d("CLEARDATA","clear");
        editor.clear();
        editor.apply();
    }

    // Status.............................

    public boolean getIsFirstTime() {
        return pref.getBoolean(Constant.FIRST_TIME, true);
    }

    public void setIsFirstTime(boolean first) {
        editor.putBoolean(Constant.FIRST_TIME, first);
        editor.commit();
    }

    public boolean getIsLogged() {
        return pref.getBoolean(Constant.IS_LOGGED, false);
    }

    public void setIsLogged(boolean isLogged) {
        editor.putBoolean(Constant.IS_LOGGED, isLogged);
        editor.commit();
    }


    public String getRememberPass() {
        return pref.getString(Constant.REMBER_PASSWORD, null);
    }

    public void setRememberPass(String pass) {
        editor.putString(Constant.REMBER_PASSWORD, pass);
        editor.commit();
    }

    public String getRememberEmail() {
        return pref.getString(Constant.REMEMBER_EMAIL, null);
    }

    public void setRememberEmail(String remEmail) {
        editor.putString(Constant.REMEMBER_EMAIL, remEmail);
        editor.commit();
    }

    public String getSignUpData() {
        return pref.getString(Constant.SIGNUP_DATA, "BLANK");
    }

    public void setSignUpData(String data) {
        editor.putString(Constant.SIGNUP_DATA, data);
        editor.commit();
    }

    public String getAccessToken() {
        return pref.getString(Constant.USER_ACCESS_TOKEN, null);
    }

    public void setAccessToken(String image) {
        editor.putString(Constant.USER_ACCESS_TOKEN, image);
        editor.commit();

    }


    public String getImage() {
        return pref.getString(Constant.imageKey, null);
    }

    public void setImage(String image) {
        editor.putString(Constant.imageKey, image);
        editor.commit();

    }

    // hold user details................................

    public String getEmail() {
        return pref.getString(Constant.email_key, null);
    }

    public void setEmail(String email) {
        editor.putString(Constant.email_key, email);
        editor.commit();
    }

    public int getCartID() {
        return pref.getInt(Constant.CART_KEY, 0);
    }

    public void setCartId(int id) {
        editor.putInt(Constant.CART_KEY, id);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(Constant.password_key, null);
    }

    public void setPassword(String password) {
        editor.putString(Constant.password_key, password);
        editor.commit();
    }

    public String getFirstName() {
        return pref.getString(Constant.firstNameKey, null);
    }

    public void setFirstName(String firstName) {
        editor.putString(Constant.firstNameKey, firstName);
        editor.commit();
    }

    public String getLastName() {
        return pref.getString(Constant.lastNameKey, null);
    }

    public void setLastName(String lastName) {
        editor.putString(Constant.lastNameKey, lastName);
        editor.commit();
    }

    public String getGender() {
        return pref.getString(Constant.genderKey, null);
    }

    public void setGender(String gender) {
        editor.putString(Constant.genderKey, gender);
        editor.commit();
    }

    public String getBirthDate() {
        return pref.getString(Constant.birthDateKey, calendar.get(Calendar.DAY_OF_MONTH)+"");
    }

    public void setBirthDate(String birthDate) {
        editor.putString(Constant.birthDateKey, birthDate);
        editor.commit();
    }

    public String getBirthMonth() {
        return pref.getString(Constant.birthMonthKey, (calendar.get(Calendar.MONTH)+1)+"");
    }

    public void setBirthMonth(String birthMonth) {
        editor.putString(Constant.birthMonthKey, birthMonth);
        editor.commit();
    }

    public String getBirthYear() {
        return pref.getString(Constant.birthYearKey, calendar.get(Calendar.YEAR)+"");
    }

    public void setBirthYear(String birthYear) {
        editor.putString(Constant.birthYearKey, birthYear);
        editor.commit();
    }

    public String getPhoneNumber() {
        return pref.getString(Constant.phoneNumberKey, null);
    }

    public void setPhoneNumber(String phoneNumber) {
        editor.putString(Constant.phoneNumberKey, phoneNumber);
        editor.commit();
    }


    // Addresss details hold

    public String getCity() {
        return pref.getString(Constant.cityKey, null);
    }

    public void setCity(String city) {
        editor.putString(Constant.cityKey, city);
        editor.commit();
    }

    public String getState() {
        return pref.getString(Constant.stateKey, null);
    }

    public void setState(String stateKEy) {
        editor.putString(Constant.stateKey, stateKEy);
        editor.commit();
    }

    public String getCountry() {
        return pref.getString(Constant.countryKey, null);
    }

    public void setCountry(String countryKey) {
        editor.putString(Constant.countryKey, countryKey);
        editor.commit();
    }

    public String getDistrict() {
        return pref.getString(Constant.districtKey, null);
    }

    public void setDistrict(String countryCode) {
        editor.putString(Constant.districtKey, countryCode);
        editor.commit();
    }

    public String getCountryCode() {
        return pref.getString(Constant.countryCodeKey, null);
    }

    public void setCountryCode(String countryCode) {
        editor.putString(Constant.countryCodeKey, countryCode);
        editor.commit();
    }

    public String getZipCode() {
        return pref.getString(Constant.zipCodeKey, null);
    }

    public void setZipCode(String zipCode) {
        editor.putString(Constant.zipCodeKey, zipCode);
        editor.commit();
    }

    public String getAddress() {
        return pref.getString(Constant.ADDRESS_KEY, null);
    }

    public void setAddress(String address) {
        editor.putString(Constant.ADDRESS_KEY, address);
        editor.commit();
    }


    public String getNpwpImage() {
        return pref.getString(Constant.NPWP_IMAGE_KEY, null);
    }

    public void setNpwpImage(String nomor) {
        editor.putString(Constant.NPWP_IMAGE_KEY, nomor);
        editor.commit();
    }

    public String getKtpImage() {
        return pref.getString(Constant.KTP_IMAGE_KEY, null);
    }

    public void setKtpImage(String address) {
        editor.putString(Constant.KTP_IMAGE_KEY, address);
        editor.commit();
    }


    // hold Bank Details....................

    public String getBankName() {
        return pref.getString(Constant.bankNameKey, null);
    }

    public void setBankName(String bankName) {
        editor.putString(Constant.bankNameKey, bankName);
        editor.commit();
    }

    public String getAccountNumber() {
        return pref.getString(Constant.accountNumberKey, null);
    }

    public void setAccountNumber(String accountNumber) {
        editor.putString(Constant.accountNumberKey, accountNumber);
        editor.commit();
    }

    public String getBankCode() {
        return pref.getString(Constant.BANK_CODE,null);
    }

    public void setBankCode(String bankName) {
        editor.putString(Constant.BANK_CODE,bankName);
        editor.commit();
    }

    public String getNomor() {
        return pref.getString(Constant.NOMOR_KEY, null);
    }

    public void setNomor(String nomor) {
        editor.putString(Constant.NOMOR_KEY, nomor);
        editor.commit();
    }


    public String getBankOwnerName() {
        return pref.getString(Constant.BANK_OWNER_NAME_KEY, null);
    }

    public void setBankOwnerName(String bankOwnerName) {
        editor.putString(Constant.BANK_OWNER_NAME_KEY, bankOwnerName);
        editor.commit();
    }

    public String getkecamatan() {
        return pref.getString(Constant.kecamatanKey, null);
    }

    public void setkecamatan(String kecamatan) {
        editor.putString(Constant.kecamatanKey, kecamatan);
        editor.commit();
    }


    ////  extra value......................................//////////////////

    public int getPid1() {
        return pref.getInt(Constant.pID1, 0);
    }

    public void setPid1(int Pid1) {
        editor.putInt(Constant.pID1, Pid1);// provinci ID
        editor.commit();
    }


    public int getPid2() {
        return pref.getInt(Constant.pID2, 0);// dist ID
    }

    public void setPid2(int Pid2) {
        editor.putInt(Constant.pID2, Pid2);
        editor.commit();
    }

    public int getPid3() {
        return pref.getInt(Constant.pID3, 0);
    }

    public void setPid3(int Pid3) {
        editor.putInt(Constant.pID3, Pid3);
        editor.commit();
    }


    public String getPName1() {
        return pref.getString(Constant.pName1, null);
    }

    public void setPName1(String kecamatan) {
        editor.putString(Constant.pName1, kecamatan);
        editor.commit();
    }


    public String getPName2() {
        return pref.getString(Constant.pName2, null);
    }

    public void setPName2(String kecamatan) {
        editor.putString(Constant.pName2, kecamatan);
        editor.commit();
    }

    public String getPName3() {
        return pref.getString(Constant.pName3, null);
    }

    public void setPName3(String kecamatan) {
        editor.putString(Constant.pName3, kecamatan);
        editor.commit();
    }
}
