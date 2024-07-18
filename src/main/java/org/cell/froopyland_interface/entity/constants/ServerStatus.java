package org.cell.froopyland_interface.entity.constants;

/**
 * @author yozora
 * @description ServerStatus
 **/
public interface ServerStatus {

    String SUCCESS = "200";

    String INVALID_PARAMETER = "400";

    String UNAUTHORIZED = "401";

    String PERMISSION_DENIED = "403";

    String NOT_FOUND = "404";

    String ABORTED = "409";

    String RESOURCE_EXHAUSTED = "429";

    String CANCELLED = "499";

    String INTERNAL_SERVER_ERROR = "500";

}
