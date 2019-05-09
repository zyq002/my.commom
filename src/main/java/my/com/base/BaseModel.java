package my.com.base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.baomidou.mybatisplus.annotation.TableField;

import my.com.Util.DateUtil;

public abstract class BaseModel {

	protected LocalDateTime createTime;

	protected LocalDateTime updateTime;
	@TableField(exist = false)
	protected String createTimeStr;
	@TableField(exist = false)
	protected String updateTimeStr;

	protected String getCreateTimeStr() {
		if (this.getCreateTime() == null)
			return null;
		this.createTimeStr = this.getCreateTime().format(DateTimeFormatter.ofPattern(DateUtil.FORMAT_LONG));
		return createTimeStr;
	}

	protected String getUpdateTimeStr() {
		if (this.getUpdateTime() == null)
			return null;
		this.updateTimeStr = this.getUpdateTime().format(DateTimeFormatter.ofPattern(DateUtil.FORMAT_LONG));
		return updateTimeStr;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

}
