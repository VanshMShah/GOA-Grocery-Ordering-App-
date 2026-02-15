package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CART_REQUEST_CODE = 1;
    private final List<GroceryItem> groceryList = new ArrayList<>();
    private final List<GroceryItem> cartList = new ArrayList<>();
    private Customer customer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        customer = getIntent().getParcelableExtra("customer");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ListView listView = findViewById(R.id.listView);
        FloatingActionButton viewCartBtn = findViewById(R.id.btnViewCart);

        // Sample grocery items
        groceryList.add(new GroceryItem("Potato", 25, R.drawable.potato));
        groceryList.add(new GroceryItem("Onion", 35, R.drawable.onion));
        groceryList.add(new GroceryItem("Tomato", 40, R.drawable.tomato));
        groceryList.add(new GroceryItem("Cucumber", 30, R.drawable.cucumber));
        groceryList.add(new GroceryItem("Radish", 25, R.drawable.radish));
        groceryList.add(new GroceryItem("Carrot", 40, R.drawable.carrot));
        groceryList.add(new GroceryItem("Beetroot", 35, R.drawable.beetroot));
        groceryList.add(new GroceryItem("Cabbage", 30, R.drawable.cabbage));
        groceryList.add(new GroceryItem("Cauliflower", 45, R.drawable.cauliflower));
        groceryList.add(new GroceryItem("Capsicum (Green)", 60, R.drawable.capsicum));
        groceryList.add(new GroceryItem("Capsicum (Red)", 120, R.drawable.capsicum_red));
        groceryList.add(new GroceryItem("Capsicum (Red) and Capsicum (Yellow)", 240, R.drawable.capsicum_red_and_capsicum_yellow));
        groceryList.add(new GroceryItem("Bitter Gourd", 50, R.drawable.bittergourd));
        groceryList.add(new GroceryItem("Bottle Gourd", 35, R.drawable.bottlegourd));
        groceryList.add(new GroceryItem("Snake Gourd", 40, R.drawable.snakegourd));
        groceryList.add(new GroceryItem("Ridge Gourd", 45, R.drawable.ridgegourd));
        groceryList.add(new GroceryItem("Brinjal (Eggplant)", 40, R.drawable.brinjal));
        groceryList.add(new GroceryItem("Drumstick", 90, R.drawable.drumstick));
        groceryList.add(new GroceryItem("Okra (Ladies Finger)", 50, R.drawable.okra));
        groceryList.add(new GroceryItem("Spinach", 30, R.drawable.spinach));
        groceryList.add(new GroceryItem("Coriander (per bunch)", 15, R.drawable.coriander));
        groceryList.add(new GroceryItem("Mint (per bunch)", 20, R.drawable.mint));
        groceryList.add(new GroceryItem("Spring Onion", 40, R.drawable.springonion));
        groceryList.add(new GroceryItem("Green Peas", 100, R.drawable.greenpeas));
        groceryList.add(new GroceryItem("Sweet Corn", 60, R.drawable.sweetcorn));
        groceryList.add(new GroceryItem("Broccoli", 120, R.drawable.broccoli));
        groceryList.add(new GroceryItem("Zucchini (Green)", 130, R.drawable.zucchini));
        groceryList.add(new GroceryItem("Zucchini (Yellow)", 140, R.drawable.zucchini_yellow));
        groceryList.add(new GroceryItem("Mushroom", 160, R.drawable.mushroom));
        groceryList.add(new GroceryItem("Pumpkin", 30, R.drawable.pumpkin));
        groceryList.add(new GroceryItem("Turnip", 40, R.drawable.turnip));
        groceryList.add(new GroceryItem("Sweet Potato", 50, R.drawable.sweetpotato));
        groceryList.add(new GroceryItem("Fenugreek (Methi)", 25, R.drawable.fenugreek));
        groceryList.add(new GroceryItem("Curry Leaves (per bunch)", 10, R.drawable.curryleaves));
        groceryList.add(new GroceryItem("Green Chilli", 60, R.drawable.greenchilli));
        groceryList.add(new GroceryItem("Red Chilli (Dry)", 180, R.drawable.redchilli));
        groceryList.add(new GroceryItem("Garlic", 200, R.drawable.garlic));
        groceryList.add(new GroceryItem("Ginger", 150, R.drawable.ginger));
        groceryList.add(new GroceryItem("Lemon (per piece)", 5, R.drawable.lemon));
        groceryList.add(new GroceryItem("Celery", 120, R.drawable.celery));

        GroceryAdapter adapter = new GroceryAdapter();
        listView.setAdapter(adapter);

        viewCartBtn.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(MainActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cartList", new ArrayList<>(cartList));
            intent.putExtra("customer", customer);
            startActivityForResult(intent, CART_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_past_orders) {
            Intent intent = new Intent(MainActivity.this, PastOrdersActivity.class);
            intent.putExtra("customer", customer);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_manage_addresses) {
            Intent intent = new Intent(MainActivity.this, AddressManagementActivity.class);
            intent.putExtra("customer", customer);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CART_REQUEST_CODE && resultCode == RESULT_OK) {
            cartList.clear();
            Toast.makeText(this, "New order started", Toast.LENGTH_SHORT).show();
        }
    }

    class GroceryAdapter extends BaseAdapter {
        @Override
        public int getCount() { return groceryList.size(); }
        @Override
        public Object getItem(int position) { return groceryList.get(position); }
        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null)
                row = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, parent, false);

            GroceryItem item = groceryList.get(position);

            ImageView img = row.findViewById(R.id.imgItem);
            TextView name = row.findViewById(R.id.txtItemName);
            TextView price = row.findViewById(R.id.txtPrice);
            EditText qty = row.findViewById(R.id.editQty);
            Button addBtn = row.findViewById(R.id.btnAdd);
            TextView plusBtn = row.findViewById(R.id.btnPlus);
            TextView minusBtn = row.findViewById(R.id.btnMinus);

            img.setImageResource(item.getImageResId());
            name.setText(item.getName());
            price.setText(getString(R.string.price_format, item.getPrice()));
            qty.setText("");

            plusBtn.setOnClickListener(v -> {
                String currentQty = qty.getText().toString();
                int q = 0;
                if (!currentQty.isEmpty()) {
                    q = Integer.parseInt(currentQty);
                }
                q++;
                qty.setText(String.valueOf(q));
            });

            minusBtn.setOnClickListener(v -> {
                String currentQty = qty.getText().toString();
                if (!currentQty.isEmpty()) {
                    int q = Integer.parseInt(currentQty);
                    if (q > 1) {
                        q--;
                        qty.setText(String.valueOf(q));
                    } else if (q == 1) {
                        qty.setText("");
                    }
                }
            });

            addBtn.setOnClickListener(v -> {
                try {
                    int q = Integer.parseInt(qty.getText().toString());
                    if (q > 0) {
                        item.setQuantity(q);
                        if (!cartList.contains(item)) {
                            cartList.add(item);
                        }
                        Toast.makeText(MainActivity.this, item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Enter quantity > 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Enter quantity", Toast.LENGTH_SHORT).show();
                }
            });
            return row;
        }
    }
}