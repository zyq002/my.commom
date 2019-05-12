package my.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;

@Configuration
public class MyBatisPlusConfiguration {
	
	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}
	
	@Bean
	public MybatisObjectHandler mybatisHandler() {
		return new MybatisObjectHandler();
	}
}
