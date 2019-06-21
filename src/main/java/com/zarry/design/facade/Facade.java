package com.zarry.design.facade;

/**
 * 外观模式：对多个子系统接口的统一对外封装
 */
public class Facade {
    public static void main(String[] args) {
        new FacadeInterFace(new InterFace1(),new InterFace2()).commMethod();
    }
    static class FacadeInterFace{
        private InterFace1 method1;
        private InterFace2 method2;

        public FacadeInterFace(InterFace1 method1, InterFace2 method2) {
            this.method1 = method1;
            this.method2 = method2;
        }

        void commMethod(){
            System.out.println("我是统一方法处理接口");
            method1.method();
            method2.method();
        }
    }
    //子接口1
    static class InterFace1{
        void method(){
            System.out.println("接口1");
        }
    }
    //子接口2
    static class InterFace2{
        void method(){
            System.out.println("接口1");
        }
    }
}
