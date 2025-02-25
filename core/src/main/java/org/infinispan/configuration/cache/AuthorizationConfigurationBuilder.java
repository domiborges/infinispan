package org.infinispan.configuration.cache;

import static org.infinispan.configuration.cache.AuthorizationConfiguration.ENABLED;
import static org.infinispan.configuration.cache.AuthorizationConfiguration.ROLES;
import static org.infinispan.util.logging.Log.CONFIG;

import java.util.Set;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.global.GlobalConfiguration;

/**
 * AuthorizationConfigurationBuilder.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class AuthorizationConfigurationBuilder extends AbstractSecurityConfigurationChildBuilder implements Builder<AuthorizationConfiguration> {
   private final AttributeSet attributes;

   public AuthorizationConfigurationBuilder(SecurityConfigurationBuilder securityBuilder) {
      super(securityBuilder);
      attributes = AuthorizationConfiguration.attributeDefinitionSet();
   }

   /**
    * Disables authorization for this cache
    */
   public AuthorizationConfigurationBuilder disable() {
      attributes.attribute(ENABLED).set(false);
      return this;
   }

   /**
    * Enables authorization for this cache. If no explicit {@link #role(String)} are specified, all of the global roles apply.
    */
   public AuthorizationConfigurationBuilder enable() {
      attributes.attribute(ENABLED).set(true);
      return this;
   }

   /**
    * Enables/disables authorization for this cache. If enabled and no explicit {@link #role(String)} are specified, all of the global roles apply.
    */
   public AuthorizationConfigurationBuilder enabled(boolean enabled) {
      this.attributes.attribute(ENABLED).set(enabled);
      return this;
   }

   /**
    * Adds a role that can work with this cache. Roles must be declared in the {@link org.infinispan.configuration.global.GlobalAuthorizationConfigurationBuilder}.
    * @param name the name of the role
    */
   public AuthorizationConfigurationBuilder role(String name) {
      Set<String> roles = attributes.attribute(ROLES).get();
      roles.add(name);
      attributes.attribute(ROLES).set(roles);
      return this;
   }

   /**
    * Adds roles that can work with this cache. Roles must be declared in the {@link org.infinispan.configuration.global.GlobalAuthorizationConfigurationBuilder}.
    * @param names the names of the roles
    */
   public AuthorizationConfigurationBuilder roles(String... names) {
      for (String name : names) {
         this.role(name);
      }
      return this;
   }

   @Override
   public void validate(GlobalConfiguration globalConfig) {
      if (attributes.attribute(ENABLED).get() && !globalConfig.security().authorization().enabled()) {
         throw CONFIG.globalSecurityAuthShouldBeEnabled();
      }
      Set<String> cacheRoles = attributes.attribute(ROLES).get();
      Set<String> globalRoles = globalConfig.security().authorization().roles().keySet();
      if (!globalRoles.containsAll(cacheRoles)) {
         cacheRoles.removeAll(globalRoles);
         throw CONFIG.noSuchGlobalRoles(cacheRoles);
      }
   }

   @Override
   public AuthorizationConfiguration create() {
      return new AuthorizationConfiguration(attributes.protect());
   }

   @Override
   public Builder<?> read(AuthorizationConfiguration template) {
      this.attributes.read(template.attributes());
      return this;
   }

   @Override
   public String toString() {
      return "AuthorizationConfigurationBuilder [attributes=" + attributes + "]";
   }
}
