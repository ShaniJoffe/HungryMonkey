package com.example.shanijoffe.hungry_monkey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.shanijoffe.hungry_monkey.DishType;

/**
 * Created by Shani Joffe on 12/6/2017.
 */


public class AssignmentsDbHelper extends SQLiteOpenHelper
{
    public static final String DISH_TYPES_TABLE = "dishtype";

    public static final String COLUMN_ID = "_id";
    public static final String DISH_TYPE_NAME = "dishtypename";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dishes.db";

    public AssignmentsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String create_products_table = "CREATE TABLE " + DISH_TYPES_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " + DISH_TYPE_NAME + " TEXT )";
        sqLiteDatabase.execSQL(create_products_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DISH_TYPES_TABLE);
        onCreate(sqLiteDatabase);
    }
    /**
     * Gets all Products in the Database and returns a cursor of them.
     * If there are no items in the database then the cursor returns null
     *
     * @return A Cursor of all products or null
     */
    public Cursor getAllProducts()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DISH_TYPES_TABLE, new String[] {COLUMN_ID, DISH_TYPE_NAME,
               }, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        else
        {
            return null;
        }
    }
    /**
     * Adds a product to the database
     * @param dishType the product to be added.
     */
    public void addProduct(DishType dishType) {
        ContentValues values = new ContentValues();
        values.put(DISH_TYPE_NAME, dishType.getDishtName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DISH_TYPES_TABLE, null, values);
        db.close();
    }

    /**
     * Finds a product in the database and returns it to the caller. If this function does not find
     * a product, then the returned product is null
     * @param dishtname Name of the product to find
     * @return A valid product or a null product
     */
    public DishType findProduct(String dishtname) {
        String query = "Select * FROM " + DISH_TYPES_TABLE + " WHERE " + DISH_TYPE_NAME +
                " = \"" + dishtname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DishType p = new DishType();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            p.setID(Integer.parseInt(cursor.getString(0)));
            p.setDishName(cursor.getString(1));
            cursor.close();
        } else {
            p = null;
        }
        db.close();
        return p;
    }

    /**
     * This function delete's a product in TABLE_PRODUCTS based on the ID of the product retrieved
     * by it's product name.
     * @param dishname The name of the product to delete
     * @return True if deleted false otherwise.
     */
    public boolean deleteProduct(String dishname) {
        boolean result = false;
        String q = "SELECT * FROM " + DISH_TYPES_TABLE + " WHERE " + DISH_TYPE_NAME
                + " = \"" + dishname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        DishType p = new DishType();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            p.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(DISH_TYPES_TABLE, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(p.getID())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    /**
     * Updates the product passed to this function.
     * @param p The product to update
     * @return True if updated, false otherwise.
     */
    public boolean updateDishType(DishType p) {
        boolean result = false;
        String q = "SELECT * FROM " + DISH_TYPES_TABLE + " WHERE " + COLUMN_ID + " = " + p.getID();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(q, null);
        if (c.moveToFirst())
        {
            String q2 = "UPDATE " + DISH_TYPES_TABLE + " SET " + DISH_TYPE_NAME + " = \""
                    + p.getDishtName() + "\", "  + " = "
                    +" WHERE " + COLUMN_ID + " = " + p.getID();
            db.execSQL(q2);
            result = true;
        }
        db.close();
        return result;
    }

    /**
     * Deletes all records in the dishtype database regardless if the
     * database contains records or not.
     */
    public void deleteAllDishTypes()
    {
        String q = "DELETE FROM " + DISH_TYPES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(q);
        db.close();
    }
}

