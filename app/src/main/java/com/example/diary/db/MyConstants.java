package com.example.diary.db;

public class MyConstants {
    public static final String LIST_ITEM_INTENT = "list_item_intent";
    public static final String EDIT_STATE = "edit_state";

    public static final String TABLE_NAME = "notes";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String URI = "uri";
    public static final String QUOTE = "quote";

    public static final String TABLE_NAME_MAP = "map_notes";
    public static final String _ID_TAG = "_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String MARKER_TITLE = "marker_title";

    public static final String DB_NAME = "my_db.db";
    public static final int DB_VERSION = 4;
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, " + TITLE + " TEXT, " + DESCRIPTION + " TEXT, " +
            URI +  " TEXT, " +  QUOTE + " TEXT) ";

    public static final String TABLE_STRUCTURE_MAP = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_MAP + " (" + _ID_TAG + " INTEGER PRIMARY KEY, " + LATITUDE + " TEXT, " + LONGITUDE + " TEXT, "
            + MARKER_TITLE + " TEXT) ";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String DROP_TABLE_MAP = "DROP TABLE IF EXISTS " + TABLE_NAME_MAP;




}
