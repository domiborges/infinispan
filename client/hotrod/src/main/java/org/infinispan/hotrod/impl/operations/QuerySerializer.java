package org.infinispan.hotrod.impl.operations;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.infinispan.commons.dataconversion.MediaType.APPLICATION_JSON;
import static org.infinispan.commons.dataconversion.MediaType.MATCH_ALL;

import java.io.IOException;

import org.infinispan.api.common.query.QueryRequest;
import org.infinispan.commons.dataconversion.MediaType;
import org.infinispan.commons.dataconversion.internal.Json;
import org.infinispan.commons.marshall.Marshaller;
import org.infinispan.hotrod.exceptions.HotRodClientException;
import org.infinispan.hotrod.impl.query.QueryResponse;
import org.infinispan.hotrod.impl.query.RemoteQuery;
import org.infinispan.protostream.ProtobufUtil;
import org.infinispan.protostream.SerializationContext;

/**
 * @since 9.4
 */
enum QuerySerializer {

   JSON(APPLICATION_JSON) {
      @Override
      byte[] serializeQueryRequest(RemoteQuery remoteQuery, QueryRequest queryRequest) {
         Json object = Json.make(queryRequest);
         return object.toString().getBytes(UTF_8);
      }

      @Override
      QueryResponse readQueryResponse(Marshaller marshaller, RemoteQuery remoteQuery, byte[] bytesResponse) {
         Json response = Json.read(new String(bytesResponse, UTF_8));
         //return new JsonClientQueryResponse(response);
         return null;
      }
   },

   DEFAULT(MATCH_ALL) {
      @Override
      byte[] serializeQueryRequest(RemoteQuery remoteQuery, QueryRequest queryRequest) {
         final SerializationContext serCtx = remoteQuery.getSerializationContext();
         Marshaller marshaller;
         if (serCtx != null) {
            try {
               return ProtobufUtil.toByteArray(serCtx, queryRequest);
            } catch (IOException e) {
               throw new HotRodClientException(e);
            }
         } else {
            marshaller = remoteQuery.getCache().getHotRodTransport().getMarshaller();
            try {
               return marshaller.objectToByteBuffer(queryRequest);
            } catch (IOException e) {
               throw new HotRodClientException(e);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               throw new HotRodClientException(e);
            }
         }
      }

      @Override
      QueryResponse readQueryResponse(Marshaller marshaller, RemoteQuery remoteQuery, byte[] bytesResponse) {
         SerializationContext serCtx = remoteQuery.getSerializationContext();
         if (serCtx != null) {
            try {
               return ProtobufUtil.fromByteArray(serCtx, bytesResponse, QueryResponse.class);
            } catch (IOException e) {
               throw new HotRodClientException(e);
            }
         } else {
            try {
               return (QueryResponse) marshaller.objectFromByteBuffer(bytesResponse);
            } catch (IOException | ClassNotFoundException e) {
               throw new HotRodClientException(e);
            }
         }
      }
   };

   private final MediaType mediaType;

   QuerySerializer(MediaType mediaType) {
      this.mediaType = mediaType;
   }

   static QuerySerializer findByMediaType(MediaType mediaType) {
      return mediaType != null && mediaType.match(APPLICATION_JSON) ? JSON : DEFAULT;
   }

   @Override
   public String toString() {
      return mediaType.getTypeSubtype();
   }

   abstract byte[] serializeQueryRequest(RemoteQuery remoteQuery, QueryRequest queryRequest);

   abstract QueryResponse readQueryResponse(Marshaller marshaller, RemoteQuery remoteQuery, byte[] bytesResponse);
}
