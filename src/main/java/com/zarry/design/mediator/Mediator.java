package com.zarry.design.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者模式：存在一个复杂的网状关系，使用中介者模式，
 */
public class Mediator {

    public static void main(String[] args) {
        AbstractMediator mediator = new Mediator1();
        Colleague colleague = new Colleague();
        mediator.register(colleague);
        mediator.register(new Colleague2());
        colleague.send(colleague);

    }
    //抽象中介者
    interface AbstractMediator{
        void register(AbstractColleague colleague);
        void relay(AbstractColleague colleague);
    }
    //具体抽象中介者
    static class Mediator1 implements  AbstractMediator{
        private List<AbstractColleague> list;

        public Mediator1() {
            this.list = new ArrayList<>();
        }

        @Override
        public void register(AbstractColleague colleague) {
            list.add(colleague);
            colleague.setMediator(this);
        }

        @Override
        public void relay(AbstractColleague colleague) {
            for (AbstractColleague coll: list) {
                if (coll.equals(colleague)){
                    coll.receive();
                }
            }
        }
    }

    //抽象同事
    static abstract class AbstractColleague{
        protected AbstractMediator mediator;
        public void setMediator(AbstractMediator mediator){
            this.mediator = mediator;
        }
        public abstract void receive();
        public abstract void send(AbstractColleague colleague);
    }
    //具体同事
    static class Colleague extends AbstractColleague{

        @Override
        public void receive() {
            System.out.println("1号收到请求");
        }

        @Override
        public void send(AbstractColleague colleague) {
            mediator.relay(colleague);
        }
    }
    static class Colleague2 extends AbstractColleague{

        @Override
        public void receive() {
            System.out.println("2号收到请求");
        }

        @Override
        public void send(AbstractColleague colleague) {
            mediator.relay(colleague);
        }
    }
}
