package my.com.common;

import java.io.Serializable;
 
public class PageInfo implements Serializable {
	private static final long serialVersionUID = -6830864636737268480L;
	
	private static final Integer DEAFULT_PAGE_SIZE = 10; //每页默认记录数
	private static final Integer MAX_PAGE_SIZE = 10; //每页默认记录数
 

    /**
     * 分页时当前查询哪一页
     */
	 
    private Integer currentPage;
	
    /**
     * 分页时每页记录数
     */
 
    protected Integer pageSize;
	
	//记录总数,这个是输出
 
	private Integer recordCount; 

	// 1 asc 2 desc 仅仅在soa里面使用
	private transient Integer dir;
	
	private transient String orderByFiled;
	
    public Integer getPageSize() {
        if(pageSize == null || pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        } else if(pageSize < 1) {
            pageSize = DEAFULT_PAGE_SIZE;
        }

        return pageSize;
    }

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	
    public Integer getCurrentPage() {
    	if(currentPage == null || currentPage.intValue() <= 0){
    		currentPage=1;
    	}else if(recordCount!=null ){
    		Integer total=getTotalPage();
    		if(total!=null){
    			if(total==0){
        			currentPage=1;
        		}else if(currentPage>total){
        			currentPage =total;
        		}
    		}
    	}
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
    	return (getCurrentPage() - 1 ) * getPageSize()  ;
    }

	public Integer getEndRow() {
		return getStartRow() + getPageSize() - 1;
	}
    
 
	public Integer getRecordCount() {
		return recordCount;
	}

	public Integer getDir() {
		return dir;
	}

	public void setDir(Integer dir) {
		this.dir = dir;
	}
	
	public String getOrderByFiled() {
		String filed = "id";
		if (orderByFiled != null) {
			filed = orderByFiled;
		}
		if (dir != null && dir == 2) {
			filed = filed + " desc";
		}
		return filed;
	}
	
	public void setOrderByFiled(String orderByFiled) {
		this.orderByFiled = orderByFiled;
	}
	
	public Integer getTotalPage(){
		if(recordCount==null)
			return null;
		return recordCount%getPageSize()>0?recordCount/getPageSize()+1:recordCount/getPageSize();
	}
	
	public String getLimit(){
		int cPage=getCurrentPage();
    	int tPage=getTotalPage();
    	if(tPage>0){
    		int starRow;
        	if(cPage>tPage){
        		currentPage=tPage;
        		starRow= (tPage-1)*getPageSize();
        	}else{
        		starRow= (cPage - 1 ) * getPageSize()  ;
        	}
    		return "LIMIT "+starRow+","+getPageSize();
    	}
    	return null;
    	
	}
}
