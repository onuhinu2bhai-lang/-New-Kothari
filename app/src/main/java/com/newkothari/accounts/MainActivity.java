package com.newkothari.accounts;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.*;

public class MainActivity extends Activity {

    SQLiteDatabase db;
    LinearLayout root;

    int green = Color.rgb(13, 62, 48);
    int gold = Color.rgb(220, 180, 90);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = openOrCreateDatabase(
            "NewKothari.db", MODE_PRIVATE, null
        );

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS customers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT, phone TEXT)"
        );

        home();
  }    void setup(String title) {
        root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(18, 18, 18, 18);
        root.setBackgroundColor(Color.rgb(18, 30, 27));
        setContentView(root);

        TextView heading = new TextView(this);
        heading.setText("NK | " + title);
        heading.setTextSize(24);
        heading.setTextColor(gold);
        heading.setPadding(12, 12, 12, 12);
        root.addView(heading);
    }

    void home() {
        setup("NEW KOTHARI");
        TextView tagline = new TextView(this);
        tagline.setText("Har Ghar Ki Zaroorat");
        tagline.setTextColor(Color.WHITE);
        tagline.setTextSize(16);
        root.addView(tagline);

        Button add = new Button(this);
        add.setText("Add Customer");
        add.setOnClickListener(v -> addCustomer());
        root.addView(add);

        Button list = new Button(this);
        list.setText("Customer List");
        list.setOnClickListener(v -> customerList());
        root.addView(list);
    }    void addCustomer() {
        setup("Add Customer");

        EditText name = new EditText(this);
        name.setHint("Customer Name");
        root.addView(name);

        EditText phone = new EditText(this);
        phone.setHint("Mobile Number");
        root.addView(phone);

        Button save = new Button(this);
        save.setText("Save Customer");
        root.addView(save);

        save.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String p = phone.getText().toString().trim();

            if (n.isEmpty()) {
                name.setError("Enter customer name");
                return;
            }

            db.execSQL(
                "INSERT INTO customers(name, phone) VALUES(?,?)",
                new Object[]{n, p}
            );

            Toast.makeText(this, "Customer Saved!",
                Toast.LENGTH_SHORT).show();

            home();
        });

        Button back = new Button(this);
        back.setText("Back");
        root.addView(back);
        back.setOnClickListener(v -> home());
    }    void customerList() {
        setup("Customer List");

        Cursor c = db.rawQuery(
            "SELECT id, name, phone FROM customers ORDER BY name",
            null
        );

        if (c.getCount() == 0) {
            TextView empty = new TextView(this);
            empty.setText("No customers added yet.");
            empty.setTextColor(Color.WHITE);
            root.addView(empty);
        }

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String phone = c.getString(2);

            Button item = new Button(this);
            item.setText(name + " - " + phone);
            root.addView(item);
        }

        c.close();

        Button back = new Button(this);
        back.setText("Back");
        root.addView(back);
        back.setOnClickListener(v -> home());
    }
          }
