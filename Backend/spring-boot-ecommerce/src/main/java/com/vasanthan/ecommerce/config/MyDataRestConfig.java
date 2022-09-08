package com.vasanthan.ecommerce.config;

import com.vasanthan.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private EntityManager entityManager;
    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager=theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration( RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST,
                                              HttpMethod.DELETE,HttpMethod.PATCH};

        disableHttpMethods(Product.class,config, theUnsupportedActions);
        disableHttpMethods(ProductCategory.class,config, theUnsupportedActions);
        disableHttpMethods(Country.class,config, theUnsupportedActions);
        disableHttpMethods(State.class,config, theUnsupportedActions);
        disableHttpMethods(Order.class,config, theUnsupportedActions);

        // Call an internal expose method
        exposeIds(config);

        // Configure cors mapping
        cors.addMapping(config.getBasePath()+"/**").allowedOrigins(allowedOrigins);
    }

    private void disableHttpMethods(Class theClass,RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        // Disable http methods for Product: PUT, POST, DELETE
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // Expose entity Ids

        // Get a list of all entity classes from entity manager
        Set<EntityType<?>> entities= entityManager.getMetamodel().getEntities();

        // Create an array of the entity types
        List<Class> entityClass=new ArrayList<>();

        // Get the entity types of an entities
        for(EntityType tempEntityType: entities) {
            entityClass.add(tempEntityType.getJavaType());
        }

        // Expose the entity Ids for an array of entity/domain types
        Class[] domainType=entityClass.toArray(new Class[0]);
        config.exposeIdsFor(domainType);
    }
}
