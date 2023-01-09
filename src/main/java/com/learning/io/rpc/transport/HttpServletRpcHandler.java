package com.learning.io.rpc.transport;

import com.learning.io.rpc.InvokeUtil;
import com.learning.io.rpc.SimpleRegisterCenter;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("exec HttpServletRpcHandler#doPost....");
        try {
            ServletInputStream inputStream = req.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RpcContent content = (RpcContent) objectInputStream.readObject();

            Object interfaceImpl = SimpleRegisterCenter.getRegisterCenter().get(SimpleRegisterCenter.MODULE_SERVER_A, content.getName());
            Object result = InvokeUtil.invoke(interfaceImpl, content.getMethodName(), content.getParameterTypes(), content.getArgs());

            // 将结果写回给客户端
            RpcContent respContent = new RpcContent(result);
            ServletOutputStream outputStream = resp.getOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(outputStream);
            oout.writeObject(respContent);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }

    }
}
