package com.th.ac.ku.kps.cpe.ecommerce.model;

import com.th.ac.ku.kps.cpe.ecommerce.model.allenum.OrderHistoryStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_history", schema = "e-commerce_01")
public class OrderHistoryEntity {
    private int idOrderHistory;
    private int idBuyer;
    private String usernameBuyer;
    private int idItem;
    private int idShop;
    private String nameShop;
    private int idProduct;
    private String nameProduct;
    private int idVariation;
    private String nameVariation;
    private int quantity;
    private double price;
    private OrderHistoryStatus status;

    @Id
    @Column(name = "id_order_history")
    public int getIdOrderHistory() {
        return idOrderHistory;
    }

    public void setIdOrderHistory(int idOrderHistory) {
        this.idOrderHistory = idOrderHistory;
    }

    @Basic
    @Column(name = "id_buyer")
    public int getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(int idBuyer) {
        this.idBuyer = idBuyer;
    }

    @Basic
    @Column(name = "username_buyer")
    public String getUsernameBuyer() {
        return usernameBuyer;
    }

    public void setUsernameBuyer(String usernameBuyer) {
        this.usernameBuyer = usernameBuyer;
    }

    @Basic
    @Column(name = "id_item")
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    @Basic
    @Column(name = "id_shop")
    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    @Basic
    @Column(name = "name_shop")
    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    @Basic
    @Column(name = "id_product")
    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    @Basic
    @Column(name = "name_product")
    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    @Basic
    @Column(name = "id_variation")
    public int getIdVariation() {
        return idVariation;
    }

    public void setIdVariation(int idVariation) {
        this.idVariation = idVariation;
    }

    @Basic
    @Column(name = "name_variation")
    public String getNameVariation() {
        return nameVariation;
    }

    public void setNameVariation(String nameVariation) {
        this.nameVariation = nameVariation;
    }

    @Basic
    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public OrderHistoryStatus getStatus() {
        return status;
    }

    public void setStatus(OrderHistoryStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderHistoryEntity that = (OrderHistoryEntity) o;
        return idOrderHistory == that.idOrderHistory &&
                idBuyer == that.idBuyer &&
                idShop == that.idShop &&
                nameShop == that.nameShop &&
                idProduct == that.idProduct &&
                idVariation == that.idVariation &&
                nameVariation == that.nameVariation &&
                quantity == that.quantity &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(usernameBuyer, that.usernameBuyer) &&
                Objects.equals(nameProduct, that.nameProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrderHistory, idBuyer, usernameBuyer, idShop, nameShop, idProduct, nameProduct, idVariation, nameVariation, quantity, price);
    }
}