package org.cell.froopyland_interface.entity.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author yozora
 * description user join log dto
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBidLogDto {

    private Long bidId;

    private BigInteger amount;

    private String userAddress;
}
