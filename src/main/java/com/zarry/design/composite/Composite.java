package com.zarry.design.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式：表示一个对象整体与部分的层次结构关系，隐藏组合对象和单个对象的不同
 * 有两种形式：透明式 安全式
 */
public class Composite {

    public static void main(String[] args) {
        Bag bag = new Bag();
        bag.add(new Good("语文","红色"));
        bag.add(new Good("数学","紫色"));
        bag.add(new Good("历史","绿色"));
        bag.operate();
    }
    //抽象体
    interface Package{
        void operate();
    }
    //树叶
    static class Good implements Package{
        private String name;
        private String color;
        public Good(String name,String color){
            this.name = name;
            this.color = color;
        }

        @Override
        public void operate() {
            System.out.println(name +" 书 - " + color + "颜色");
        }
    }
    //树枝
    static class Bag implements Package {
        private List<Package> leafs;
        public Bag(){
            leafs = new ArrayList<>();
        }
        public void add(Package leaf){
            leafs.add(leaf);
        }

        @Override
        public void operate() {
            for (Package leaf: leafs){
                leaf.operate();
            }
        }
    }
}
