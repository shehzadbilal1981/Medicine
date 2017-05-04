package com.example.shehzadbilal.medicine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by shehzadbilal on 04/04/2017.
 */

public class MedicineDataSource {

    // Database fields
    private SQLiteDatabase database;
    private CustomSQLiteHelper dbHelper;
    private String[] allColumns = { CustomSQLiteHelper.COLUMN_ID,
            CustomSQLiteHelper.COLUMN_MEDICINE_NAME,CustomSQLiteHelper.COLUMN_MEDICINE_IMAGE,
    CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY, CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY_TYPE,
    CustomSQLiteHelper.COLUMN_MEDICINE_IMAGE_PATH};

    public MedicineDataSource(Context context) {
        dbHelper = new CustomSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean addMedicine(MedicineModel medicine) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_NAME, medicine.name);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_IMAGE, R.drawable.icon);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY, medicine.quantity);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY_TYPE, medicine.quantityType);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_IMAGE_PATH, medicine.imagePath);

        long insertId = database.insert(CustomSQLiteHelper.TABLE_MEDICINE, null,
                values);
        return (insertId > 0) ? true : false;
    }

    public boolean updateMedicine(MedicineModel medicine) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_ID, medicine.medId);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_NAME, medicine.name);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY, medicine.quantity);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY_TYPE, medicine.quantityType);
        values.put(CustomSQLiteHelper.COLUMN_MEDICINE_IMAGE_PATH, medicine.imagePath);
        int rows = database.update(CustomSQLiteHelper.TABLE_MEDICINE,values, CustomSQLiteHelper.COLUMN_ID + " =  " + medicine.medId,null);
        if(rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int deleteMedicine(MedicineModel medicine) {
        long id = medicine.medId;
        System.out.println("Medicine deleted with id: " + id);
        return  database.delete(CustomSQLiteHelper.TABLE_MEDICINE, CustomSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<MedicineModel> getAllMedicines() {
        ArrayList<MedicineModel> medicines = new ArrayList<MedicineModel>();

        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_MEDICINE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicineModel medicine = cursorToMedicine(cursor);
            medicines.add(medicine);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return medicines;
    }

    private MedicineModel cursorToMedicine(Cursor cursor) {
        MedicineModel medicine = new MedicineModel();
        medicine.medId = cursor.getInt(0);
        medicine.name =cursor.getString(1);
        medicine.imageResourceID = cursor.getInt(2);
        medicine.quantity = cursor.getInt(3);
        medicine.quantityType = cursor.getString(4);
        medicine.imagePath = cursor.getString(5);
        return medicine;
    }
}
