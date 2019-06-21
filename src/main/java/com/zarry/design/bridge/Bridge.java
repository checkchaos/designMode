package com.zarry.design.bridge;

/**
 * 桥接模式：存在两个独立变化的维度，比如：包（提包、钱包）、颜色（红色、黄色）；为了更好的扩展和灵活变动
 *          而设立      抽象化类，扩展抽象化类、实现化类、具体实现化类
 */
public class Bridge {

    public static void main(String[] args) {
        new ExtendAbstractPacket(new RedRealColor()).getPacket();
    }
    //抽象化类
    static abstract class AbstractPacket{
        protected RealizationColor color;

        public AbstractPacket(RealizationColor color) {
            this.color = color;
        }

        public abstract void getPacket();
    }
    //扩展抽象类
    static class ExtendAbstractPacket extends AbstractPacket{
        public ExtendAbstractPacket(RealizationColor color) {
            super(color);
        }

        @Override
        public void getPacket() {
            System.out.println(color.getColor()+"手提包");
        }
    }
    //实现类
    static abstract class RealizationColor{
        abstract String getColor();
    }
    //具体实现类
    static class RedRealColor extends RealizationColor{

        @Override
        String getColor() {
            return "红色";
        }
    }
}
