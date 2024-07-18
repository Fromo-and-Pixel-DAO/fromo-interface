package org.cell.froopyland_interface.entity.constants;

/**
 * @author yozora
 * description Game constant
 * @version 1.0
 */
public interface GameConstant {

    /**
     * auction status upcoming
     */
    int UPCOMING = 0;

    /**
     * auction status ongoing
     */
    int ONGOING = 1;

    /**
     * auction status finished
     */
    int FINISHED = 2;

    /**
     * bid status not started
     */
    int NOT_STARTED = 0;

    /**
     * bid status biding
     */
    int BIDING = 1;

    /**
     * bid status staking
     */
    int STAKING = 2;

    /**
     * bid status finished
     */
    int FINISHED_BID = 3;

    /**
     * user status claim key
     */
    int CLAIM_KEY = 0;

    /**
     * user status last claim
     */
    int LAST_CLAIM = 1;

    /**
     * user status nft sale
     */
    int NFT_SALE = 2;
}
