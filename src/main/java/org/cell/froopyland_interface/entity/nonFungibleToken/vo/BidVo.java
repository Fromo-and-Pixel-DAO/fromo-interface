package org.cell.froopyland_interface.entity.nonFungibleToken.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * description
 * @version 1.0
 */
@Data
public class BidVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4942210304827802568L;

    private String userAddress;

    private String amount;

}
