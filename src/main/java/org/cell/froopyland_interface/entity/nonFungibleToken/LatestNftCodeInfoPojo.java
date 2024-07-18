package org.cell.froopyland_interface.entity.nonFungibleToken;

import lombok.Data;

/**
 * @author yozora
 * @description LatestNftCodeInfoPojo
 **/
@Data
public class LatestNftCodeInfoPojo {

    /**
     * nftCode
     */
    private String nonFungibleTokenCode;

    /**
     * login count
     */
    private String loginCount;
}
