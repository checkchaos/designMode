package com.zarry.design.adapter;

/**
 * 适配器模式 ：对现存接口的新需求适配 有类适配和对象适配
 *
 * 可以进行扩展为双向适配
 */
public class Adapter {

    public static void main(String[] args) {
        new AdapterImpl().target();
        new AdapterImpl2(new AdapeeImpl()).target();
    }
    //目标接口
    interface Target{
        void target();
    }
    //适配者接口
    interface Adapee{
        void adapee();
    }
    //适配者实现类
    static class AdapeeImpl implements Adapee{

        @Override
        public void adapee() {
            System.out.println("适配者实现类");
        }
    }
    //适配器1 类适配
    static class AdapterImpl extends AdapeeImpl implements Target {
        @Override
        public void target() {
            System.out.println("开始适配1");
            adapee();
        }
    }
    //适配器2 对象适配
    static class AdapterImpl2 implements Target{

        private Adapee adapee;
        public AdapterImpl2(Adapee adapee){
            this.adapee = adapee;
        }
        @Override
        public void target() {
            System.out.println("适配器2");
            adapee.adapee();
        }
    }
}
