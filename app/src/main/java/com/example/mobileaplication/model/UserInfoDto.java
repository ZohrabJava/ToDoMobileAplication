package com.example.mobileaplication.model;

public class UserInfoDto {
    private int newTaskCount;
    private int inProgressTaskCount;
    private int inReviewTaskCount;
    private int rejectedTaskCount;
    private int doneTaskCount;

    public int getNewTaskCount() {
        return newTaskCount;
    }

    public void setNewTaskCount(int newTaskCount) {
        this.newTaskCount = newTaskCount;
    }

    public int getInProgressTaskCount() {
        return inProgressTaskCount;
    }

    public void setInProgressTaskCount(int inProgressTaskCount) {
        this.inProgressTaskCount = inProgressTaskCount;
    }

    public int getInReviewTaskCount() {
        return inReviewTaskCount;
    }

    public void setInReviewTaskCount(int inReviewTaskCount) {
        this.inReviewTaskCount = inReviewTaskCount;
    }

    public int getRejectedTaskCount() {
        return rejectedTaskCount;
    }

    public void setRejectedTaskCount(int rejectedTaskCount) {
        this.rejectedTaskCount = rejectedTaskCount;
    }

    public int getDoneTaskCount() {
        return doneTaskCount;
    }

    public void setDoneTaskCount(int doneTaskCount) {
        this.doneTaskCount = doneTaskCount;
    }
}
