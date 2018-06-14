package com.leona.models;

/**
 * Created by krify on 2/6/17.
 */

public class InfoHelpModel {

    private String question;
    private String answer;
    private String quesheading;
    private boolean quesHead;
    private boolean expandStatus;

    public boolean isExpandStatus() {
        return expandStatus;
    }

    public void setExpandStatus(boolean expandStatus) {
        this.expandStatus = expandStatus;
    }

    public boolean isQuesHead() {
        return quesHead;
    }

    public void setQuesHead(boolean quesHead) {
        this.quesHead = quesHead;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuesheading() {
        return quesheading;
    }

    public void setQuesheading(String quesheading) {
        this.quesheading = quesheading;
    }
}
