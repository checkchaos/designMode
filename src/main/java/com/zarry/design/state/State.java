package com.zarry.design.state;

/**
 * 状态模式：运行时，有较多的分支if-else 状态，将每个状态抽象取出进行封装
 *           然后通过改变对象的状态影响对象处理行为，
 */
public class State {
    public static void main(String[] args) {
        Context context = new Context();
        context.excute();
        context.setStatus(new EndState()).excute();
    }
    //抽象状态类
    interface Status{
        void handle();
    }
    //具体状态类
    static class StartState implements Status {

        @Override
        public void handle() {
            System.out.println("开始状态");
        }
    }
    //具体状态类
    static class EndState implements Status{

        @Override
        public void handle() {
            System.out.println("结束状态");
        }
    }
    //环境类
    static class Context{
        private Status status;

        public Context setStatus(Status status) {
            this.status = status;
            return this;
        }

        public void excute(){
            if(status == null){
                status = new StartState();
            }
            status.handle();
        }
    }
}
