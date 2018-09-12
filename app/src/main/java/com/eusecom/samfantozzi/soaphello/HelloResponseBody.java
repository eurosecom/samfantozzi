package com.eusecom.samfantozzi.soaphello;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * body
 * Created by Jeeson on 16/7/15.
 */
@Root(name = "Body")
public class HelloResponseBody {

    @Element(name = "HelloWorldResponse", required = false)
    public HelloResponseModel getHelloResponse;

}
