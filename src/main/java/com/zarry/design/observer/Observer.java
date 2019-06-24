package com.zarry.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式：存在一对多的关系，当一个对象发生改变时，会影响其他对象发生改变
 */
public class Observer {

    public static void main(String[] args) {
        SubjectImpl subject = new SubjectImpl();
        subject.add(new ConObserver());
        subject.add(new ConObserver1());
        subject.notofiy();
    }

    //抽象目标/抽象主题
    static abstract class Subject{
        protected List<MyObserver> list;
        public void add(MyObserver obj){
            list.add(obj);
        }
        public void dele(MyObserver obj){
            list.remove(obj);
        }
        public abstract void notofiy();
    }
    //具体目标/具体主图
    static class SubjectImpl extends Subject{
        public SubjectImpl() {
            list = new ArrayList<>();
        }

        @Override
        public void notofiy() {
            for (MyObserver observer: list) {
                observer.response();
            }
        }
    }
    //抽象观察者
    interface MyObserver{
        void response();
    }
    //具体观察者
    static class ConObserver implements MyObserver{

        @Override
        public void response() {
            System.out.println("1号收到通知");
        }
    }
    //具体观察者
    static class ConObserver1 implements MyObserver{

        @Override
        public void response() {
            System.out.println("2号收到通知");
        }
    }
}
