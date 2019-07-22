package id.meteor.alfamind.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bodacious on 28/12/17.
 */

public class TableAddress {

    public static void onCreate(SQLiteDatabase db){
        Log.e("DATABASE","ONCREATE");
        String query = "create table Address("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + "name text,"
                + "number text,"
                + "address text,"
                + "rt text,"
                + "rw text,"
                + "provinsi text,"
                + "kabupaten text,"
                + "kecamatan text,"
                + "kode text"
                + ")";
        db.execSQL(query);
    }

    public static void saveAddress(String name,String number, String address, String rt,String rw,String provinsi,String kabupaten,String kecamatan,String kode,SQLiteDatabase db){
        Log.e("DATABASE","SAVE");
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("number",number);
        values.put("address",address);
        values.put("rt",rt);
        values.put("rw",rw);
        values.put("provinsi",provinsi);
        values.put("kabupaten",kabupaten);
        values.put("kecamatan",kecamatan);
        values.put("kode",kode);
        db.insert("Address",null,values);
    }

    public static void deleteAddress(String id,SQLiteDatabase db){
        Log.e("DATABASE","DELETE "+id);
        String str = "DELETE FROM Address WHERE id ='"+id+"'";
        db.execSQL(str);
    }

    public static ArrayList<Address> getAllAddress(SQLiteDatabase db){
        Log.e("DATABASE","GETALLADDRESS");
        ArrayList<Address> list = new ArrayList<>();
        String str = "SELECT * FROM Address ";
        Cursor cursor = db.rawQuery(str, null);
        if(cursor!=null&&cursor.moveToFirst()){

            do {
                Address address =new Address();
                address.setId(cursor.getString(cursor.getColumnIndex("id")));
                address.setName(cursor.getString(cursor.getColumnIndex("name")));
                address.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                address.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                address.setRt(cursor.getString(cursor.getColumnIndex("rt")));
                address.setRw(cursor.getString(cursor.getColumnIndex("rw")));
                address.setProvinsi(cursor.getString(cursor.getColumnIndex("provinsi")));
                address.setKabupaten(cursor.getString(cursor.getColumnIndex("kabupaten")));
                address.setKecamatan(cursor.getString(cursor.getColumnIndex("kecamatan")));
                address.setKode(cursor.getString(cursor.getColumnIndex("kode")));
                list.add(address);
            }while (cursor.moveToNext());
            return list;
        }
        cursor.close();

        return null;
    }

    public static Address getAddress(String id,SQLiteDatabase db ){
        String str = "SELECT * FROM Address WHERE id ='"+id+"'";
        Cursor cursor = db.rawQuery(str, null);
        if(cursor!=null&&cursor.moveToFirst()){
            Address address  = new Address();
            address.setId(cursor.getString(cursor.getColumnIndex("id")));
            address.setName(cursor.getString(cursor.getColumnIndex("name")));
            address.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            address.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            address.setRt(cursor.getString(cursor.getColumnIndex("rt")));
            address.setRw(cursor.getString(cursor.getColumnIndex("rw")));
            address.setProvinsi(cursor.getString(cursor.getColumnIndex("provinsi")));
            address.setKabupaten(cursor.getString(cursor.getColumnIndex("kabupaten")));
            address.setKecamatan(cursor.getString(cursor.getColumnIndex("kecamatan")));
            address.setKode(cursor.getString(cursor.getColumnIndex("kode")));
            return address;
        }else {
            cursor.close();
            return  null;
        }
    }

    public static void updateAddress(String id,String name,String number, String address, String rt,String rw,String provinsi,String kabupaten,String kecamatan,String kode,SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("number",number);
        values.put("address",address);
        values.put("rt",rt);
        values.put("rw",rw);
        values.put("provinsi",provinsi);
        values.put("kabupaten",kabupaten);
        values.put("kecamatan",kecamatan);
        values.put("kode",kode);
        db.update("Address",values,"id="+id,null);
    }
}