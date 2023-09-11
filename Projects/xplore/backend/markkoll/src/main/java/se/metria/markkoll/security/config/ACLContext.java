package se.metria.markkoll.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.metria.markkoll.security.MarkkollMethodSecurityExpressionHandler;
import se.metria.markkoll.security.MarkkollRole;
import se.metria.markkoll.security.MkPermission;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class ACLContext {

    @Autowired
    DataSource dataSource;

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority(MarkkollRole.GLOBAL_ADMIN));
    }

    @Bean
    public PermissionEvaluator aclPermissionEvaluator() {
        var permissionEvaluator = new AclPermissionEvaluator(mutableAclService());
        permissionEvaluator.setPermissionFactory(permissionFactory());
        return permissionEvaluator;
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new MarkkollMethodSecurityExpressionHandler();

        var permissionEvaluator = aclPermissionEvaluator();

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(mutableAclService()));
        return expressionHandler;
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        var lookupStrategy = new BasicLookupStrategy(
            dataSource,
            aclCache(),
            aclAuthorizationStrategy(),
            new ConsoleAuditLogger()
        );
        lookupStrategy.setAclClassIdSupported(true);
        lookupStrategy.setPermissionFactory(permissionFactory());
        return lookupStrategy;
    }

    @Bean
    public JdbcMutableAclService mutableAclService() {
        var aclDataSource = dataSource;

        var service = new JdbcMutableAclService(aclDataSource, lookupStrategy(), aclCache());
        service.setAclClassIdSupported(true);
        service.setSidIdentityQuery("SELECT currval('acl_sid_id_seq')");
        service.setClassIdentityQuery("SELECT currval('acl_class_id_seq')");

        return service;
    }

    @Bean
    public PermissionFactory permissionFactory() {
        return new DefaultPermissionFactory(MkPermission.class);
    }
}
