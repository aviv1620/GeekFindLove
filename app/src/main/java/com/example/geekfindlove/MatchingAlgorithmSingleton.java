package com.example.geekfindlove;

import android.util.Log;

import java.util.Map;


public class MatchingAlgorithmSingleton {

    private static final String TAG = "MatchingAlgorithm";
    private static MatchingAlgorithmSingleton matchingAlgorithmSingleton;

    public static MatchingAlgorithmSingleton getInstance() {
        if (matchingAlgorithmSingleton == null) {
            matchingAlgorithmSingleton = new MatchingAlgorithmSingleton();
        }
        return matchingAlgorithmSingleton;
    }

    private UserAnswerInformation me;

    private boolean filterAgeMin;
    private boolean filterAgeMax;

    private boolean filterHeightMin;
    private boolean filterHeightMax;

    private MatchingAlgorithmSingleton() {
    }

    public void setMe(UserAnswerInformation me) {
        this.me = me;
    }

    public UserAnswerInformation getMe() {
        if (me == null)
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
     * @return null no match
     */
    public MatchingInformation UserAnswerInformation_To_MatchingInformation(UserAnswerInformation match) {
        if (me == null || me.getAnswer() == null) {
            Log.e(TAG, "set me before use");
            return null;
        }
        String match_id = match.getUserDetails().getId();
        String me_id = me.getUserDetails().getId();

        if (match_id.equals(me_id))// means we are refering to the same user -> therefor we dont need to find matching percentage
            return null;

        UserInformation user = match.getUserDetails();
        //UserInformation u1 = new UserInformation("alice", "a", "alice@a.com", "female", "BgnkBYOP7dg8cY3Wc7GezM3IBSv2");
        MatchingInformation mi1 = new MatchingInformation(percentAlgorithm(match), user.getId(), me.getUserDetails().getId(), user);
        return mi1;
    }

    /**
     * -1 percent no match.
     */
    private int percentAlgorithm(UserAnswerInformation match) {
        double count = 0; // count how many questions the two user answered the same. so then we can divide and get the percentage
        double num_of_questions = 0; // if one user answered 10 question, and one user answered 20 questions,
        // we want the sum of question they both answered, regardless the answer

        for (Map.Entry mapElement : me.getAnswer().entrySet()) {
            // extracting the question id and the answer that belongs to it
            String q_id = (String) mapElement.getKey();
            int ans = ((int) mapElement.getValue());
            if (match.getAnswer() != null && match.getAnswer().containsKey(q_id)) {
                num_of_questions++; // we found a question that both user answered
                int match_ans = match.getAnswer().get(q_id); // getting the answer of the q_id of the "match" user
                if (match_ans == ans) {
                    count++; // increasing the number of question they both answered the same
                }
            }
        }
        if (num_of_questions == 0) // if they didn't answer the same questions, also fixing arithmetic exception "divide by zero"
            return 0;
        return (int) ((count / num_of_questions) * 100);
    }

}
