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
public class UserJoinLogDto {

    private Long gameId;

    private String userAddress;

    private BigInteger amount;

    /**
     * tx hash
     */
    private String transactionHash;
}
