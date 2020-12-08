package com.example.geekfindlove;

import android.util.Log;


public class MatchingAlgorithmSingleton{

    private static final String TAG = "MatchingAlgorithm";
    private static MatchingAlgorithmSingleton matchingAlgorithmSingleton;

    public static MatchingAlgorithmSingleton getInstance(){
        if(matchingAlgorithmSingleton == null){
            matchingAlgorithmSingleton = new MatchingAlgorithmSingleton();
        }
        return matchingAlgorithmSingleton;
    }
    private  UserAnswerInformation me;

    private boolean filterAgeMin;
    private boolean filterAgeMax;

    private boolean filterHeightMin;
    private boolean filterHeightMax;

    private MatchingAlgorithmSingleton(){
    }

    public void setMe(UserAnswerInformation me) {
        this.me = me;
    }

    public UserAnswerInformation getMe() {
        if(me == null)
            me = new UserAnswerInformation();
        return me;
    }

    public void setFilters(boolean filterAgeMin, boolean filterAgeMax, boolean filterHeightMin, boolean filterHeightMax) {
        this.filterAgeMin = filterAgeMin;
        this.filterAgeMax = filterAgeMax;
        this.filterHeightMin = filterHeightMin;
        this.filterHeightMax = filterHeightMax;
    }


    /**
     *
     * @return null no match
     */
    public MatchingInformation UserAnswerInformation_To_MatchingInformation(UserAnswerInformation match){
        if(me == null) {
            Log.e(TAG, "set me before use");
            return null;
        }
        //TODO make MatchingInformation.
        UserInformation u1 = new UserInformation("alice", "a", "alice@a.com", "female", "BgnkBYOP7dg8cY3Wc7GezM3IBSv2");
        MatchingInformation mi1 = new MatchingInformation(90, "BgnkBYOP7dg8cY3Wc7GezM3IBSv2", "srcId1", u1);
        return mi1;
    }

    /**
     * -1 percent no match.
     */
    private float percentAlgorithm(UserInformation match){
        //TODO write the algorithm;
        return 0;
    }

}
