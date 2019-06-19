package com.zarry.design.single;

/**
 * 单例模式
 */
public class Single {
    private static Single single;
    private Single(){}
    public static Single getInstance(){
        if (single == null){
            synchronized (Single.class){
                if (single == null) {
                    single = new Single();
                }
            }
        }
        return single;
    }
}
