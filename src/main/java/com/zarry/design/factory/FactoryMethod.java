package com.zarry.design.factory;

/**
 * 工厂方法模式
 *
 * 抽象工厂模式  和工厂方法模式相似就是Factory的方法更多
 *
 * 一般在使用工厂模式的时候 都是多模式结合 简单工厂、抽象工厂、工厂方法结合使用
 */
public class FactoryMethod {

    public static void main(String[] args) {
        People obj = new PeopleFactory().createObj();
    }

    interface Factory{
        Object createObj();
    }

    static class PeopleFactory implements Factory{

        @Override
        public People createObj() {
            return new People();
        }
    }

    static class AnimalFactory implements Factory{

        @Override
        public Animal createObj() {
            return new Animal();
        }
    }

    static class Animal{
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class People{
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
