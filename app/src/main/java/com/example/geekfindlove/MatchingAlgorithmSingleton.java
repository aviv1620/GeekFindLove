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

    private int myMinAge;
    private int myMaxAge;
    private String myLoctaion;

    private MatchingAlgorithmSingleton() {
        //default values in case the user doesnt want to filter searching.

    }

    public void setMe(UserAnswerInformation me) {
        this.me = me;
    }

    public UserAnswerInformation getMe() {
        if (me == null)
            me = new UserAnswerInformation();
        return me;
    }

    public void setFilters(int minAge, int maxAge, String loctaion) {
        this.myMinAge = minAge;
        this.myMaxAge = maxAge;
        this.myLoctaion = loctaion;

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
        int percentage = percentAlgorithm(match);
        if (percentage != -1) {
            MatchingInformation mi1 = new MatchingInformation(percentage, user.getId(), me.getUserDetails().getId(), user);
            return mi1;
        } else
            return null;

    }

    /**
     * -1 percent no match.
     */
    private int percentAlgorithm(UserAnswerInformation match) {
        double count = 0; // count how many questions the two user answered the same. so then we can divide and get the percentage
        double num_of_questions = 0; // if one user answered 10 question, and one user answered 20 questions,
        // we want the sum of question they both answered, regardless the answer
        int candidentAge = match.getUserDetails().getAge();
        String candidentLocation = match.getUserDetails().getLocation();

        String myWantedGender = me.getUserDetails().getActualOrientation(); //Men ,Women, Both
        String candidentWantedGender = match.getUserDetails().getActualOrientation();

        if (candidentAge < myMinAge || candidentAge > myMaxAge) { // not correspond with my filter choice.(for age).
            return -1;
        } else if (!candidentLocation.equals(myLoctaion) && !myLoctaion.equals("Location")) { // the same for not equal wanted location search.
            return -1;
        }

        // here we wanna check if my choice equals to the other side Gender or my choice equals to both genders
        // and respectivly the other side choice - if its equals to my Gender or the other side choice is both.
        else if (!(myWantedGender.equals(match.getUserDetails().getGender()) || myWantedGender.equals("Both")
                && match.getUserDetails().getActualOrientation().equals(me.getUserDetails().getGender()) || match.getUserDetails().getActualOrientation().equals("Both"))) {
            return -1;
        }


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
