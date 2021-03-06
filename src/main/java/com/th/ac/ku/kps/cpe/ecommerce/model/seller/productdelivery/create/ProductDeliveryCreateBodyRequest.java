package com.th.ac.ku.kps.cpe.ecommerce.model.seller.productdelivery.create;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ProductDeliveryCreateBodyRequest {
    private int id_product;
    private int id_type;
    private double price;
    private Integer time_ship;

    @JsonGetter
    public int getId_product() {
        return id_product;
    }

    @JsonSetter
    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    @JsonGetter
    public int getId_type() {
        return id_type;
    }

    @JsonSetter
    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    @JsonGetter
    public double getPrice() {
        return price;
    }

    @JsonSetter
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonGetter
    public Integer getTime_ship() {
        return time_ship;
    }

    @JsonSetter
    public void setTime_ship(Integer time_ship) {
        this.time_ship = time_ship;
    }
}
