package com.qww.mongologger.core.entity.interfaces;

import javax.servlet.http.HttpServletRequest;

public interface WebLogInterface extends ExecLogInterface {
    public void setDataFromRequest(HttpServletRequest request);
}
