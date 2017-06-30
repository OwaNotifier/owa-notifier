package info.kapable.utils.owanotifier.auth;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;

public interface TokenService {

  @FormUrlEncoded
  @POST("/{tenantid}/oauth2/v2.0/token")
  Response getAccessTokenFromAuthCode(
    @Path("tenantid") String tenantId,
    @Field("client_id") String clientId,
    @Field("client_secret") String clientSecret,
    @Field("grant_type") String grantType,
    @Field("code") String code,
    @Field("redirect_uri") String redirectUrl
  );
}