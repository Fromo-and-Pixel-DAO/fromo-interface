package org.cell.froopyland_interface.entity.nonFungibleToken.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yozora
 * description non fungible token vo
 * @version 1.0
 */
@Data
public class NonFungibleTokenVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3823984692742835629L;

    /**
     * game id
     */
    private Long gameId;

    /**
     * NFT name
     */
    private String name;

    /**
     * NFT chain name
     */
    private String chainName;

    /**
     * NFT user address
     */
    private String userAddress;

    /**
     * NFT address
     */
    private String nftAddress;

    /**
     * last address
     */
    private String lastAddress;

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
     * NFT metadata url
     */
    private String tokenMetadataUrl;

    /**
     * open sea url
     */
    private String openSeaUrl;

    /**
     * NFT key price
     */
    private String keyPrice;

    /**
     * NFT key price
     */
    private String finalPrice;

    /**
     * NFT status
     */
    private Integer status;

    /**
     * sales amount
     */
    private String salesAmount;

    /**
     * total key minted
     */
    private String totalKeyMinted;

    /**
     * total mint fee
     */
    private String totalMintFee;

    /**
     * NFT bidders count
     */
    private Integer biddersCount;

    /**
     * NFT auctions count
     */
    private Integer auctionsCount;
    /**
     * NFT start time
     */
    private Date startTime;

    /**
     * NFT end time
     */
    private Date endTime;

    /**
     * NFT start time
     */
    private Long startTimestamp;

    /**
     * NFT end time
     */
    private Long endTimestamp;


}
