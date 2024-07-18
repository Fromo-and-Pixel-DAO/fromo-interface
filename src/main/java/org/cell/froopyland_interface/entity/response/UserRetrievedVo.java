package org.cell.froopyland_interface.entity.response;

import lombok.Data;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftVo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author yozora
 * description user retrieved vo
 * @version 1.0
 */
@Data
public class UserRetrievedVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -2830290852473520282L;

    private Integer total;

    private List<GameNftVo> gameNftList;
}
