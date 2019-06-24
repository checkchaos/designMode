package com.zarry.design.memento;

import java.util.HashMap;
import java.util.Map;

/**
 * 备忘录模式：保存程序的上一个状态，给程序提供回退功能 操作
 */
public class Memento {

    public static void main(String[] args) throws CloneNotSupportedException {
        Pub pub = new Pub();
        pub.createMem("快照1");
        pub.alterMement("修改后");
        System.out.println("修改后: "+pub.getMement().getState());
        pub.resetMem("快照1");
        System.out.println("恢复：" +pub.getMement().getState());
    }
    //备忘录角色
    static class Mement implements Cloneable {
        private String state;

        public Mement(String state) {
            this.state = state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        //结合原型模式
        @Override
        protected Mement clone() throws CloneNotSupportedException {
            return (Mement) super.clone();
        }
    }

    //发起人角色
    static class Pub {
        private Manager manager;
        private Mement mement;

        public Pub() {
            manager = new Manager();
        }

        public void createMem(String username) throws CloneNotSupportedException {
            mement = new Mement("快照1");
            manager.setMement(mement.clone(),username);
        }

        public Mement getMement() {
            return mement;
        }

        public void alterMement(String state){
            mement.setState(state);
        }

        public void resetMem(String username) {
            this.mement = manager.getMement(username);
        }
    }

    //管理者
    static class Manager {
        private Map<String, Mement> manage;

        public Manager() {
            manage = new HashMap<>();
        }

        public void setMement(Mement mement, String uername) {
            manage.put(uername, mement);
        }

        public Mement getMement(String username) {
            return manage.get(username);
        }
    }
}
