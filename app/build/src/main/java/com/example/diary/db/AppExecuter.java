package com.example.diary.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecuter {
    private static AppExecuter instance;
    private final Executor mainIo;
    private final Executor subIo;

    public AppExecuter(Executor mainIo, Executor subIo) {
        this.mainIo = mainIo;
        this.subIo = subIo;
    }

    public static AppExecuter getInstace(){
        if (instance == null) instance = new AppExecuter(Executors.newSingleThreadExecutor(), new MainThreadHandler());
        return instance;
    }

    public static class MainThreadHandler implements Executor{
        private Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHandler.post(command);

        }
    }

    public Executor getMainIo() {
        return mainIo;
    }

    public Executor getSubIo() {
        return subIo;
    }
}
