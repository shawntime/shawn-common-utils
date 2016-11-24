package com.shawntime.common.web;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by zhouxiaoming on 2015/8/31.
 */
public class ProtocolBody<T> {

    private static final Logger log = Logger.getLogger(ProtocolBody.class);

    private int rowcount;

    private int pagecount;

    private int pageindex;

    private Collection<T> list;

    public ProtocolBody(){
        // default constract
    }
    public ProtocolBody(Collection list, int pageCount, int pageIndex, int rowCount) {
        this.list = list;
        this.rowcount = rowCount;
        this.pagecount = pageCount;
        this.pageindex = pageIndex;
    }

    public int getRowcount() {
        return rowcount;
    }

    public void setRowcount(int rowcount) {
        this.rowcount = rowcount;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public Collection<T> getList() {
        //和调用方约定后，过滤list中的null元素
        if (list != null && list.contains(null)) {
            list = list.stream().filter(l -> l != null).collect(Collectors.toList());
            log.fatal("ProtocolBody.list中含null元素");
        }
        return list;
    }

    public void setList(Collection<T> list) {
        this.list = list;
    }
}
