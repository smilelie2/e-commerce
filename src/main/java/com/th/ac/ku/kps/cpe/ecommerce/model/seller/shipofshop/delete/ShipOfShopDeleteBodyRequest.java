package com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.delete;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ShipOfShopDeleteBodyRequest {
    private int id_ship;
@JsonGetter
    public int getId_ship() {
        return id_ship;
    }
@JsonSetter
    public void setId_ship(int id_ship) {
        this.id_ship = id_ship;
    }
}
