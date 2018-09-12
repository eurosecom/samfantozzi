package com.eusecom.samfantozzi.soapweather;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * body
 * Created by Jeeson on 16/7/15.
 */
@Root(name = "Body")
public class WeatherResponseBody {

    @Element(name = "getWeatherbyCityNameResponse", required = false)
    public WeatherResponseModel getWeatherbyCityNameResponse;

}
