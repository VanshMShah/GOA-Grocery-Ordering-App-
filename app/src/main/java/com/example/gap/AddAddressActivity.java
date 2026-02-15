package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddAddressActivity extends AppCompatActivity {

    private static final int MAP_REQUEST_CODE = 200;
    private DatabaseHelper dbHelper;
    private Customer customer;
    private EditText etHouseNo, etStreet, etArea, etCity, etState, etPincode, etLandmark;
    private Spinner spinnerLabel;
    private CheckBox cbDefault;
    private double selectedLatitude = 0;
    private double selectedLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        dbHelper = new DatabaseHelper(this);
        customer = getIntent().getParcelableExtra("customer");

        Toolbar toolbar = findViewById(R.id.toolbarAddAddress);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        etHouseNo = findViewById(R.id.etHouseNo);
        etStreet = findViewById(R.id.etStreet);
        etArea = findViewById(R.id.etArea);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etPincode = findViewById(R.id.etPincode);
        etLandmark = findViewById(R.id.etLandmark);
        spinnerLabel = findViewById(R.id.spinnerLabel);
        cbDefault = findViewById(R.id.cbDefault);
        Button btnSave = findViewById(R.id.btnSaveAddress);
        Button btnSelectLocation = findViewById(R.id.btnSelectLocation);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.address_labels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLabel.setAdapter(adapter);

        etCity.setText("Mumbai");
        etState.setText("Maharashtra");

        btnSelectLocation.setOnClickListener(v -> {
            Intent intent = new Intent(AddAddressActivity.this, MapLocationActivity.class);
            startActivityForResult(intent, MAP_REQUEST_CODE);
        });

        btnSave.setOnClickListener(v -> saveAddress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedLatitude = data.getDoubleExtra("latitude", 0);
            selectedLongitude = data.getDoubleExtra("longitude", 0);
            Toast.makeText(this, "Location selected from map", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAddress() {
        String houseNo = etHouseNo.getText().toString().trim();
        String street = etStreet.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String pincode = etPincode.getText().toString().trim();
        String landmark = etLandmark.getText().toString().trim();
        String label = spinnerLabel.getSelectedItem().toString();
        boolean isDefault = cbDefault.isChecked();

        if (houseNo.isEmpty() || area.isEmpty() || city.isEmpty() || state.isEmpty() || pincode.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pincode.length() != 6) {
            Toast.makeText(this, "Please enter valid 6-digit pincode", Toast.LENGTH_SHORT).show();
            return;
        }

        Address address = new Address(label, houseNo, street, area, city, state, pincode,
                landmark, isDefault, selectedLatitude, selectedLongitude);
        long result = dbHelper.addAddress(customer.getCustomerId(), address);

        if (result > 0) {
            Toast.makeText(this, "Address saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}