package org.seckill.dto;

/**
 * @Author: hjg
 * @Date: Create in 2018/2/5 11:09
 * @Description: 分页相关参数
 */
public class Page {

    /**
     * 总条数
     */
    private int totalNumber;
    /**
     * 每页条数
     */
    private int pageNumber = 4;
    /**
     * 当前第几页
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 数据库查询参数
     */
    private int dbOffset;
    /**
     * 数据库查询参数
     */
    private int dbLimit;

    /**
     * 计算相关属性.根据totalNumber,pageNumber,currentPage
     */
    public void count() {
        //计算总页数
        totalPage = totalNumber <= 0 ? 1 : ((totalNumber - 1) / pageNumber) + 1;
        //调整当前页数
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }
        //计算数据库参数
        dbOffset = (currentPage - 1) * pageNumber;
        dbLimit = pageNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getDbOffset() {
        return dbOffset;
    }

    public void setDbOffset(int dbOffset) {
        this.dbOffset = dbOffset;
    }

    public int getDbLimit() {
        return dbLimit;
    }

    public void setDbLimit(int dbLimit) {
        this.dbLimit = dbLimit;
    }

    @Override
    public String toString() {
        return "Page{" +
                "totalNumber=" + totalNumber +
                ", pageNumber=" + pageNumber +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", dbOffset=" + dbOffset +
                ", dbLimit=" + dbLimit +
                '}';
    }
}
