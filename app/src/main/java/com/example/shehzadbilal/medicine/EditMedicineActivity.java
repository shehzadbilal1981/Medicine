package com.example.shehzadbilal.medicine;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class EditMedicineActivity extends AppCompatActivity implements IPickResult {
    MedicineModel medicine;
    private static int RESULT_LOAD_IMG = 1;
    boolean imgChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        SetQuantitySpinner();
        SetQuantityTypeSpinner();

        //TODO fill parameters
        if(getIntent().hasExtra("medicine")) {
            medicine = (MedicineModel) getIntent().getExtras().getSerializable("medicine");
            if (medicine != null) {
                EditText medicine_name = (EditText) findViewById(R.id.medicine_name);
                medicine_name.setText(medicine.name);

                Spinner qty_spinner = (Spinner) findViewById(R.id.medicine_quantity);
                qty_spinner.setSelection(medicine.quantity - 1);

                Spinner qty_type_spinner = (Spinner) findViewById(R.id.medicine_quantity_type);
                if (medicine.quantityType.compareToIgnoreCase("Strip") == 0) {
                    qty_type_spinner.setSelection(1);
                }
            }
        }
    }

    void SetQuantitySpinner() {
        //Quantity Spinner
        Spinner spinner = (Spinner) findViewById(R.id.medicine_quantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void SetQuantityTypeSpinner() {
        //Quantity Type Spinner
        Spinner spinner = (Spinner) findViewById(R.id.medicine_quantity_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quantity_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void loadImage(View view) {
        PickImageDialog.build(new PickSetup()).show(this);
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            ImageView imgView = (ImageView) findViewById(R.id.imgView);
            //If you want the Bitmap.
            imgView.setImageBitmap(r.getBitmap());
            imgChanged = true;
            //Image path
            //r.getPath();
            /*
            Long tsLong = System.currentTimeMillis()/1000;
            new ImageSaver(this).
                    setFileName(tsLong.toString() + ".png").
                    setDirectoryName("images").
                    save(r.getBitmap());

            Bitmap bitmap = new ImageSaver(this).
                    setFileName(tsLong.toString() + ".png").
                    setDirectoryName("images").
                    load();
            */

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void SaveClicked(View v) {
        MedicineDataSource dSource = new MedicineDataSource(this);
        dSource.open();
        if(medicine == null) {
            medicine = new MedicineModel();
        }
        EditText medicine_name = (EditText) findViewById(R.id.medicine_name);
        Spinner qty_spinner = (Spinner) findViewById(R.id.medicine_quantity);
        Spinner qty_type_spinner = (Spinner) findViewById(R.id.medicine_quantity_type);
        medicine.name = medicine_name.getText().toString();
        medicine.quantity = qty_spinner.getSelectedItemPosition() + 1;
        medicine.quantityType = qty_type_spinner.getSelectedItem().toString();
        if(imgChanged) {
            Long tsLong = System.currentTimeMillis()/1000;
            String imgPath = tsLong.toString() + ".png";
            ImageView imgView = (ImageView) findViewById(R.id.imgView);
            new ImageSaver(this).
                    setFileName(imgPath).
                    setDirectoryName("images").
                    save(((BitmapDrawable)imgView.getDrawable()).getBitmap());
            medicine.imagePath = imgPath;
        }
        boolean result;
        if(medicine.medId > 0) {
            result = dSource.updateMedicine(medicine);
        } else {
            result = dSource.addMedicine(medicine);
        }

        if(result) {
            Intent intent=new Intent();
            setResult(1,intent);
            finish();
        }
    }
}