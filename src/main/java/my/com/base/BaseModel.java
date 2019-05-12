package my.com.base;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
public abstract class BaseModel {
	protected Long id;
	@TableField(value = "createTime", fill = FieldFill.INSERT)
	protected LocalDateTime  createTime;
	protected String createCode;
	@TableField(value = "updateTime", fill = FieldFill.UPDATE)
	protected LocalDateTime updateTime;
	protected String updateCode;
	@TableLogic
	protected int isDel;
}
