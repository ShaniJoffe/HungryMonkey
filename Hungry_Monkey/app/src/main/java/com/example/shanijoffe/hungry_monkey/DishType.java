package com.example.shanijoffe.hungry_monkey;

/**
 * Created by Shani Joffe on 12/15/2017.
 */

public class DishType
{
    private int    _id;
    private String _name;
    public DishType()
    {

    }
    public DishType(int id, String name)
    {
        this._id = id;
        this._name = name;

    }
    public void setID(int id) {
        this._id = id;
    }
    public int getID() {
        return this._id;
    }
    public void setDishName(String name) {
        this._name = name;
    }
    public String getDishtName() {
        return this._name;
    }
}
