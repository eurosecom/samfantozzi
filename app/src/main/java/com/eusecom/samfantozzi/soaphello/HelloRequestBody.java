package com.eusecom.samfantozzi.soaphello;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * body
 * Created by Jeeson on 16/7/15.
 */
@Root(name = "soap:Body", strict = false)
public class HelloRequestBody {

    @Element(name = "HelloWorld", required = false)
    public HelloRequestModel getHelloString;
}
