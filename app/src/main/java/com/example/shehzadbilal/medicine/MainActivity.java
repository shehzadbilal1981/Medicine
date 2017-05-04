package com.example.shehzadbilal.medicine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_CODE = 1;
    boolean freeze = false;
    ArrayList<MedicineModel> listOfMedicines;
    ArrayList<MedicineModel> selectedItems;

    MedicineAdapter adapter;
    MenuItem freezeItem;
    Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _context = this;

        MedicineDataSource dSource = new MedicineDataSource(this);
        dSource.open();
        listOfMedicines = dSource.getAllMedicines();

        selectedItems = new ArrayList<MedicineModel>(listOfMedicines);
        adapter = new MedicineAdapter(this, selectedItems);
        ListView listView = (ListView) findViewById(R.id.MedicineList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!freeze) {
                    selectedItems.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                if(!freeze) {
                    final CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("Please Select");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0) {
                                //edit
                                Intent intent = new Intent(_context, EditMedicineActivity.class);
                                intent.putExtra("medicine",selectedItems.get(pos));
                                MainActivity.this.startActivityForResult(intent,RESULT_CODE);
                            } else if(which == 1) {
                                //delete
                                MedicineDataSource dSource = new MedicineDataSource(MainActivity.this);
                                dSource.open();
                                if(dSource.deleteMedicine(selectedItems.get(pos)) >0 ) {
                                    UpdateLayout();
                                }
                            }

                        }
                    });
                    builder.show();
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CODE) {
            UpdateLayout();
        }
    }

    public void UpdateLayout() {
        MedicineDataSource dSource = new MedicineDataSource(this);
        dSource.open();
        listOfMedicines = dSource.getAllMedicines();
        selectedItems.clear();
        selectedItems.addAll(listOfMedicines);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.freeze_button) {
            if(freeze == true) {
                freezeItem = item;
                showPromptDialog();
            } else {
                freeze = true;
                item.setTitle(R.string.action_unfreeze);
            }
        } else if (id == R.id.showall_button && freeze == false) {
            selectedItems.clear();
            selectedItems.addAll(listOfMedicines);
            adapter.notifyDataSetChanged();
        } else if(id == R.id.add_button) {
            Intent intent = new Intent(_context, EditMedicineActivity.class);
            this.startActivityForResult(intent,RESULT_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    public  void showPromptDialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String m_Text = userInput.getText().toString();

                                if(m_Text.compareTo("shehzad") == 0) {
                                    freeze = !freeze;
                                    freezeItem.setTitle(R.string.action_freeze);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
