package com.learning.io.rpc.transport;

import com.learning.io.rpc.prototype.RpcResponse;
import com.learning.io.rpc.util.InvokeUtil;
import com.learning.io.rpc.ServiceFactory;
import com.learning.io.rpc.prototype.RpcContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HttpServletRpcHandler extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HttpServletRpcHandler.class);


    public HttpServletRpcHandler() {
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("exec HttpServletRpcHandler#doPost....");

        RpcContent respContent;
        try {
            ServletInputStream inputStream = req.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RpcContent content = (RpcContent) objectInputStream.readObject();

            Object interfaceImpl = ServiceFactory.getServiceFactory().get(content.getName());
            Object result = InvokeUtil.invoke(interfaceImpl, content.getMethodName(), content.getParameterTypes(), content.getArgs());

            // 将结果写回给客户端
            respContent = new RpcContent(new RpcResponse(result));

        } catch (Exception e) {
            respContent = new RpcContent(new RpcResponse(e));
        }

        ServletOutputStream outputStream = resp.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(respContent);
    }
}
