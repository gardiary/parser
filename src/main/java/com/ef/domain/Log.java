package com.ef.domain;

import java.util.Date;

/**
 * Created by gardiary on 03/04/18.
 */
public class Log {
    private Integer id;
    private Date dateTime;
    private String ip;
    private String request;
    private Integer status;
    private String userAgent;
    private String jobId;   // jobId only to separate different process

    public Log() {
    }

    public Log(Date dateTime, String ip, String request, Integer status, String userAgent, String jobId) {
        this.dateTime = dateTime;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
        this.jobId = jobId;
    }

    public Log(Integer id, Date dateTime, String ip, String request, Integer status, String userAgent, String jobId) {
        this.id = id;
        this.dateTime = dateTime;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
        this.jobId = jobId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "[" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", ip='" + ip + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", userAgent='" + userAgent + '\'' +
                ", jobId='" + jobId + '\'' +
                ']';
    }
}
