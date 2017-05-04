package com.example.shehzadbilal.medicine;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shehzadbilal on 29/03/2017.
 */

public class MedicineAdapter extends ArrayAdapter<MedicineModel> {
    private Context ctx;
    public MedicineAdapter(Context context, ArrayList<MedicineModel> medicines) {
        super(context, 0, medicines);
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MedicineModel medicine = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_cell, parent, false);
        }
        // Lookup view for data population
        TextView medicine_name = (TextView) convertView.findViewById(R.id.medicine_name);
        ImageView medicine_image = (ImageView) convertView.findViewById(R.id.medicine_image);

        medicine_name.setText(medicine.GetFullName());
        if(medicine.imagePath != null) {
            Bitmap bitmap = new ImageSaver(ctx).
                    setFileName(medicine.imagePath).
                    setDirectoryName("images").
                    load();
            medicine_image.setImageBitmap(bitmap);
        } else {
            medicine_image.setImageResource(medicine.imageResourceID);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}