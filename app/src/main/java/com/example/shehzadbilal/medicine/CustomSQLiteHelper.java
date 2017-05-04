package com.example.shehzadbilal.medicine;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shehzadbilal on 04/04/2017.
 */

public class CustomSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_MEDICINE = "medicine";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MEDICINE_NAME = "medicine_name";
    public static final String COLUMN_MEDICINE_IMAGE = "medicine_image";
    public static final String COLUMN_MEDICINE_QUANTITY = "medicine_quantity";
    public static final String COLUMN_MEDICINE_QUANTITY_TYPE = "medicine_quantity_type";
    public static final String COLUMN_MEDICINE_IMAGE_PATH = "medicine_image_path";

    private static final String DATABASE_NAME = "medicines.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MEDICINE + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_MEDICINE_NAME
            + " text not null," + COLUMN_MEDICINE_IMAGE  + " integer," + COLUMN_MEDICINE_QUANTITY  +
            " integer, " + COLUMN_MEDICINE_QUANTITY_TYPE + " text not null, " +
            COLUMN_MEDICINE_IMAGE_PATH + " text null);";

    public CustomSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        ArrayList<MedicineModel> listOfMedicines = new ArrayList<MedicineModel>();
        listOfMedicines.add(new MedicineModel("Gvia 50",R.drawable.gvia,1,"Box"));
        listOfMedicines.add(new MedicineModel("Losar K",R.drawable.losark,1,"Box"));
        listOfMedicines.add(new MedicineModel("E P Wis",R.drawable.icon,1,"Box"));
        listOfMedicines.add(new MedicineModel("Fefol Vit",R.drawable.fefol,1,"Strip"));
        listOfMedicines.add(new MedicineModel("Visa Vit",R.drawable.visavit,1,"Box"));
        listOfMedicines.add(new MedicineModel("Amryl 2",R.drawable.amaryl,1,"Box"));
        listOfMedicines.add(new MedicineModel("Neuromet",R.drawable.neuromet,2,"Strip"));
        listOfMedicines.add(new MedicineModel("Inosita 50",R.drawable.inosita,1,"Box"));
        listOfMedicines.add(new MedicineModel("Glucovence 500",R.drawable.glucovence,1,"Box"));
        listOfMedicines.add(new MedicineModel("Norvesce 5",R.drawable.norvesce,1,"Strip"));
        listOfMedicines.add(new MedicineModel("Ascard 75",R.drawable.ascard,1,"Strip"));
        listOfMedicines.add(new MedicineModel("Novolux",R.drawable.icon,2,"Strip"));
        listOfMedicines.add(new MedicineModel("Nise",R.drawable.nise,1,"Box"));

        for(MedicineModel m : listOfMedicines) {
            ContentValues values = new ContentValues();
            values.put(CustomSQLiteHelper.COLUMN_MEDICINE_NAME, m.name);
            values.put(CustomSQLiteHelper.COLUMN_MEDICINE_IMAGE, m.imageResourceID);
            values.put(CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY, m.quantity);
            values.put(CustomSQLiteHelper.COLUMN_MEDICINE_QUANTITY_TYPE, m.quantityType);
            database.insert(CustomSQLiteHelper.TABLE_MEDICINE, null,
                    values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CustomSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        onCreate(db);
    }
}
