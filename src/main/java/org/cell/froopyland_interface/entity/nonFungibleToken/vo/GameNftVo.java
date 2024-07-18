package org.cell.froopyland_interface.entity.nonFungibleToken.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * description game NFT vo
 * @version 1.0
 */
@Data
public class GameNftVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -89168938359190834L;

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
}
