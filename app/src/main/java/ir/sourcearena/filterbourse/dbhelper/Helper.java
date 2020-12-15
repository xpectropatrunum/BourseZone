package ir.sourcearena.filterbourse.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class Helper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "USER_FILTERS";
    private static final String TABLE_CONTACTS = "FILTERS";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FUNCTION = "function";
    private static final String KEY_TYPE = "type";
    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_FUNCTION + " TEXT, "  +KEY_TYPE + "  " +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    public void addFilter(Filter filter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, filter.getName()); // Contact Name
        values.put(KEY_FUNCTION, filter.getFunctionr()); // Contact Phone
        values.put(KEY_TYPE, filter.getType()); // Contact Phone
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public void editFilter(String name,String cond) {
        SQLiteDatabase db = this.getWritableDatabase();
        String edit = "UPDATE " +TABLE_CONTACTS+
                "    SET "+KEY_FUNCTION+" = '"+cond +"'"+
                "    WHERE " +
               KEY_NAME +" ='"+name+"'";
        db.execSQL(edit);
    }

    // code to get the single contact
    Filter getFunction(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_FUNCTION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Filter contact = new Filter(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Filter> getAllFilters() {
        List<Filter> contactList = new ArrayList<Filter>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Filter contact = new Filter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setFunction(cursor.getString(2));
                contact.setType(cursor.getInt(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }


        return contactList;
    }

    public boolean checkExists(String name){
        List<Filter> contactList = new ArrayList<Filter>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" WHERE name LIKE '"+name+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
          return true;
        }
        return false;
    }
    public int getType(String name){
        List<Filter> contactList = new ArrayList<Filter>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" WHERE name LIKE '"+name+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return cursor.getInt(3);
        }
        return 0;
    }
    public String getFunction(String name){
        List<Filter> contactList = new ArrayList<Filter>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" WHERE name LIKE '"+name+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return cursor.getString(2);
        }
        return "";
    }
    // code to update the single contact
    public int updateContact(Filter contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_FUNCTION, contact.getFunctionr());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteFilter(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + "='" + name+"'", null);
    }
    public void deleteID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        // return count
        return cursor.getCount();
    }

}