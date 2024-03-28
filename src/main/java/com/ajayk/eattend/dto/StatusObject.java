package com.ajayk.eattend.dto;

import java.io.Serializable;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StatusObject implements Serializable {
    private String message;
    private String[] errors;
    private int httpStatus;
    private Object data;

    // Private constructor to prevent direct instantiation
    private StatusObject(Builder builder) {
        this.message = builder.message;
        this.errors = builder.errors;
        this.httpStatus = builder.httpStatus;
        this.data = builder.data;
    }

    // Static inner class for the builder
    public static class Builder {
        private String message;
        private String[] errors;
        private int httpStatus;
        private Object data;

        public Builder() {
            // Initialize default values if needed
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setErrors(String[] errors) {
            this.errors = errors;
            return this;
        }

        public Builder setHttpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public StatusObject build() {
            return new StatusObject(this);
        }
    }

    // Getters for the fields (optional)
    public String getMessage() {
        return message;
    }

    public String[] getErrors() {
        return errors;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "StatusObject{" +
                "message='" + message + '\'' +
                ", errors=" + Arrays.toString(errors) +
                ", httpStatus=" + httpStatus +
                ", data=" + data +
                '}';
    }
}