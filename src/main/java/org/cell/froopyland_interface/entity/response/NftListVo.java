package org.cell.froopyland_interface.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.NonFungibleTokenVo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author yozora
 * @description nft list response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NftListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -3725831742250349628L;

    private Integer total;

    /**
     * nft list
     */
    private List<NonFungibleTokenVo> nftList;

}
