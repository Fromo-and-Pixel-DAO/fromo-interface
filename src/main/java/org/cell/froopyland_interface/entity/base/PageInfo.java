package org.cell.froopyland_interface.entity.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * Description page info
 **/
@Data
public class PageInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2840351558316760842L;

    /**
     * current page
     */
    private Integer pageNum = 1;

    /**
     * page size
     */
    private Integer pageSize = 10;

    /**
     * total page
     */
    private Integer totalPage = 0;

    /**
     * total count
     */
    private Integer totalCount = 0;

    /**
     * start index
     */
    protected Integer startIndex = -1;

     public boolean hasData(int totalCount) {
        this.checkQueryPageInfo();
        this.totalCount = totalCount;
        this.totalPage = (this.totalCount + this.pageSize - 1) / this.pageSize;
        return startIndex < totalCount;
    }

    /**
     * description: check query page info
     *
     * @author yozora
     */
    protected void checkQueryPageInfo() {
        if (this.pageNum <= 0) {
            throw new IllegalArgumentException("pageNum(" + this.pageNum + ")must more than 0!");
        } else if (this.pageSize <= 0) {
            throw new IllegalArgumentException("pageSize(" + this.pageSize + ")must more than 0!");
        } else {
            this.startIndex = (this.pageNum - 1) * this.pageSize;
            if (this.startIndex < 0) {
                this.startIndex = 0;
            }
        }
    }

}
