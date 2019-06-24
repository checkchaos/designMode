package com.zarry.design.visitor;

import javax.swing.text.html.parser.Element;

/**
 * 访问者模式：数据模型不变，而处理数据的方式易变时，可以将处理方式提出来进行抽象封装，
 */
public class Visitor {
    //抽象访问者
    interface Vister{
        void vist(Element element);
    }
    //具体访问者1
    static class Vister1 implements Vister {

        @Override
        public void vist(Element element) {

        }
    }
    //具体访问者2
    static class Vister2 implements Vister {

        @Override
        public void vist(Element element) {

        }
    }
    //抽象元素类
    interface Element{
        void accept(Vister vister);
    }
    //具体元素1
    static class Element1 implements Element{

        @Override
        public void accept(Vister vister) {
            vister.vist(this);
        }
    }
    //具体元素2
    static class Element2 implements Element{

        @Override
        public void accept(Vister vister) {
            vister.vist(this);
        }
    }
    //
}
