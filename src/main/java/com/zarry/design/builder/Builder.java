package com.zarry.design.builder;

/**
 * 建造者模式：建造一些比较复杂的对象，注重建造组装过程，工厂模式注重建造的创建过程
 */
public class Builder {
    public static void main(String[] args) {
        new Manager(new ComputerBuilderImpl()).builde().show();

    }
    static class Computer{
        private String cpu;
        private String memory;
        private String display;

        public void show(){
            System.out.println("我的电脑是：cpu:" + cpu  + " memory: " + memory + " display: " + display);
        }

        public void setCpu(String cpu) {
            this.cpu = cpu;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }

    interface Builde{
        void buildCpu();
        void buildMemory();
        void buildDisplay();
        Object getResult();
    }

    static class Manager{
        private Builde builde;
        public Manager(Builde builde){
            this.builde = builde;
        }
        public Computer builde(){
            builde.buildCpu();
            builde.buildDisplay();
            builde.buildMemory();
            return (Computer) builde.getResult();
        }
    }

    static class ComputerBuilderImpl extends ComputerBuilder{
        public ComputerBuilderImpl() {
            super(new Computer());
        }
    }

    static abstract class ComputerBuilder implements Builde{

        private Computer computer;
        protected ComputerBuilder(Computer computer){
            this.computer = computer;
        }
        @Override
        public void buildCpu() {
            computer.setCpu("三星cpu");
        }

        @Override
        public void buildMemory() {
            computer.setMemory("三星缓存");
        }

        @Override
        public void buildDisplay() {
            computer.setDisplay("三星显卡");
        }

        @Override
        public Computer getResult() {
            return computer;
        }
    }
}
