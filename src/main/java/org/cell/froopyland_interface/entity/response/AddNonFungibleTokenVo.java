package org.cell.froopyland_interface.entity.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * @description
 */
@Data
public class AddNonFungibleTokenVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4064933494175491098L;

    /**
     * NFT address
     */
    private String nftAddress;

    /**
     * NFT token id
     */
    private String tokenId;

    /**
     * add result
     */
    private String result;
}
