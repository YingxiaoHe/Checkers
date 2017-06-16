package com.example.hyx.billiardball.activity;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by HYX on 2017/3/4.
 */

public class ActivityList extends Application {

    private List<Activity> activitiesList = new LinkedList();
    private static ActivityList instance;

    private ActivityList() {
    }

    public synchronized static ActivityList getInstance() {
        if (instance == null) {
            instance = new ActivityList();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activitiesList.add(activity);
    }

    public void removeActivity(Activity activity) {
        for (int i = 0; i < activitiesList.size(); i++) {
            if (activitiesList.get(i) == activity) {
                activitiesList.remove(i);
                activity.finish();
            }
        }
    }

    public void finishAll() {
        try {
            for (Activity activity : activitiesList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
