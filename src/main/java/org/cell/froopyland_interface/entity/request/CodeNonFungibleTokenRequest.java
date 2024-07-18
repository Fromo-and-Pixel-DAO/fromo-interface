package org.cell.froopyland_interface.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yozora
 * @description CodeNonFungibleTokenRequest
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeNonFungibleTokenRequest {

    /**
     * chain name
     */
    private String networkName;

    /**
     * chain ID
     */
    private String chainId;

    /**
     * NFT ID
     */
    private String nonFungibleTokenId;

    /**
     * NFT address
     */
    private String nonFungibleTokenAddress;

}
