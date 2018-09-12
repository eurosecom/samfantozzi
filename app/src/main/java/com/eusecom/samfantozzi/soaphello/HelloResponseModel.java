package com.eusecom.samfantozzi.soaphello;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import java.util.List;

/**
 * Created by Jeeson on 16/7/15.
 */

@Root(name = "HelloWorldResponse")
@Namespace(reference = "http://Wsdl2CodeTestService/")
public class HelloResponseModel {

    @Element(name = "HelloWorldResult")
    public String result;

    //model with @ElementList for more elements in root HelloWorldResponse
    //@ElementList(name = "getWeatherbyCityNameResult")
    //public List<String> result;

}
