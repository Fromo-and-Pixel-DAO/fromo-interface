package org.cell.froopyland_interface.entity.base;


import org.cell.froopyland_interface.entity.constants.ServerStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Created by yozora
 */
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 8732277221304105593L;

    private String msg;

    private String code;

    private T data;


    public ApiResponse() {
        this.code = ServerStatus.SUCCESS;
        this.msg = "success";
    }

    public ApiResponse(String code, String msg) {
        this(code, msg, null);
    }

    public ApiResponse(T data) {
        this.code = ServerStatus.SUCCESS;
        this.data = data;
        this.msg = "success";
    }

    public ApiResponse(String code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        if (data != null) this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO [msg=" + msg + ", code=" + code + ", data=" + data + "]";
    }
}
