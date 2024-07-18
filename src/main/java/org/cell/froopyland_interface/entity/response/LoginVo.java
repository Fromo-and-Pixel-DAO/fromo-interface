package org.cell.froopyland_interface.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * @description login vo
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4821775338178027609L;
    /**
     * user address
     */
    private String userAddress;

    /**
     * access token
     */
    private String t;

    /**
     * refresh token
     */
    private String r;
}
