package com.eusecom.samfantozzi.soapweather;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Jeeson on 16/7/15.
 */

public class RequestModel {
    @Attribute(name = "xmlns")
    public String cityNameAttribute;

    @Element(name = "theCityName", required = false)
    public String theCityName;     //城市名字

}
