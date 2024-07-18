package org.cell.froopyland_interface.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cell.froopyland_interface.entity.base.PageInfo;

import java.io.Serial;
import java.util.Date;

/**
 * @author yozora
 * @description user NFT request
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserNonFungibleTokenRequest extends PageInfo {

    @Serial
    private static final long serialVersionUID = 3908223760342396715L;

    /**
     * network name
     */
    @NotBlank(message = "network name is required")
    private String chainName;

    /**
     * chain id
     */
    @NotBlank(message = "chain ids is required")
    private String[] chainIds;

    /**
     * user address
     */
    private String userAddress;

    /**
     * nft contract address
     */
    private String[] nftAddresses;

    /**
     * nft name
     */
    private String nftName;

    /**
     * time type (0: 1 month, 1: 7 day, 2: 3 day)
     */
    private Integer timeType;

    /**
     * latest time
     */
    private Date startTime;

    /**
     * key word
     */
    private String keyWord;

    /**
     * query contract address
     */
    private String queryAddress;

    /**
     * query token id
     */
    private String queryToken;

    /**
     * query name or symbol
     */
    private String queryNameOrDescription;
}
