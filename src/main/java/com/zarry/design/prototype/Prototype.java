package com.zarry.design.prototype;

/**
 * 原型涉及模式：实际使用的是clone()方法，去创建对象，和工厂模式功能相似
 */
public interface Prototype extends Cloneable {
    Object clone();
}
