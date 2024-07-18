package org.cell.froopyland_interface.entity.user.dto;

import lombok.Data;

/**
 * @author yozora
 * description user game nft vo
 * @version 1.0
 */
@Data
public class UserGameNftDto {

    /**
     * game id
     */
    private Long gameId;

    /**
     * NFT
     */
    private String name;

    /**
     * NFT image url
     */
    private String imageUrl;

    /**
     * NFT id
     */
    private String tokenId;

    /**
     * transaction hash
     */
    private String tx;

    /**
     * amount
     */
    private String amount;

}
