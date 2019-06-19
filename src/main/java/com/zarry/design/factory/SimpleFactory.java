package com.zarry.design.factory;

/**
 * 简单工厂
 */
public class SimpleFactory implements Factory{
    @Override
    public Product create() {
        return new Product();
    }
}

interface Factory{
    Product create();
}

class Product{
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
