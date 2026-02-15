package com.example.gap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GroceryApp.db";
    private static final int DATABASE_VERSION = 2; // Increased version

    // Customer Table
    private static final String TABLE_CUSTOMER = "customers";
    private static final String COL_CUSTOMER_ID = "customer_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";

    // Address Table
    private static final String TABLE_ADDRESS = "addresses";
    private static final String COL_ADDRESS_ID = "address_id";
    private static final String COL_ADDRESS_CUSTOMER_ID = "customer_id";
    private static final String COL_ADDRESS_LABEL = "address_label";
    private static final String COL_HOUSE_NO = "house_no";
    private static final String COL_STREET = "street";
    private static final String COL_AREA = "area";
    private static final String COL_CITY = "city";
    private static final String COL_STATE = "state";
    private static final String COL_PINCODE = "pincode";
    private static final String COL_LANDMARK = "landmark";
    private static final String COL_IS_DEFAULT = "is_default";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";

    // Order Table
    private static final String TABLE_ORDER = "orders";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_ORDER_CUSTOMER_ID = "customer_id";
    private static final String COL_ORDER_DATE = "order_date";
    private static final String COL_ORDER_TOTAL = "order_total";
    private static final String COL_ORDER_ADDRESS = "delivery_address";
    private static final String COL_PAYMENT_METHOD = "payment_method";

    // Order Items Table
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String COL_ORDER_ITEM_ID = "order_item_id";
    private static final String COL_ORDER_ITEM_ORDER_ID = "order_id";
    private static final String COL_ITEM_NAME = "item_name";
    private static final String COL_ITEM_PRICE = "item_price";
    private static final String COL_ITEM_QUANTITY = "item_quantity";
    private static final String COL_ITEM_IMAGE_RES_ID = "item_image_res_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Customer Table
        String createCustomerTable = "CREATE TABLE " + TABLE_CUSTOMER + " (" +
                COL_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_FULL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_EMAIL + " TEXT)";
        db.execSQL(createCustomerTable);

        // Create Address Table with location
        String createAddressTable = "CREATE TABLE " + TABLE_ADDRESS + " (" +
                COL_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ADDRESS_CUSTOMER_ID + " INTEGER NOT NULL, " +
                COL_ADDRESS_LABEL + " TEXT NOT NULL, " +
                COL_HOUSE_NO + " TEXT NOT NULL, " +
                COL_STREET + " TEXT, " +
                COL_AREA + " TEXT NOT NULL, " +
                COL_CITY + " TEXT NOT NULL, " +
                COL_STATE + " TEXT NOT NULL, " +
                COL_PINCODE + " TEXT NOT NULL, " +
                COL_LANDMARK + " TEXT, " +
                COL_LATITUDE + " REAL, " +
                COL_LONGITUDE + " REAL, " +
                COL_IS_DEFAULT + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_ADDRESS_CUSTOMER_ID + ") REFERENCES " +
                TABLE_CUSTOMER + "(" + COL_CUSTOMER_ID + "))";
        db.execSQL(createAddressTable);

        // Create Order Table
        String createOrderTable = "CREATE TABLE " + TABLE_ORDER + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_CUSTOMER_ID + " INTEGER NOT NULL, " +
                COL_ORDER_DATE + " TEXT NOT NULL, " +
                COL_ORDER_TOTAL + " REAL NOT NULL, " +
                COL_ORDER_ADDRESS + " TEXT NOT NULL, " +
                COL_PAYMENT_METHOD + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_ORDER_CUSTOMER_ID + ") REFERENCES " +
                TABLE_CUSTOMER + "(" + COL_CUSTOMER_ID + "))";
        db.execSQL(createOrderTable);

        // Create Order Items Table
        String createOrderItemsTable = "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ITEM_ORDER_ID + " INTEGER NOT NULL, " +
                COL_ITEM_NAME + " TEXT NOT NULL, " +
                COL_ITEM_PRICE + " REAL NOT NULL, " +
                COL_ITEM_QUANTITY + " INTEGER NOT NULL, " +
                COL_ITEM_IMAGE_RES_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_ORDER_ITEM_ORDER_ID + ") REFERENCES " +
                TABLE_ORDER + "(" + COL_ORDER_ID + "))";
        db.execSQL(createOrderItemsTable);

        // Insert default user
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, "user");
        cv.put(COL_PASSWORD, "password");
        cv.put(COL_FULL_NAME, "Test User");
        cv.put(COL_PHONE, "9876543210");
        cv.put(COL_EMAIL, "user@test.com");
        long customerId = db.insert(TABLE_CUSTOMER, null, cv);

        // Insert default addresses
        ContentValues addr1 = new ContentValues();
        addr1.put(COL_ADDRESS_CUSTOMER_ID, customerId);
        addr1.put(COL_ADDRESS_LABEL, "Home");
        addr1.put(COL_HOUSE_NO, "123");
        addr1.put(COL_STREET, "Main Street");
        addr1.put(COL_AREA, "Bandra West");
        addr1.put(COL_CITY, "Mumbai");
        addr1.put(COL_STATE, "Maharashtra");
        addr1.put(COL_PINCODE, "400050");
        addr1.put(COL_LANDMARK, "Near Station");
        addr1.put(COL_IS_DEFAULT, 1);
        db.insert(TABLE_ADDRESS, null, addr1);

        ContentValues addr2 = new ContentValues();
        addr2.put(COL_ADDRESS_CUSTOMER_ID, customerId);
        addr2.put(COL_ADDRESS_LABEL, "Office");
        addr2.put(COL_HOUSE_NO, "456");
        addr2.put(COL_STREET, "Business Park");
        addr2.put(COL_AREA, "Andheri East");
        addr2.put(COL_CITY, "Mumbai");
        addr2.put(COL_STATE, "Maharashtra");
        addr2.put(COL_PINCODE, "400069");
        addr2.put(COL_LANDMARK, "Near Metro");
        addr2.put(COL_IS_DEFAULT, 0);
        db.insert(TABLE_ADDRESS, null, addr2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add location columns to existing address table
            db.execSQL("ALTER TABLE " + TABLE_ADDRESS + " ADD COLUMN " + COL_LATITUDE + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_ADDRESS + " ADD COLUMN " + COL_LONGITUDE + " REAL");

            // Create new tables
            String createOrderTable = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER + " (" +
                    COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ORDER_CUSTOMER_ID + " INTEGER NOT NULL, " +
                    COL_ORDER_DATE + " TEXT NOT NULL, " +
                    COL_ORDER_TOTAL + " REAL NOT NULL, " +
                    COL_ORDER_ADDRESS + " TEXT NOT NULL, " +
                    COL_PAYMENT_METHOD + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COL_ORDER_CUSTOMER_ID + ") REFERENCES " +
                    TABLE_CUSTOMER + "(" + COL_CUSTOMER_ID + "))";
            db.execSQL(createOrderTable);

            String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_ITEMS + " (" +
                    COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ORDER_ITEM_ORDER_ID + " INTEGER NOT NULL, " +
                    COL_ITEM_NAME + " TEXT NOT NULL, " +
                    COL_ITEM_PRICE + " REAL NOT NULL, " +
                    COL_ITEM_QUANTITY + " INTEGER NOT NULL, " +
                    COL_ITEM_IMAGE_RES_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COL_ORDER_ITEM_ORDER_ID + ") REFERENCES " +
                    TABLE_ORDER + "(" + COL_ORDER_ID + "))";
            db.execSQL(createOrderItemsTable);
        }
    }

    // Register new customer
    public long registerCustomer(String username, String password, String fullName, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);
        cv.put(COL_FULL_NAME, fullName);
        cv.put(COL_PHONE, phone);
        cv.put(COL_EMAIL, email);
        return db.insert(TABLE_CUSTOMER, null, cv);
    }

    // Login validation
    public Customer loginCustomer(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMER,
                null,
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        Customer customer = null;
        if (cursor != null && cursor.moveToFirst()) {
            customer = new Customer(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_CUSTOMER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL))
            );
            cursor.close();
        }
        return customer;
    }

    // Add new address with location
    public long addAddress(int customerId, Address address) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (address.isDefault()) {
            ContentValues cv = new ContentValues();
            cv.put(COL_IS_DEFAULT, 0);
            db.update(TABLE_ADDRESS, cv, COL_ADDRESS_CUSTOMER_ID + "=?",
                    new String[]{String.valueOf(customerId)});
        }

        ContentValues cv = new ContentValues();
        cv.put(COL_ADDRESS_CUSTOMER_ID, customerId);
        cv.put(COL_ADDRESS_LABEL, address.getLabel());
        cv.put(COL_HOUSE_NO, address.getHouseNo());
        cv.put(COL_STREET, address.getStreet());
        cv.put(COL_AREA, address.getArea());
        cv.put(COL_CITY, address.getCity());
        cv.put(COL_STATE, address.getState());
        cv.put(COL_PINCODE, address.getPincode());
        cv.put(COL_LANDMARK, address.getLandmark());
        cv.put(COL_LATITUDE, address.getLatitude());
        cv.put(COL_LONGITUDE, address.getLongitude());
        cv.put(COL_IS_DEFAULT, address.isDefault() ? 1 : 0);

        return db.insert(TABLE_ADDRESS, null, cv);
    }

    // Get all addresses for a customer
    public List<Address> getCustomerAddresses(int customerId) {
        List<Address> addresses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADDRESS,
                null,
                COL_ADDRESS_CUSTOMER_ID + "=?",
                new String[]{String.valueOf(customerId)},
                null, null, COL_IS_DEFAULT + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int latIndex = cursor.getColumnIndex(COL_LATITUDE);
                int lonIndex = cursor.getColumnIndex(COL_LONGITUDE);

                double latitude = 0;
                double longitude = 0;

                if (latIndex != -1 && !cursor.isNull(latIndex)) {
                    latitude = cursor.getDouble(latIndex);
                }
                if (lonIndex != -1 && !cursor.isNull(lonIndex)) {
                    longitude = cursor.getDouble(lonIndex);
                }

                Address address = new Address(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ADDRESS_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS_LABEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_HOUSE_NO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STREET)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AREA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PINCODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LANDMARK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_DEFAULT)) == 1,
                        latitude,
                        longitude
                );
                addresses.add(address);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return addresses;
    }

    // Get default address
    public Address getDefaultAddress(int customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADDRESS,
                null,
                COL_ADDRESS_CUSTOMER_ID + "=? AND " + COL_IS_DEFAULT + "=1",
                new String[]{String.valueOf(customerId)},
                null, null, null);

        Address address = null;
        if (cursor != null && cursor.moveToFirst()) {
            int latIndex = cursor.getColumnIndex(COL_LATITUDE);
            int lonIndex = cursor.getColumnIndex(COL_LONGITUDE);

            double latitude = 0;
            double longitude = 0;

            if (latIndex != -1 && !cursor.isNull(latIndex)) {
                latitude = cursor.getDouble(latIndex);
            }
            if (lonIndex != -1 && !cursor.isNull(lonIndex)) {
                longitude = cursor.getDouble(lonIndex);
            }

            address = new Address(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ADDRESS_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS_LABEL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_HOUSE_NO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STREET)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_AREA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PINCODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_LANDMARK)),
                    true,
                    latitude,
                    longitude
            );
            cursor.close();
        }
        return address;
    }

    // Set default address
    public void setDefaultAddress(int customerId, int addressId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put(COL_IS_DEFAULT, 0);
        db.update(TABLE_ADDRESS, cv1, COL_ADDRESS_CUSTOMER_ID + "=?",
                new String[]{String.valueOf(customerId)});

        ContentValues cv2 = new ContentValues();
        cv2.put(COL_IS_DEFAULT, 1);
        db.update(TABLE_ADDRESS, cv2, COL_ADDRESS_ID + "=?",
                new String[]{String.valueOf(addressId)});
    }

    // Update address
    public int updateAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ADDRESS_LABEL, address.getLabel());
        cv.put(COL_HOUSE_NO, address.getHouseNo());
        cv.put(COL_STREET, address.getStreet());
        cv.put(COL_AREA, address.getArea());
        cv.put(COL_CITY, address.getCity());
        cv.put(COL_STATE, address.getState());
        cv.put(COL_PINCODE, address.getPincode());
        cv.put(COL_LANDMARK, address.getLandmark());
        cv.put(COL_LATITUDE, address.getLatitude());
        cv.put(COL_LONGITUDE, address.getLongitude());

        return db.update(TABLE_ADDRESS, cv, COL_ADDRESS_ID + "=?",
                new String[]{String.valueOf(address.getAddressId())});
    }

    // Delete address
    public int deleteAddress(int addressId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ADDRESS, COL_ADDRESS_ID + "=?",
                new String[]{String.valueOf(addressId)});
    }

    // Check if username exists
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMER,
                new String[]{COL_CUSTOMER_ID},
                COL_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    // Save order to database
    public long saveOrder(int customerId, Order order, String deliveryAddress, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert order
        ContentValues orderValues = new ContentValues();
        orderValues.put(COL_ORDER_CUSTOMER_ID, customerId);
        orderValues.put(COL_ORDER_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        orderValues.put(COL_ORDER_TOTAL, order.getTotal());
        orderValues.put(COL_ORDER_ADDRESS, deliveryAddress);
        orderValues.put(COL_PAYMENT_METHOD, paymentMethod);

        long orderId = db.insert(TABLE_ORDER, null, orderValues);

        if (orderId > 0) {
            // Insert order items
            for (GroceryItem item : order.getItems()) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COL_ORDER_ITEM_ORDER_ID, orderId);
                itemValues.put(COL_ITEM_NAME, item.getName());
                itemValues.put(COL_ITEM_PRICE, item.getPrice());
                itemValues.put(COL_ITEM_QUANTITY, item.getQuantity());
                itemValues.put(COL_ITEM_IMAGE_RES_ID, item.getImageResId());
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }
        }

        return orderId;
    }

    // Get customer orders
    public List<OrderHistory> getCustomerOrders(int customerId) {
        List<OrderHistory> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDER,
                null,
                COL_ORDER_CUSTOMER_ID + "=?",
                new String[]{String.valueOf(customerId)},
                null, null, COL_ORDER_DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_ID));
                String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_DATE));
                double orderTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ORDER_TOTAL));
                String deliveryAddress = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_ADDRESS));
                String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAYMENT_METHOD));

                // Get order items
                List<GroceryItem> items = getOrderItems(orderId);

                OrderHistory orderHistory = new OrderHistory(orderId, orderDate, orderTotal, deliveryAddress, paymentMethod, items);
                orders.add(orderHistory);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return orders;
    }

    // Get order items
    private List<GroceryItem> getOrderItems(int orderId) {
        List<GroceryItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDER_ITEMS,
                null,
                COL_ORDER_ITEM_ORDER_ID + "=?",
                new String[]{String.valueOf(orderId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ITEM_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_QUANTITY));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_IMAGE_RES_ID));

                GroceryItem item = new GroceryItem(name, price, imageResId);
                item.setQuantity(quantity);
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return items;
    }
}