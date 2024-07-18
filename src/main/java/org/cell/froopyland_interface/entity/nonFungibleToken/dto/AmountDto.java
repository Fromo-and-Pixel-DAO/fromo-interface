package org.cell.froopyland_interface.entity.nonFungibleToken.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yozora
 * description amount dto
 * @version 1.0
 */
@Data
@ToString
public class AmountDto {

    /**
     * game id
     */
    private Long gameId;

    /**
     * amount
     */
    private BigDecimal amount;

    /**
     * type
     */
    private String type;

    /**
     * transaction hash
     */
    private String tx;

    /**
     * create time
     */
    private Date createTime;
}
