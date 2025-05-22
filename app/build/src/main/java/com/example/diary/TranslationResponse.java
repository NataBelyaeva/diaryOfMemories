package com.example.diary;

import com.google.gson.annotations.SerializedName;

public class TranslationResponse {
    @SerializedName("responseData")
    private TranslationData responseData;

    public String getTranslatedText() {
        return responseData != null ? responseData.translatedText : "";
    }

    public static class TranslationData {
        @SerializedName("translatedText")
        private String translatedText;
    }
}

