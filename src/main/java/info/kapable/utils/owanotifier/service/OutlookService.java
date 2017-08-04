package info.kapable.utils.owanotifier.service;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface OutlookService {

  @GET("/v1.0/me")
  Response getCurrentUser();

  @GET("/v1.0/me/mailfolders/{folderid}")
  Response getFolder(
    @Path("folderid") String folderId
  );
  
  @GET("/v1.0/me/mailfolders/{folderid}/messages")
  Response getMessages(
    @Path("folderid") String folderId,
    @Query("$orderby") String orderBy,
    @Query("$select") String select,
    @Query("$filter") String filter,
    @Query("$top") Integer maxResults
  );
}