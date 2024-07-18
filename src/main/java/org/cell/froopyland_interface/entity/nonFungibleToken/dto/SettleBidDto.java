package org.cell.froopyland_interface.entity.nonFungibleToken.dto;

import lombok.Data;

/**
 * @author yozora
 * description SettleBidDto
 * @version 1.0
 */
@Data
public class SettleBidDto {

    private Long bidId;

    private String amount;

    private String userAddress;
}
