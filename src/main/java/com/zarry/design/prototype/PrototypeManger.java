package com.zarry.design.prototype;

import java.util.HashMap;
import java.util.Map;

public class PrototypeManger {
    private static Map prototypes = new HashMap<String,Prototype>();
    public static Prototype ceshi = new PrototypeImpl();

    static {
        prototypes.put("1",ceshi);
    }

    public static Prototype getPrototype(String key){
        return (Prototype) ((Prototype) prototypes.get(key)).clone();
    }

    public static void main(String[] args) {
        Prototype prototype = getPrototype("1");
        System.out.println(prototype);
        System.out.println((prototype == ceshi)+"---:---"+prototype.equals(ceshi)+"---:---"+prototype.toString());
    }
}
