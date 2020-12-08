package com.example.geekfindlove;

public class MatchingInformation {
    private int percent;
    private String userIdDst;
    private String userIdSrs;
    private UserInformation dstDetail;

    public MatchingInformation() {
    }

    public MatchingInformation(int percent, String userIdDst, String userIdSrs, UserInformation dstDetail) {
        this.percent = percent;
        this.userIdDst = userIdDst;
        this.userIdSrs = userIdSrs;
        this.dstDetail = dstDetail;
    }

    public int getPercent() {
        return percent;
    }

    public String getUserIdDst() {
        return userIdDst;
    }

    public String getUserIdSrs() {
        return userIdSrs;
    }

    public UserInformation getDstDetail() {
        return dstDetail;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public void setUserIdDst(String userIdDst) {
        this.userIdDst = userIdDst;
    }

    public void setUserIdSrs(String userIdSrs) {
        this.userIdSrs = userIdSrs;
    }

    public void setDstDetail(UserInformation dstDetail) {
        this.dstDetail = dstDetail;
    }


}
