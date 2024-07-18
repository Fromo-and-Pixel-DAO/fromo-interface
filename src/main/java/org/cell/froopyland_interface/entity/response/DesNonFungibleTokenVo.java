package org.cell.froopyland_interface.entity.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yozora
 * @description des NonFungibleTokenRequest
 **/
@Data
public class DesNonFungibleTokenVo implements Serializable {


    @Serial
    private static final long serialVersionUID = -5527640267216582795L;

    /**
     * network name
     */
    private String chainName;

    /**
     * chain id
     */
    private String chainId;

    /**
     * NFT contract address
     */
    private String nftAddress;

    /**
     * NFT name
     */
    private String name;

    /**
     * NFT symbol
     */
    private String schemaName;

    /**
     * NFT symbol
     */
    private String symbol;

    /**
     * NFT description
     */
    private String description;

    /**
     * NFT image url
     */
    private String imageUrl;

    /**
     * NFT created time
     */
    private Date createdDate;
}
