package com.example.zhujia.dxracer_factory.Data;

import java.io.Serializable;

/**
 * Created by ZHUJIA on 2018/1/2.
 */

@SuppressWarnings("serial")
public class ConsumableData implements Serializable {
    private String num;
    private String text;

    public ConsumableData() {
    }

    public ConsumableData(String subtotal, String text) {
        super();
        this.num = subtotal;
        this.text = text;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String subtotal) {
        this.num = subtotal;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * 为什么要重写toString()呢？
     *
     * 因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
     */
    @Override
    public String toString() {
        return text;
    }

}
