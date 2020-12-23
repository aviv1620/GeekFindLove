package com.example.geekfindlove;

import android.accessibilityservice.GestureDescription;

import java.util.HashMap;

public class UserAnswerInformation {
    private HashMap<String, Integer> answer;
    private UserInformation userDetails;

    public UserAnswerInformation() {
    }

    public UserAnswerInformation(HashMap<String, Integer> answer, UserInformation userDetails) {
        this.answer = answer;
        this.userDetails = userDetails;
    }

    public void setAnswer(HashMap<String, Integer> answer) {
        this.answer = answer;
    }

    public void setUserDetails(UserInformation userDetails) {
        this.userDetails = userDetails;
    }

    public HashMap<String, Integer> getAnswer() {
        return answer;
    }

    public UserInformation getUserDetails() {
        return userDetails;
    }

    public void put(String questionID, int itemPosition) {
        answer.put(questionID, itemPosition);
    }
}
