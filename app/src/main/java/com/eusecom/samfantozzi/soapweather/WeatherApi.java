package com.eusecom.samfantozzi.soapweather;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface WeatherApi {

    @Headers({"Content-Type: text/xml;charset=UTF-8", "SOAPAction: http://WebXml.com.cn/getWeatherbyCityName"})
    @POST("WeatherWebService.asmx")
    Observable<ResponseEnvelope> getWeatherbyCityName(@Body RequestEnvelope requestEnvelope);

}