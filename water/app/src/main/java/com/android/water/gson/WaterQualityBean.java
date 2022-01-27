package com.android.water.gson;

import java.util.List;

public class WaterQualityBean {

    private String resultcode;
    private String reason;
    private List<ResultBean> result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        private String state;
        private String ph;
        private String phquality;
        private String oxygen;
        private String oxygenquality;
        private String nitrogen;
        private String nitrogenquality;
        private String permangan;
        private String permanganquality;
        private String orgacarbon;
        private String orgacarbonquality;
        private String section;
        private String profile;
        private String belong;
        private String date;
        private String time;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPh() {
            return ph;
        }

        public void setPh(String ph) {
            this.ph = ph;
        }

        public String getPhquality() {
            return phquality;
        }

        public void setPhquality(String phquality) {
            this.phquality = phquality;
        }

        public String getOxygen() {
            return oxygen;
        }

        public void setOxygen(String oxygen) {
            this.oxygen = oxygen;
        }

        public String getOxygenquality() {
            return oxygenquality;
        }

        public void setOxygenquality(String oxygenquality) {
            this.oxygenquality = oxygenquality;
        }

        public String getNitrogen() {
            return nitrogen;
        }

        public void setNitrogen(String nitrogen) {
            this.nitrogen = nitrogen;
        }

        public String getNitrogenquality() {
            return nitrogenquality;
        }

        public void setNitrogenquality(String nitrogenquality) {
            this.nitrogenquality = nitrogenquality;
        }

        public String getPermangan() {
            return permangan;
        }

        public void setPermangan(String permangan) {
            this.permangan = permangan;
        }

        public String getPermanganquality() {
            return permanganquality;
        }

        public void setPermanganquality(String permanganquality) {
            this.permanganquality = permanganquality;
        }

        public String getOrgacarbon() {
            return orgacarbon;
        }

        public void setOrgacarbon(String orgacarbon) {
            this.orgacarbon = orgacarbon;
        }

        public String getOrgacarbonquality() {
            return orgacarbonquality;
        }

        public void setOrgacarbonquality(String orgacarbonquality) {
            this.orgacarbonquality = orgacarbonquality;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getBelong() {
            return belong;
        }

        public void setBelong(String belong) {
            this.belong = belong;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
