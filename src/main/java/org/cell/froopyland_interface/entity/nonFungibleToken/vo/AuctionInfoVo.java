package org.cell.froopyland_interface.entity.nonFungibleToken.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * description : auction info vo
 * @version 1.0
 */
@Data
public class AuctionInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3351106651776104517L;

    /**
     * bid id
     */
    private Long bidId;

    /**
     * status (0upcoming 1bid 2staking 3playing)
     */
    private Integer status;

    /**
     * bid start time
     */
    private Long startTimestamp;

    /**
     * winner address
     */
    private String bidWinnerAddress;

    /**
     * NFT highest bid
     */
    private String highestBid;

    /**
     * NFT bidders count
     */
    private Integer biddersCount;
}
