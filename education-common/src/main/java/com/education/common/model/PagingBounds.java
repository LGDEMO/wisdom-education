package com.education.common.model;

import org.apache.ibatis.session.RowBounds;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/10 22:21
 */
public class PagingBounds extends RowBounds {

    private int offset;
    private int limit;

    public PagingBounds() {
        super();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
