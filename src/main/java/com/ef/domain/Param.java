package com.ef.domain;

/**
 * Created by gardiary on 02/04/18.
 */
public class Param {
    private ParamKey key;
    private Object value;

    public ParamKey getKey() {
        return key;
    }

    public void setKey(ParamKey key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ']';
    }
}
