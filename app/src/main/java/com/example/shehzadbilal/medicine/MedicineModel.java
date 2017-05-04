package com.example.shehzadbilal.medicine;

import java.io.Serializable;

/**
 * Created by shehzadbilal on 29/03/2017.
 */

public class MedicineModel implements Serializable {
    public int medId;
    public String name;
    public int imageResourceID;
    public  int quantity;
    public  String quantityType;
    public  String imagePath;

    public  String GetFullName() {
        return name + " (" + quantity + " " + quantityType + ")";
    }

    public  MedicineModel() {

    }

    public MedicineModel(String name, int imgResourceId, int qty,  String qtyType) {
        this.name=name;
        this.imageResourceID = imgResourceId;
        this.quantity = qty;
        this.quantityType = qtyType;
    }
}
