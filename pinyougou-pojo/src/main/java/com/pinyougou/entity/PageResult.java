package com.pinyougou.entity;

import java.io.Serializable;
import java.util.List;
/**
 * Description: 分页结果类
 * @author:
 * @date: 2018/11/16
 * @param:
 * @return:
 */
public class PageResult<T> implements Serializable {
    private long total;//总记录数
    private List<T> rows;//当前页记录

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
