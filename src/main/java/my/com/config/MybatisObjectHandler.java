package my.com.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

public class MybatisObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		setFieldValByName("createTime", LocalDateTime.now(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
	}

}
