package org.cell.froopyland_interface.entity.request;

import lombok.Data;

/**
 * @author yozora
 * description user nft request
 * @version 1.0
 */
@Data
public class UserNftRequest {

    /**
     * owner address
     */
    private String userAddress;

    /**
     * last address
     */
    private String lastAddress;

    /**
     * most key address
     */
    private String mostKeyAddress;

    /**
     * status
     */
    private Integer status;
}
