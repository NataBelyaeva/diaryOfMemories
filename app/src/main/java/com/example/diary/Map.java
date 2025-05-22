package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.diary.adapter.MapMarker;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.*;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;
import com.example.diary.db.MyDbManager;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Map extends AppCompatActivity {
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private MyDbManager myDbManager;
    private EditText noteEditText;
    private ConstraintLayout noteContainer;
    private Button saveNoteButton, deleteNoteButton;
    private Point currentMarkerPoint;
    private PlacemarkMapObject currentMapObject;
    private int currentMarkerId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_map);

        

        initViews();
        setupMap();
    }

    private void initViews() {
        myDbManager = new MyDbManager(this);
        mapView = findViewById(R.id.mapview);
        noteEditText = findViewById(R.id.MultiLineMap);
        noteContainer = findViewById(R.id.noteMap);
        saveNoteButton = findViewById(R.id.buttonSaveNoteMap);
        deleteNoteButton = findViewById(R.id.buttonDelNoteMap);


        saveNoteButton.setOnClickListener(v -> saveNote());
        deleteNoteButton.setOnClickListener(v -> onClickDeleteNote(v));
    }

    private void setupMap() {
        mapObjects = mapView.getMapWindow().getMap().getMapObjects();

        mapView.getMapWindow().getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
                createNewMarker(point);
            }

            @Override
            public void onMapLongTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
                // Не используется
            }
        });

        mapView.getMapWindow().getMap().move(new CameraPosition(
                new Point(56.010569, 92.852545), // Москва
                14.0f, 0.0f, 0.0f
        ));
    }

    private void createNewMarker(Point point) {
        currentMarkerPoint = point;
        currentMarkerId = -1;
        currentMapObject = null;
        showNoteDialog("");
    }

    private void showNoteDialog(String note) {
        noteEditText.setText(note);
        noteContainer.setVisibility(View.VISIBLE);
    }

    private void saveNote() {
        String noteText = noteEditText.getText().toString().trim();

        if (noteText.isEmpty()) {
            Toast.makeText(this, "Введите текст заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentMarkerPoint != null) {
            if (currentMarkerId == -1) {
                // Новая метка
                long newId = myDbManager.insertMarkerToDb(
                        currentMarkerPoint.getLatitude(),
                        currentMarkerPoint.getLongitude(),
                        noteText
                );
                addMarkerToMap(currentMarkerPoint, noteText, (int)newId);
            } else {
                // Обновление существующей метки
                myDbManager.updateMarker(
                        currentMarkerId,
                        currentMarkerPoint.getLatitude(),
                        currentMarkerPoint.getLongitude(),
                        noteText
                );

                if (currentMapObject != null) {
                    currentMapObject.setUserData(new MarkerData(noteText, currentMarkerId));
                }
            }
        }

        noteContainer.setVisibility(View.GONE);
    }



    public void onClickDeleteNote(View view) {
        if (currentMarkerId != -1 && currentMapObject != null) {
            // Удаляем из базы данных
            myDbManager.deleteMarker(currentMarkerId);

            // Удаляем с карты
            mapObjects.remove(currentMapObject);

            // Сбрасываем текущие значения
            currentMarkerId = -1;
            currentMapObject = null;
            currentMarkerPoint = null;

            // Скрываем панель редактирования
            noteContainer.setVisibility(View.GONE);

            Toast.makeText(this, "Метка удалена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Не выбрана метка для удаления", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMarkerToMap(Point point, String title, int id) {
        try {
            ImageProvider icon = ImageProvider.fromResource(this, R.drawable.tag);
            PlacemarkMapObject marker = mapObjects.addPlacemark(point, icon);

            marker.setIconStyle(new IconStyle()
                    .setAnchor(new PointF(0.5f, 1.0f))
                    .setScale(1.5f)
                    .setZIndex(100f));

            marker.addTapListener((mapObject, tappedPoint) -> {
                showMarkerDetails((PlacemarkMapObject) mapObject);
                return true;
            });

            marker.setUserData(new MarkerData(title, id));
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка добавления метки: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showMarkerDetails(PlacemarkMapObject marker) {
        currentMapObject = marker;
        currentMarkerPoint = marker.getGeometry();

        if (marker.getUserData() instanceof MarkerData) {
            MarkerData data = (MarkerData) marker.getUserData();
            currentMarkerId = data.getId();
            showNoteDialog(data.getTitle());
        }
    }

    private void loadMarkersFromDb() {
        List<MapMarker> markers = myDbManager.getMarkersFromDb();
        for (MapMarker marker : markers) {
            addMarkerToMap(marker.getPoint(), marker.getTitle(), marker.getId());
        }
    }

    public void onClickCancel(View view) {
        noteContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        loadMarkersFromDb();
    }

    @Override
    protected void onPause() {
        myDbManager.closeDb();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    public void toStr4(View v) {
        Intent intent = new Intent(this, Str4.class);
        startActivity(intent);
    }

    private static class MarkerData {
        private final String title;
        private final int id;

        public MarkerData(String title, int id) {
            this.title = title;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }
    }
}