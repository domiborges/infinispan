package org.infinispan.hotrod;

import org.infinispan.api.configuration.MultiMapConfiguration;
import org.infinispan.api.mutiny.MutinyMultiMap;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

/**
 * @since 14.0
 **/
public class HotRodMutinyMultiMap<K, V> implements MutinyMultiMap<K, V> {
   private final HotRod hotrod;
   private final String name;

   HotRodMutinyMultiMap(HotRod hotrod, String name) {
      this.hotrod = hotrod;
      this.name = name;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public Uni<MultiMapConfiguration> configuration() {
      return null;
   }

   @Override
   public HotRodMutinyContainer container() {
      return hotrod.mutiny();
   }

   @Override
   public Uni<Void> add(K key, V value) {
      return null;
   }

   @Override
   public Multi<V> get(K key) {
      return null;
   }

   @Override
   public Uni<Boolean> remove(K key) {
      return null;
   }

   @Override
   public Uni<Boolean> remove(K key, V value) {
      return null;
   }

   @Override
   public Uni<Boolean> containsKey(K key) {
      return null;
   }

   @Override
   public Uni<Boolean> containsEntry(K key, V value) {
      return null;
   }

   @Override
   public Uni<Long> estimateSize() {
      return null;
   }
}
