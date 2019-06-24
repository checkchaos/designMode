package com.zarry.design.strategy;

/**
 * 策略模式：将每种不同算法进行封装，隔离算法实现和调用
 */
public class Strategy {
    public static void main(String[] args) {
        new Context(new StrategyImp()).abstractMethod();
    }

    //抽象策略类
    interface AbstractStrategy{
        void abstractMethod();
    }

    //策略类1
    static class StrategyImpl implements AbstractStrategy{

        @Override
        public void abstractMethod() {
            System.out.println("策略1");
        }
    }
    //策略类2
    static class StrategyImp implements AbstractStrategy{

        @Override
        public void abstractMethod() {
            System.out.println("策略2");
        }
    }
    //环境类
    static class Context{
        private AbstractStrategy strategy;

        public Context(AbstractStrategy strategy) {
            this.strategy = strategy;
        }

        public void abstractMethod(){
            strategy.abstractMethod();
        }
    }
}
