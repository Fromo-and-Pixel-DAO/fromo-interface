package org.cell.froopyland_interface.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author yozora
 * @description NonFungibleTokenRequest
 **/
@Data
public class BaseNonFungibleTokenRequest {

    /**
     * NonFungibleToken code
     */
    @NotNull(message = "NFT info is required")
    private String nftCode;

    /**
     * chainId
     */
    private String chainId = "1";

    /**
     * chain name
     */
    private String chainName = "ethereum";

    /**
     * user address
     */
    private String userAddress;

    /**
     * signature
     */
    private String signature;

    /**
     * timestamp
     */
    private String timestamp;

    /**
     * target
     */
    private String target;

    /**
     * NFT address
     */
    private String nftAddress;

    /**
     * NFT token id
     */
    private String nftTokenId;

    /**
     * NFT token list
     */
    private List<AddNftRequest> addNftRequest;

}
