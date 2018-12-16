package com.ef.domain;

import java.util.Date;

/**
 * Created by gardiary on 03/04/18.
 */
public class BlockedIP {
    private Integer id;
    private String ip;
    private Integer count;
    private Date startDate;
    private Date endDate;
    private Integer threshold;
    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "[" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", count=" + count +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", threshold=" + threshold +
                ", reason='" + reason + '\'' +
                ']';
    }
}
