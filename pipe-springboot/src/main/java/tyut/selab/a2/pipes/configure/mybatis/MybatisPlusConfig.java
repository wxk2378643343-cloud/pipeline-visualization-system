package tyut.selab.a2.pipes.configure.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("tyut.selab.a2.pipes.mapper")
public class MybatisPlusConfig {
}
