package org.cell.froopyland_interface.entity.nonFungibleToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yozora
 * @description NonFungibleTokenCodeDto
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonFungibleTokenCodePojo {

    /**
     * chain name
     */
    private String networkName;

    /**
     * 链 ID
     */
    private String chainId;

    /**
     * NFT ID
     */
    private String nonFungibleTokenId;

    /**
     * NFT Code
     */
    private String nonFungibleTokenCode;

    /**
     * NFT address
     */
    private String nonFungibleTokenAddress;


}
