package com.example.myfreelancer;

public class Model_Order {

    private String BuyerID, BuyerName, ServiceImage, OCategory, ODeliveryDays, OImage, OPaymentMethod, OPrice, OStatus, OTitle, OrderID, SellerID, SellerName, ServiceID ;


    public Model_Order() {
    }

    public Model_Order(String ServiceImage, String BuyerID, String OCategory, String OPaymentMethod, String ServiceID, String BuyerName, String ODeliveryDays, String OImage, String OPrice, String OStatus, String OTitle, String OrderID, String SellerID, String SellerName) {
        this.BuyerID = BuyerID;
        this.ServiceImage = ServiceImage;
        this.BuyerName = BuyerName;
        this.ODeliveryDays = ODeliveryDays;
        this.OImage = OImage;
        this.OPrice = OPrice;
        this.OStatus = OStatus;
        this.OTitle = OTitle;
        this.OrderID = OrderID;
        this.SellerID = SellerID;
        this.SellerName = SellerName;
        this.ServiceID = ServiceID;
        this.OCategory = OCategory;
        this.OPaymentMethod = OPaymentMethod;
    }

    public String getServiceImage() {
        return ServiceImage;
    }

    public void setServiceImage(String serviceImage) {
        ServiceImage = serviceImage;
    }

    public String getOCategory() {
        return OCategory;
    }

    public void setOCategory(String OCategory) {
        this.OCategory = OCategory;
    }

    public String getOPaymentMethod() {
        return OPaymentMethod;
    }

    public void setOPaymentMethod(String OPaymentMethod) {
        this.OPaymentMethod = OPaymentMethod;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public void setBuyerID(String buyerID) {
        BuyerID = buyerID;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getODeliveryDays() {
        return ODeliveryDays;
    }

    public void setODeliveryDays(String ODeliveryDays) {
        this.ODeliveryDays = ODeliveryDays;
    }

    public String getOImage() {
        return OImage;
    }

    public void setOImage(String OImage) {
        this.OImage = OImage;
    }

    public String getOPrice() {
        return OPrice;
    }

    public void setOPrice(String OPrice) {
        this.OPrice = OPrice;
    }

    public String getOStatus() {
        return OStatus;
    }

    public void setOStatus(String OStatus) {
        this.OStatus = OStatus;
    }

    public String getOTitle() {
        return OTitle;
    }

    public void setOTitle(String OTitle) {
        this.OTitle = OTitle;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }
}
