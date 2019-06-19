package com.zarry.design.proxy;

public class StaticProxy {
    public static void main(String[] args) {
        new ProxyPeople(new People()).invoke();
    }
    interface Shop{
        void shop();
    }

    static class People implements Shop{
        @Override
        public void shop() {
            System.out.println("去香港买化妆品");
        }
    }

    static class ProxyPeople{
        private Shop shop;
        public ProxyPeople(Shop shop){
            this.shop = shop;
        }
        public void invoke(){
            System.out.println("代理前");
            shop.shop();
            System.out.println("代理后");
        }
    }
}
