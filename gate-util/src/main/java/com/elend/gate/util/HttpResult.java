package com.elend.gate.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class HttpResult {
    /**http返回码*/
    private int httpResponseCode;
    /**http body*/
    private String httpBody;
    public int getHttpResponseCode() {
        return httpResponseCode;
    }
    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }
    public String getHttpBody() {
        return httpBody;
    }
    public void setHttpBody(String httpBody) {
        this.httpBody = httpBody;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
