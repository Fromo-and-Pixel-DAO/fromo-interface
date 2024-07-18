package org.cell.froopyland_interface.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cell.froopyland_interface.entity.base.PageInfo;

import java.io.Serial;

/**
 * @author yozora
 * @description NonFungibleTokenRequest
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class DesNonFungibleTokenRequest extends PageInfo {

    @Serial
    private static final long serialVersionUID = 3620626291760495332L;

    /**
     * user address
     */
    private String userAddress;

    /**
     * chain id list
     */
    private String[] chainIds;

    /**
     * NonFungibleToken code
     */
    private String nftCode;


}


