package com.example.diary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.diary.adapter.ListItem;
import com.example.diary.db.MyConstants;
import com.example.diary.db.MyDbManager;


import android.widget.TextView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Note extends AppCompatActivity {
    private EditText edTitle, edText;
    private TextView textQuotes;
    private MyDbManager myDbManager;
    private ImageView ImageView;
    private ConstraintLayout ImageContainer, Quotes;
    private String tempURI = "empty";
    private String tempQuote = "empty";
    private final int PICK_IMAGE_CODE = 123;
    private boolean isEditState = true;
    private ListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        init();
        getMyIntents();

    }

    private void fetchQuote() {
        RetrofitClient.getApi().getQuotes().enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Quote quote = response.body().get(0);
                    translateToRussian(quote.getQuote()); // Переводим цитату
                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Log.e("API_ERROR", "Ошибка загрузки данных", t);
            }
        });
    }

    // Метод для перевода цитаты на русский язык через MyMemory API
    private void translateToRussian(String text) {
        String url = "https://api.mymemory.translated.net/get?q=" + text + "&langpair=en|ru";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mymemory.translated.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslationApi translationApi = retrofit.create(TranslationApi.class);
        translationApi.getTranslation(url).enqueue(new Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String translatedText = response.body().getTranslatedText();

                    tempQuote = translatedText;

                    // Обновляем UI
                    runOnUiThread(() -> textQuotes.setText(tempQuote));
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {
                Log.e("TRANSLATE_ERROR", "Ошибка перевода", t);
            }
        });
    }

    public void onClickAddQuotes(View view){
        Quotes.setVisibility(View.VISIBLE);
        fetchQuote();
    }


    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK && requestCode == PICK_IMAGE_CODE && data != null){
            tempURI = data.getData().toString();
            ImageView.setImageURI(data.getData());

            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);


        }
    }

    private void init(){
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitile);
        edText = findViewById(R.id.edText);
        ImageContainer = findViewById(R.id.ImageContainer);
        ImageView = findViewById(R.id.imageView);
        Quotes = findViewById(R.id.Quotes);
        textQuotes = findViewById(R.id.textQuotes);

    }

    private void getMyIntents(){
        Intent i = getIntent();
        if (i != null){
            item = (ListItem)i.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = i.getBooleanExtra(MyConstants.EDIT_STATE, true);
            if (!isEditState){
                edTitle.setText(item.getTitle());
                edText.setText(item.getText());
                if (!item.getUri().equals("empty")){
                    tempURI = item.getUri();
                    ImageContainer.setVisibility(View.VISIBLE);
                    ImageView.setImageURI(Uri.parse(item.getUri()));

                }
                if (item.getQuote() != null && !item.getQuote().equals("empty")) {
                    tempQuote = item.getQuote();
                    Quotes.setVisibility(View.VISIBLE);
                    textQuotes.setText(item.getQuote());
                }

            }
        }
    }

    public void onClickSave(View view) {
        String title = edTitle.getText().toString();
        String text = edText.getText().toString();
        String quote = textQuotes.getText().toString();


        if (title.equals("") || text.equals("")) {
            Toast.makeText(this, "Заголовок или содержание не заполнено", Toast.LENGTH_SHORT).show();
        } else {
            // Отладка: проверяем, какой tempURI сохраняется
            Log.d("DEBUG", "Saving image URI: " + tempURI);
            Log.d("DEBUG", "Saving quote: " + quote);

            if (isEditState) {
                myDbManager.insertToDb(title, text, tempURI, quote);
            } else {
                myDbManager.update(title, text, tempURI, item.getId(), quote);
            }

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            myDbManager.closeDb();
            finish();
        }
    }

    public void onClickDeleteImage(View view){
        ImageView.setImageResource(R.drawable.photo_svgrepo_com);
        tempURI = "empty";
        ImageContainer.setVisibility(View.GONE);
    }
    public void onClickAddImage(View view){
        ImageContainer.setVisibility(View.VISIBLE);

    }
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                tempURI = imageUri.toString();
                                ImageView.setImageURI(imageUri);

                                // Запрашиваем постоянное разрешение на доступ к файлу
                                getContentResolver().takePersistableUriPermission(
                                        imageUri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            }
                        }
                    });


    public void onClickChoose(View view) {
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.setType("image/*");
        chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Добавили флаг
        imagePickerLauncher.launch(chooser);
    }



}