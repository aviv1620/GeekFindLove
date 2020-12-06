package com.example.geekfindlove;

import java.util.ArrayList;

public class QuestionsInformation {
    private String id;
    private String question;
    private ArrayList<String> answers;

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionsInformation(){
        answers=new ArrayList<>();
    }
    public QuestionsInformation(String question, ArrayList<String>answers){
    this.question=question;
    this.answers=answers;
    }

    public String toString(){
        return this.question + " " + this.answers.toString();
    }

}
