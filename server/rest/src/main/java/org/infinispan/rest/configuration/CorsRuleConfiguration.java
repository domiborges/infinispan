package org.infinispan.rest.configuration;

import java.util.Arrays;

import org.infinispan.commons.configuration.attributes.AttributeDefinition;
import org.infinispan.commons.configuration.attributes.AttributeSerializer;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.attributes.ConfigurationElement;

import io.netty.handler.codec.http.cors.CorsConfig;

/**
 * @since 10.0
 */
public class CorsRuleConfiguration extends ConfigurationElement<CorsRuleConfiguration> {
   public static final AttributeDefinition<String> NAME = AttributeDefinition.builder("name", null, String.class).immutable().build();
   public static final AttributeDefinition<Boolean> ALLOW_CREDENTIALS = AttributeDefinition.builder("allow-credentials", null, Boolean.class).immutable().build();
   public static final AttributeDefinition<Long> MAX_AGE = AttributeDefinition.builder("max-age-seconds", null, Long.class).immutable().build();
   public static final AttributeDefinition<String[]> ALLOW_ORIGINS = AttributeDefinition.builder("allowed-origins", null, String[].class)
         .serializer(stringArraySerializer())
         .immutable().autoPersist(false).build();

   private static AttributeSerializer<String[]> stringArraySerializer() {
      return (w, n, v) -> w.writeArrayElement(n, n, null, Arrays.asList(v));
   }

   public static final AttributeDefinition<String[]> ALLOW_METHODS = AttributeDefinition.builder("allowed-methods", null, String[].class)
         .serializer(stringArraySerializer())
         .immutable().autoPersist(false).build();
   public static final AttributeDefinition<String[]> ALLOW_HEADERS = AttributeDefinition.builder("allowed-headers", null, String[].class)
         .serializer(stringArraySerializer())
         .immutable().autoPersist(false).build();
   public static final AttributeDefinition<String[]> EXPOSE_HEADERS = AttributeDefinition.builder("expose-headers", null, String[].class)
         .serializer(stringArraySerializer())
         .immutable().autoPersist(false).build();

   public static AttributeSet attributeDefinitionSet() {
      return new AttributeSet(CorsRuleConfiguration.class, NAME, ALLOW_CREDENTIALS, MAX_AGE, ALLOW_ORIGINS, ALLOW_METHODS, ALLOW_HEADERS, EXPOSE_HEADERS);
   }

   private final CorsConfig corsConfig;

   CorsRuleConfiguration(AttributeSet attributes, CorsConfig corsConfig) {
      super("cors-rule", true, attributes,
            child(attributes.attribute(ALLOW_ORIGINS)),
            child(attributes.attribute(ALLOW_METHODS)),
            child(attributes.attribute(ALLOW_HEADERS)),
            child(attributes.attribute(EXPOSE_HEADERS))
      );
      this.corsConfig = corsConfig;
   }

   CorsConfig corsConfig() {
      return corsConfig;
   }
}
