package com.ohohmiao.web.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ohohmiao.framework.web.util.PlatExceptionUtil;
import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.security.enums.AuthDeviceEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.List;

/**
 * 核心配置
 *
 * @author ohohmiao
 * @date 2023/4/5 19:08
 */
@Configuration
@EnableSwagger2WebMvc
public class PlatConfigurer implements WebMvcConfigurer {

    /**
     * 无需登录的接口地址集合
     */
    private static final String[] NOLOGIN_PATH = {
        /* 主入口 */
        "/",
        "/favicon.ico",

        /* swagger */
        "/doc.html",
        "/webjars/**",
        "/swagger-resources/**",
        "/v2/api-docs",

        /* 验证码 */
        "/captchaImage",

        /* 登录相关 */
        "/auth/login"

    };

    /**
     * PC端要排除的，相当于APP端要认证的
     */
    private static final String[] APPUSER_PERMISSION_PATH = {
        /* 预留app端  */
        "/auth/app/**"
    };

    @Bean("stpLogic")
    public StpLogic getStpLogic() {
        return new StpLogic(AuthDeviceEnum.PC.getType());
    }

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 指定拦截路由
                .addInclude("/**")

                // 设置鉴权的接口
                .setAuth(obj -> {
                    // pc端的接口校验pc端登录
                    SaRouter.match("/**")
                            // 排除无需登录接口
                            .notMatch(CollectionUtil.newArrayList(NOLOGIN_PATH))
                            // 排除app端认证接口
                            .notMatch(CollectionUtil.newArrayList(APPUSER_PERMISSION_PATH))
                            // 校验pc端登录
                            .check(r1 -> StpPCUtil.checkLogin());
                })

                // 在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    // ---------- 设置安全相关响应头 ----------
                    SaHolder.getResponse()

                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            // OPTIONS预检请求，不做处理
                            .free(r -> {})
                            .back();
                })

                // 异常处理
                .setError(e -> {
                    // 由于过滤器中抛出的异常不进入全局异常处理，所以必须提供[异常处理函数]来处理[认证函数]里抛出的异常
                    // 在[异常处理函数]里的返回值，将作为字符串输出到前端，此处统一转为JSON输出前端
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    return PlatExceptionUtil.getCommonResult((Exception) e);
                });

    }

    /**
     * 重写Sa-Token默认的注解处理器
     */
//    @Autowired
//    public void rewriteSaStrategy() {
//        // 增加注解合并功能
//        SaStrategy.me.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
//    }

    /**
     * 自定义权限验证接口扩展
     */
    @Component
    public static class StpInterfaceImpl implements StpInterface {

        /**
         * 返回一个账号所拥有的权限码集合
         * @param loginId
         * @param loginType
         * @return
         */
        @Override
        public List<String> getPermissionList(Object loginId, String loginType) {
            if (AuthDeviceEnum.PC.getType().equals(loginType)) {
                return CollectionUtil.newArrayList(StpPCUtil.getLoginUser().getGrantedResUrls());
            } else {
                //TODO 其他登录体系
                return null;
            }
        }

        /**
         * 返回一个账号所拥有的角色标识集合
         * @param loginId
         * @param loginType
         * @return
         */
        @Override
        public List<String> getRoleList(Object loginId, String loginType) {
            if (AuthDeviceEnum.PC.getType().equals(loginType)) {
                StpLoginUser loginUser = StpPCUtil.getLoginUser();
                if(loginUser.isSuperAdmin()){
                    return CollectionUtil.newArrayList(AuthConstEnum.SUPERADMIN_ACCOUNT.getValue());
                }else{
                    return CollectionUtil.newArrayList(StpPCUtil.getLoginUser().getGrantedRoleIds());
                }
            } else {
                //TODO 其他登录体系
                return null;
            }
        }
    }

    /**
     * 添加静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 跨域请求配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600L);
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 数据库id选择器，用于Mapper.xml中
     * MyBatis可以根据不同的数据库厂商执行不同的语句
     */
//    @Component
//    public static class CustomDbIdProvider implements DatabaseIdProvider {
//        @Override
//        public String getDatabaseId(DataSource dataSource) throws SQLException {
//            String url = dataSource.getConnection().getMetaData().getURL();
//            if (url.contains("oracle")) {
//                return "oracle";
//            } else if (url.contains("postgresql")) {
//                return "pgsql";
//            } else if (url.contains("mysql")) {
//                return "mysql";
//            } else if (url.contains("dm")) {
//                return "dm";
//            } else if (url.contains("kingbase")) {
//                return "kingbase";
//            }  else {
//                return "mysql";
//            }
//        }
//    }

    @Resource
    private RedisConnectionFactory factory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    @Bean(value = "platSwaggerDocket")
    public Docket platSwaggerDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(new ApiInfoBuilder()
                        .title("通用模块")
                        .version("1.0.0")
                        .build())
                .groupName("通用模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ohohmiao.web"))
                .paths(PathSelectors.any())
                .build();
    }

}
