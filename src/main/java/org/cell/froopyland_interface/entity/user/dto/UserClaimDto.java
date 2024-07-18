package org.cell.froopyland_interface.entity.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author yozora
 * description User claim dto
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserClaimDto {

    /**
     * game id
     */
    private Long gameId;

    /**
     * user address
     */
    private String address;

    /**
     * amount
     */
    private BigInteger amount;
}
