package com.zarry.design.command;

/**
 * 命令模式：需要将命令发布者与命令执行者进行分开解耦，可将具体命令抽离出来
 */
public class Command {

    public static void main(String[] args) {
        new Invoke(new Bread()).call();
    }
    //抽象命令
    interface Cooker{
        void cooking();
    }
    //具体命令
    static class Bread implements Cooker{

        private Breader breader;
        public Bread(){
            this.breader = new Breader();
        }
        @Override
        public void cooking() {
            breader.excute();
        }
    }
    //命令接收者
    static class Breader{
        void excute(){
            System.out.println("我在做面包");
        }
    }
    //命令调用者
    static class Invoke{
        private Cooker cooker;
        public Invoke(Cooker cooker){
            this.cooker = cooker;
        }
        public void call(){
            cooker.cooking();
        }
    }
}
