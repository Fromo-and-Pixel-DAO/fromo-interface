package org.cell.froopyland_interface.entity.nonFungibleToken;

import lombok.Data;

import java.util.Date;

/**
 * @author yozora
 * @description NonFungibleTokenPojo
 **/
@Data
public class NonFungibleTokenDesPojo {

    /**
     * id
     */
    private String id;

    /**
     * NFT address
     */
    private String nftAddress;

    /**
     * NFT name
     */
    private String name;

    /**
     * NFT schema name
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
     * NFT url
     */
    private String imageUrl;

    /**
     * NFT create date
     */
    private Date createdDate;

}
