package org.cell.froopyland_interface.entity.user.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yozora
 * description my profit vo
 * @version 1.0
 */
@Data
public class MyProfitVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -4865431205913431455L;

    /**
     * fi price
     */
    private String flPrice;

    /**
     * keys
     */
    private String keys = "0";

    /**
     * locked keys
     */
    private String lockedKeys = "0";

    /**
     * fl tokens
     */
    private String flTokens = "0";

    /**
     * locked fl tokens
     */
    private String lockedFlTokens = "0";

    /**
     * fl tokens
     */
    private String withdrawalAmountTokens = "0";

    /**
     * key dividends
     */
    private String keyDividends;

    /**
     * converted game ids
     */
    private List<Long> convertedGameIds;

    /**
     * unconverted game ids
     */
    private Set<Long> unconvertedGameIds;

    /**
     * can convert
     */
    private int canConvert;

    /**
     * unclaimed key dividends
     */
    private String unclaimedKeyDividends;

    /**
     * unclaimed key game ids
     */
    private List<Long> unclaimedKeyGameIds;

    /**
     * final Winner Price
     */
    private String finalWinPrice;

    /**
     * unclaimed final winner price
     */
    private String unclaimedFinalWinPrice;

    /**
     * unclaimed final winner game ids
     */
    private List<Long> unclaimedFinalWinnerGameIds;

    /**
     * nft dividends
     */
    private String nftDividends;

    /**
     * locked nft dividends
     */
    private String lockedNftDividends;

    /**
     * unclaimed nft dividends
     */
    private String unclaimedNftDividends;

    /**
     * unclaimed nft game ids
     */
    private List<Long> unclaimedNftGameIds;

}
