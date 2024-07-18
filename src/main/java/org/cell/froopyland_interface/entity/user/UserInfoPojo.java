package org.cell.froopyland_interface.entity.user;

import lombok.Data;

/**
 * @author yozora
 * Description user info pojo
 **/
@Data
public class UserInfoPojo {

    /**
     * username
     */
    private String username;

    /**
     * email
     */
    private String email;

    /**
     * password
     */
    private String password;

    /**
     * address
     */
    private String address;

    /**
     * chain ID
     */
    private String chainId;

    /**
     * ip address
     */
    private String ipAddress;

}
