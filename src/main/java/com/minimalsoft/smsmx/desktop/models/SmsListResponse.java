/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minimalsoft.smsmx.desktop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author DaveMorales
 */
public class SmsListResponse extends BaseResponse {

    @SerializedName("totalDaily")
    @Expose
    private Integer totalDaily;

    @SerializedName("alertSent")
    @Expose
    private Integer alertSent;

    @SerializedName("diffusionSent")
    @Expose
    private Integer diffusionSent;

    @SerializedName("failed")
    @Expose
    private Integer failed;

    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public Integer getTotalDaily() {
        return totalDaily;
    }

    public void setTotalDaily(Integer totalDaily) {
        this.totalDaily = totalDaily;
    }

    public Integer getAlertSent() {
        return alertSent;
    }

    public void setAlertSent(Integer alertSent) {
        this.alertSent = alertSent;
    }

    public Integer getDiffusionSent() {
        return diffusionSent;
    }

    public void setDiffusionSent(Integer diffusionSent) {
        this.diffusionSent = diffusionSent;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("number")
        @Expose
        private String number;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("timestamp")
        @Expose
        private String timestamp;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
