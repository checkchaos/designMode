package com.zarry.design.prototype;

public class PrototypeImpl implements Prototype {
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Object clone(){
        Prototype obj = null;
        try {
            obj = (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "PrototypeImpl{" +
                "id=" + id +
                '}';
    }
}
