package cn.com.flaginfo.bean;

/**
 * pojo
 */
public class BasePoJo {

    private String id;

    private Integer pageSize = 10;

    private Integer pageNo = 0;

    private Pager page;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Pager getPage() {
        page = new Pager();
        page.setPageNumber(pageNo);
        page.setPageSize(pageSize);
        return page;
    }

    public void setPage(Pager page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
