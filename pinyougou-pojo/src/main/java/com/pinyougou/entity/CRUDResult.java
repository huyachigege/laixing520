package com.pinyougou.entity;

import java.io.Serializable;

public class CRUDResult implements Serializable {
    private boolean success;
    private String message;

    public CRUDResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}