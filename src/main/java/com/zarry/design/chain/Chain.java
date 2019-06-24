package com.zarry.design.chain;

/**
 * 责任链模式：请求发送者不需要关注处理过程中的每个处理人，只需要发送请求即可，
 *            就会有相应的责任人进行每一步处理
 */
public class Chain {

    public static void main(String[] args) {
        new Request().request(new Handle1());
    }
    //抽象处理者
    static abstract class Handle{
        private Handle next;

        public void setNext(Handle next) {
            this.next = next;
        }

        public Handle getNext() {
            return next;
        }

        abstract void excute();
    }

    //处理者1
    static class Handle1 extends Handle{

        @Override
        void excute() {
            setNext(new Handle2());
            System.out.println("第一个人处理完成");
            if (getNext() != null){
                getNext().excute();
            }
        }
    }

    //处理者2
    static class Handle2 extends Handle{

        @Override
        void excute() {
            System.out.println("第二个人处理完成");
            if (getNext() != null){
                getNext().excute();
            }
        }
    }
    //发送请求者
    static class Request{
        public void request(Handle handle){
            System.out.println("发送请求");
            handle.excute();
        }
    }
}
