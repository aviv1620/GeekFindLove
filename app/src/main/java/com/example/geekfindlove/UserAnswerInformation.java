package com.example.geekfindlove;

import android.accessibilityservice.GestureDescription;

import java.util.HashMap;

public class UserAnswerInformation {
    private HashMap<String,Integer> answer;
    private MatchingInformation.DstDetail  userDetails;

    public UserAnswerInformation(){
        answer = new HashMap<>();
    }

    public UserAnswerInformation(HashMap<String, Integer> answer, MatchingInformation.DstDetail userDetails) {
        this.answer = answer;
        this.userDetails = userDetails;
    }

    public void setAnswer(HashMap<String, Integer> answer) {
        this.answer = answer;
    }

    public void setUserDetails(MatchingInformation.DstDetail userDetails) {
        this.userDetails = userDetails;
    }

    public HashMap<String, Integer> getAnswer() {
        return answer;
    }

    public MatchingInformation.DstDetail getUserDetails() {
        return userDetails;
    }

    public void put(String questionID, int itemPosition) {
        answer.put(questionID,itemPosition);
    }
}
