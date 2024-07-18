package org.cell.froopyland_interface.entity.request;

import lombok.Data;

/**
 * @author yozora
 * @description NonFungibleTokenRequest
 */
@Data
public class AddNftRequest {

    /**
     * nft address
     */
    private String nftAddress;

    /**
     * NFT token ids
     */
    private String[] nftTokenIds;
}
