package my.com.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RemotePage<T> implements Serializable {

	private static final long serialVersionUID = 5756345873158652812L;

	private List<T> data;

	private PageInfo page;

	private Date startDate;
	
	private Date endDate;
	
	public RemotePage(List<T> data, PageInfo page) {
		this.data = data;
		this.page = page;
	}

	public RemotePage() {

	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "RemotePage [data=" + data + ", page=" + page + "]";
	}

}
