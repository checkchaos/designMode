package com.zarry.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InvokeProxy {

    public static void main(String[] args) {
        Shop o = (Shop) new Invoke().newInstance(new PeopleShop());
        o.shopping();
    }
    interface Shop{
        void shopping();
    }

    static class PeopleShop implements Shop{

        @Override
        public void shopping() {
            System.out.println("去韩国买化妆品");
        }
    }

    static class Invoke implements InvocationHandler{
        private Object object;
        public Object newInstance(Object object){
            this.object = object;
            return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("动态代理前");
            method.invoke(object,args);
            System.out.println("动态代理后");
            return null;
        }
    }
}
