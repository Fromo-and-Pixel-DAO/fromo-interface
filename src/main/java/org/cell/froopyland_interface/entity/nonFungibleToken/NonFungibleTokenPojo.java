package org.cell.froopyland_interface.entity.nonFungibleToken;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yozora
 * @description NonFungibleTokenPojo
 **/
@Data
public class NonFungibleTokenPojo {

    /**
     * game id
     */
    private Long gameId;

    /**
     * NFT name
     */
    private String name;

    /**
     * NFT symbol
     */
    private String symbol;

    /**
     * NFT chain name
     */
    private String chainName;

    /**
     * NFT chain id
     */
    private String chainId;

    /**
     * NFT code
     */
    private String nftCode;

    /**
     * owner address
     */
    private String userAddress;

    /**
     * NFT address
     */
    private String nftAddress;

    /**
     * NFT creator
     */
    private String creator;

    /**
     * NFT last address
     */
    private String lastAddress;

    /**
     * token standard
     */
    private String tokenStandard;

    /**
     * NFT id
     */
    private String tokenId;

    /**
     * image url
     */
    private String imageUrl;

    /**
     * animation url
     */
    private String animationUrl;

    /**
     * from platform
     */
    private String fromPlatform;
    /**
     * NFT metadata url
     */
    private String tokenMetadataUrl;

    /**
     * open sea url
     */
    private String openSeaUrl;

    /**
     * NFT bio
     */
    private String bio;

    /**
     * total key minted
     */
    private String totalKeyMinted;

    /**
     * NFT key price
     */
    private String keyPrice;

    /**
     * NFT sales amount
     */
    private String salesAmount;

    /**
     * NFT joined amount
     */
    private String joinedAmount;

    /**
     * NFT start time
     */
    private Date startTime;

    /**
     * NFT end time
     */
    private Date endTime;

    /**
     * NFT status
     */
    private Integer status;


}
