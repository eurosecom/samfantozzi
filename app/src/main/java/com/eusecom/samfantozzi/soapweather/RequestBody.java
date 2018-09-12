package com.eusecom.samfantozzi.soapweather;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * body
 * Created by Jeeson on 16/7/15.
 */
@Root(name = "soapenv:Body", strict = false)
public class RequestBody {

    @Element(name = "getWeatherbyCityName", required = false)
    public RequestModel getWeatherbyCityName;
}
