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
public class UserNftListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 5888155862299653096L;

    private String next;

    /**
     * nft list
     */
    private List<NonFungibleTokenVo> nftList;

}
