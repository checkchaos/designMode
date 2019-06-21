package com.zarry.design.decorator;

/**
 * 装饰模式：当现存核心组件需要功能扩展，可使用装饰模式
 *
 */
public class Decorator {

    public static void main(String[] args) {
        new Decorat8X(new Rifle()).sight();
    }
    //抽象构件 枪
    interface Gun{
        void launch();
    }
    //具体部件 步枪
    static class Rifle implements Gun{

        @Override
        public void launch() {
            System.out.println("步枪发射子弹");
        }
    }
    //抽象装饰 增加瞄准镜
    static abstract class Decorat implements Gun{
        protected Gun gun;
        public Decorat(Gun gun){
            this.gun = gun;
        }
        abstract void sight();
    }
    //具体装饰 8倍镜
    static class Decorat8X extends Decorat{

        public Decorat8X(Gun gun) {
            super(gun);
        }

        @Override
        public void launch() {
            gun.launch();
        }

        @Override
        void sight() {
            System.out.println("8倍镜瞄准");
            launch();
        }
    }
}
