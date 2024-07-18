package org.cell.froopyland_interface.entity.response;

import lombok.Data;
import org.cell.froopyland_interface.entity.user.vo.MyHistoricalDividendsVo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author yozora
 * description
 * @version 1.0
 */
@Data
public class UserDividendsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 5642595707427926283L;

    private Integer total;

    private List<MyHistoricalDividendsVo> historicalDividendsList;
}
