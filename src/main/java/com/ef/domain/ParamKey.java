package com.ef.domain;

/**
 * Created by gardiary on 02/04/18.
 */
public enum ParamKey {
    ACCESSLOG("--accesslog"), STARTDATE("--startDate"), DURATION("--duration"), THRESHOLD("--threshold"), IP("--ip");

    String key;

    ParamKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
