package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class AddressSetupActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Customer customer;
    private boolean isFirstTime;
    private int addressCount = 0;

    private EditText etHouseNo, etStreet, etArea, etCity, etState, etPincode, etLandmark;
    private Spinner spinnerLabel;
    private CheckBox cbDefault;
    private Button btnSaveAddress, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_setup);

        dbHelper = new DatabaseHelper(this);
        customer = getIntent().getParcelableExtra("customer");
        isFirstTime = getIntent().getBooleanExtra("isFirstTime", false);

        etHouseNo = findViewById(R.id.etHouseNo);
        etStreet = findViewById(R.id.etStreet);
        etArea = findViewById(R.id.etArea);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etPincode = findViewById(R.id.etPincode);
        etLandmark = findViewById(R.id.etLandmark);
        spinnerLabel = findViewById(R.id.spinnerLabel);
        cbDefault = findViewById(R.id.cbDefault);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnSkip = findViewById(R.id.btnSkip);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.address_labels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLabel.setAdapter(adapter);

        // Set Mumbai as default
        etCity.setText("Mumbai");
        etState.setText("Maharashtra");

        // First address should be default
        if (addressCount == 0) {
            cbDefault.setChecked(true);
        }

        btnSaveAddress.setOnClickListener(v -> saveAddress());

        btnSkip.setOnClickListener(v -> {
            if (addressCount < 1) {
                Toast.makeText(this, "Please add at least one address", Toast.LENGTH_SHORT).show();
            } else {
                goToMainActivity();
            }
        });
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

        Address address = new Address(label, houseNo, street, area, city, state, pincode, landmark, isDefault);
        long result = dbHelper.addAddress(customer.getCustomerId(), address);

        if (result > 0) {
            addressCount++;
            Toast.makeText(this, "Address saved successfully!", Toast.LENGTH_SHORT).show();

            if (addressCount >= 2) {
                btnSkip.setText("Continue to App");
            }

            // Clear form for next address
            clearForm();

            if (addressCount == 1) {
                Toast.makeText(this, "Please add one more address", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etHouseNo.setText("");
        etStreet.setText("");
        etArea.setText("");
        etPincode.setText("");
        etLandmark.setText("");
        cbDefault.setChecked(false);
        spinnerLabel.setSelection(0);
        etCity.setText("Mumbai");
        etState.setText("Maharashtra");
    }

    private void goToMainActivity() {
        Intent intent = new Intent(AddressSetupActivity.this, MainActivity.class);
        intent.putExtra("customer", customer);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (addressCount >= 1) {
            goToMainActivity();
        } else {
            Toast.makeText(this, "Please add at least one address", Toast.LENGTH_SHORT).show();
        }
    }
}