package org.cell.froopyland_interface.entity.user.vo;

import lombok.Data;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftVo;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * description my historical dividends vo
 * @version 1.0
 */
@Data
public class MyHistoricalDividendsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 7420306744748625546L;

    /**
     * game nft
     */
    private GameNftVo gameNft;

    private String type;

    /**
     * amount
     */
    private String amount;

    /**
     * 0 unclaimed 1 claimed
     */
    private String status;

}
