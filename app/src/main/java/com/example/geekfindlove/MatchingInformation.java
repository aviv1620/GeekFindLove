package com.example.geekfindlove;

public class MatchingInformation {
    private int percent;
    private String userIdDst;
    private String userIdSrs;
    private DstDetail dstDetail;

    public MatchingInformation() {
    }

    public MatchingInformation(int percent, String userIdDst, String userIdSrs, DstDetail dstDetail) {
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

    public DstDetail getDstDetail() {
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

    public void setDstDetail(DstDetail dstDetail) {
        this.dstDetail = dstDetail;
    }

    static public class DstDetail{
        private String phoneNumber;
        private String fullName;
        private String avatarUrl;

        public DstDetail() {
        }

        public DstDetail(String phoneNumber, String fullName, String avatarUrl) {
            this.phoneNumber = phoneNumber;
            this.fullName = fullName;
            this.avatarUrl = avatarUrl;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getFullName() {
            return fullName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
