package com.example.diary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.diary.adapter.ListItem;
import com.example.diary.adapter.MapMarker;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context){
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }
    public void openDb(){
        db = myDbHelper.getWritableDatabase();
    }

//    первая таблица
    public void insertToDb(String title, String description, String uri, String quote){
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESCRIPTION, description);
        cv.put(MyConstants.URI, uri);
        cv.put(MyConstants.QUOTE, quote);

        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public void delete(int id){
        String selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME, selection, null);
    }

    public void update(String title, String description, String uri, int id, String quote){
        String selection = MyConstants._ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESCRIPTION, description);
        cv.put(MyConstants.URI, uri);
        cv.put(MyConstants.QUOTE, quote);
        db.update(MyConstants.TABLE_NAME, cv, selection, null);
    }

    public List<ListItem> getFromDb(){
        List<ListItem> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null,
                null, null);
        while (cursor.moveToNext()) {
            ListItem item = new ListItem();

            int columnIndex1 = cursor.getColumnIndex(MyConstants.TITLE);
            int columnIndex2 = cursor.getColumnIndex(MyConstants.DESCRIPTION);
            int columnIndex3 = cursor.getColumnIndex(MyConstants.URI);
            int columnIndex5 = cursor.getColumnIndex(MyConstants.QUOTE);
            int columnIndex4 = cursor.getColumnIndex(MyConstants._ID);

            if (columnIndex1 != -1) {
                item.setTitle(cursor.getString(columnIndex1));
            } else {
                android.util.Log.e("DatabaseError", "Столбец " + MyConstants.TITLE + " не найден!");
            }

            if (columnIndex2 != -1) {
                item.setText(cursor.getString(columnIndex2));
            } else {
                android.util.Log.e("DatabaseError", "Столбец " + MyConstants.DESCRIPTION + " не найден!");
            }

            if (columnIndex3 != -1) {
                item.setUri(cursor.getString(columnIndex3)); // Добавляем URI
            } else {
                android.util.Log.e("DatabaseError", "Столбец " + MyConstants.URI + " не найден!");
            }

            if (columnIndex4 != -1) {
                item.setId(cursor.getInt(columnIndex4));
            } else {
                android.util.Log.e("DatabaseError", "Столбец " + MyConstants._ID + " не найден!");
            }

            if (columnIndex5 != -1) {
                item.setQuote(cursor.getString(columnIndex5));
            } else {
                android.util.Log.e("DatabaseError", "Столбец " + MyConstants.QUOTE + " не найден!");
            }

            tempList.add(item);
        }

        cursor.close();
        return tempList;
    }

//    вторая таблица

    // Добавление метки
    public long insertMarkerToDb(double latitude, double longitude, String title) {
        if (db == null || !db.isOpen()) {
            openDb();
        }

        ContentValues cv = new ContentValues();
        cv.put(MyConstants.LATITUDE, latitude);
        cv.put(MyConstants.LONGITUDE, longitude);
        cv.put(MyConstants.MARKER_TITLE, title);

        return db.insert(MyConstants.TABLE_NAME_MAP, null, cv);
    }

    // Получение всех меток
    public List<MapMarker> getMarkersFromDb() {
        List<MapMarker> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME_MAP, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            MapMarker marker = new MapMarker();
            int idIndex = cursor.getColumnIndex(MyConstants._ID_TAG);
            int latIndex = cursor.getColumnIndex(MyConstants.LATITUDE);
            int lngIndex = cursor.getColumnIndex(MyConstants.LONGITUDE);
            int titleIndex = cursor.getColumnIndex(MyConstants.MARKER_TITLE);

            if (idIndex != -1) marker.setId(cursor.getInt(idIndex));
            if (latIndex != -1) marker.setLatitude(cursor.getDouble(latIndex));
            if (lngIndex != -1) marker.setLongitude(cursor.getDouble(lngIndex));
            if (titleIndex != -1) marker.setTitle(cursor.getString(titleIndex));

            tempList.add(marker);
        }
        cursor.close();
        return tempList;
    }

    // Удаление метки
    public void deleteMarker(int id) {
        String selection = MyConstants._ID_TAG + "=" + id;
        db.delete(MyConstants.TABLE_NAME_MAP, selection, null);
    }

    public void updateMarker(int id, double latitude, double longitude, String title) {
        String selection = MyConstants._ID_TAG + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.LATITUDE, latitude);
        cv.put(MyConstants.LONGITUDE, longitude);
        cv.put(MyConstants.MARKER_TITLE, title);
        db.update(MyConstants.TABLE_NAME_MAP, cv, selection, null);
    }

    public void closeDb(){
       myDbHelper.close();
    }

}
