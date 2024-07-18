package org.cell.froopyland_interface.entity.nonFungibleToken.vo;

import lombok.Data;

/**
 * @author yozora
 * description game brief vo
 * @version 1.0
 */
@Data
public class GameBriefVo {

    /**
     * FLT token price
     */
    private String tokenPrice;

    /**
     * total key minted
     */
    private String totalKeyMinted;

    /**
     * total mint fee
     */
    private String totalMintFee;

    /**
     * total prize
     */
    private String totalPrize;

    /**
     * total profits
     */
    private String totalProfits;

    /**
     * total games
     */
    private Long totalGames;
}
