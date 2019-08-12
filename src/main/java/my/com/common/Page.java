package my.com.common;

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;

public class Page<T> implements IPage<T> {
	private static final long serialVersionUID = 1L;

	private List<T> records = Collections.emptyList();
	/* 总数 */
	private Long total = 0L;

	/* 当前页 */
	private Long current = 1L;

	/* 每页显示条数，默认 10 */
	private Long size = 10L;

	@Override
	public List<T> getRecords() {
		return records;
	}

	@Override
	public IPage<T> setRecords(List<T> records) {
		this.records = records;
		return this;
	}

	@Override
	public long getTotal() {
		return total;
	}

	@Override
	public IPage<T> setTotal(long total) {
		this.total = total;
		return this;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public IPage<T> setSize(long size) {
		this.size = size;
		return this;
	}

	@Override
	public long getCurrent() {
		if (current <= 0) {
			current = 1L;
		} else if (current != null) {
			Long total = getTotal();
			if (total == 0) {
				current = 1L;
			} else if (current > total) {
				current = total;
			}
		}
		return current;
	}

	@Override
	public IPage<T> setCurrent(long current) {
		this.current = current;
		return this;
	}

	@Override
	public List<OrderItem> orders() {
		// TODO Auto-generated method stub
		return null;
	}

}
