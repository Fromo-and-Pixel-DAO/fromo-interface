package org.cell.froopyland_interface.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yozora
 * @description UserLoginInfoDto
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginInfoDto {

    /**
     * user address
     */
    private String address;

    /**
     * ip address
     */
    private String ipAddress;

    /**
     * signature
     */
    private String signature;

    /**
     * timestamp
     */
    private String timestamp;

}
