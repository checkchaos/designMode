package com.zarry.design.flyweight;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 享元模式：存处理大量相似或相同对象，可以提取出相同（共享部分-内部状态）维护不同部分（外部状态）
 * 将对象细粒化
 */
public class Flyweight {

    public static void main(String[] args) {
        FlyWeightFactory.getChess("black").play("12,34");
        FlyWeightFactory.getChess("black").play("13,54");
        FlyWeightFactory.getChess("white").play("33,24");
        FlyWeightFactory.getChess("white").play("23,14");
        FlyWeightFactory.getChess("black").play("18,04");
        FlyWeightFactory.getChess("black").play("18,74");
    }

    //抽象享元
    interface Chess {
        String innerShare = " 棋 ";

        void play(String position);
    }

    //具体享元
    static class ChessImpl implements Chess {
        private String color;
        private NoShare outStatus;

        public ChessImpl(String color, NoShare outStatus) {
            this.color = color;
            this.outStatus = outStatus;
        }

        @Override
        public void play(String position) {
            System.out.println(color + innerShare + outStatus.getInfo(position));
        }
    }

    //非享元对象
    static class NoShare {
        public String getInfo(String position) {
            return position;
        }
    }

    static class FlyWeightFactory {
        private static Map factory = new HashMap<String, Chess>();

        public static Chess getChess(String color) {
            Chess chess = (Chess) factory.get(color);
            if (null != chess) {
                return chess;
            } else {
                ChessImpl chess1 = new ChessImpl(color, new NoShare());
                factory.put(color, chess1);
                return chess1;
            }
        }
    }
}
