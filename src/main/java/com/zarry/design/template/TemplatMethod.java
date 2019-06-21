package com.zarry.design.template;

/**
 * 模板方法模式：对于算法或者需求，已有固定的流程，但是有些流程是一样的但个别流程不一样，
 */
public class TemplatMethod {

    //抽象类
    static abstract class AbstractClass{
        public static void main(String[] args) {
            new Son().templat();
        }
        public void templat(){
            System.out.println("模板方法开始-----------");
            method1();
            method2();
            method3();
            methor4();
            System.out.println("模板方法结束-----------");
        }
        protected abstract void method1();
        protected abstract void method2();
        protected void method3(){
            System.out.println("相同处理部分");
        }
        //钩子方法
        protected void methor4(){}
    }
    //具体子类
    static class Son extends AbstractClass{

        @Override
        protected void method1() {
            System.out.println("子类方法1");
        }

        @Override
        protected void method2() {
            System.out.println("子类方法2");
        }

        @Override
        protected void methor4() {
            System.out.println("子类钩子方法");
        }
    }
}
