package com.example.geekfindlove;

import android.util.Log;

public class MatchingAlgorithmSingleton {

    private static final String TAG = "MatchingAlgorithm";
    private static MatchingAlgorithmSingleton matchingAlgorithmSingleton;

    public static MatchingAlgorithmSingleton getInstance(){
        if(matchingAlgorithmSingleton == null){
            matchingAlgorithmSingleton = new MatchingAlgorithmSingleton();
        }
        return matchingAlgorithmSingleton;
    }
    private  UserInformation me;

    private boolean filterAgeMin;
    private boolean filterAgeMax;

    private boolean filterHeightMin;
    private boolean filterHeightMax;

    private MatchingAlgorithmSingleton(){
    }

    public void setFilters(boolean filterAgeMin,boolean filterAgeMax,boolean filterHeightMin,boolean filterHeightMax) {
        this.filterAgeMin = filterAgeMin;
        this.filterAgeMax = filterAgeMax;
        this.filterHeightMin = filterHeightMin;
        this.filterHeightMax = filterHeightMax;
    }

    public void setMe(UserInformation me) {
        this.me = me;
    }

    /**
     * 0 percent no suitable.
     */
    public MatchingInformation UserInformation_To_MatchingInformation(UserInformation match){
        if(me == null) {
            Log.e(TAG, "set me before use");
            return null;
        }
        //TODO make MatchingInformation.
        return null;
    }

    private float percentAlgorithm(UserInformation match){
        //TODO write the algorithm;
        return 0;
    }
}
