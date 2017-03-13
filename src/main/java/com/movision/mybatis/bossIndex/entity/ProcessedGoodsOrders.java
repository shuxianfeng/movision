package com.movision.mybatis.bossIndex.entity;

/**
 * @Author zhurui
 * @Date 2017/3/13 11:11
 */
public class ProcessedGoodsOrders {

    private Integer addVExamine;//加V审批

    private Integer shopExamine;//店铺审批

    private Integer pendingQuantity;//待处理

    private Integer selfSupportGoods;//自营商品数量

    private Integer tripartiteGoods;//第三方商品数量

    private Integer inventoryWarning;//库存警告商品数量

    private Integer sellLikeHotCakesGoods;//畅销商品

    private Integer tripartiteShop;//第三方商铺

    private Integer noStock;//无库存商品

    private Integer promotionItem;//促销商品

    private Integer soldOut;//下架商品

    private Integer goodsQuantity;//商品数量

    private Integer dropShipping;//待发货

    private Integer tobepaid;//待支付

    private Integer deliverGoods;//已发货订单

    private Integer refund;//退款

    private Integer salesReturn;//退货

    private Integer orderQuantity;//订单数量

    public Integer getDeliverGoods() {
        return deliverGoods;
    }

    public void setDeliverGoods(Integer deliverGoods) {
        this.deliverGoods = deliverGoods;
    }

    public Integer getNoStock() {
        return noStock;
    }

    public void setNoStock(Integer noStock) {
        this.noStock = noStock;
    }

    public Integer getTripartiteShop() {
        return tripartiteShop;
    }

    public void setTripartiteShop(Integer tripartiteShop) {
        this.tripartiteShop = tripartiteShop;
    }

    public Integer getPromotionItem() {
        return promotionItem;
    }

    public void setPromotionItem(Integer promotionItem) {
        this.promotionItem = promotionItem;
    }

    public Integer getAddVExamine() {
        return addVExamine;
    }

    public void setAddVExamine(Integer addVExamine) {
        this.addVExamine = addVExamine;
    }

    public Integer getShopExamine() {
        return shopExamine;
    }

    public void setShopExamine(Integer shopExamine) {
        this.shopExamine = shopExamine;
    }

    public Integer getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(Integer pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public Integer getSelfSupportGoods() {
        return selfSupportGoods;
    }

    public void setSelfSupportGoods(Integer selfSupportGoods) {
        this.selfSupportGoods = selfSupportGoods;
    }

    public Integer getTripartiteGoods() {
        return tripartiteGoods;
    }

    public void setTripartiteGoods(Integer tripartiteGoods) {
        this.tripartiteGoods = tripartiteGoods;
    }

    public Integer getInventoryWarning() {
        return inventoryWarning;
    }

    public void setInventoryWarning(Integer inventoryWarning) {
        this.inventoryWarning = inventoryWarning;
    }

    public Integer getSellLikeHotCakesGoods() {
        return sellLikeHotCakesGoods;
    }

    public void setSellLikeHotCakesGoods(Integer sellLikeHotCakesGoods) {
        this.sellLikeHotCakesGoods = sellLikeHotCakesGoods;
    }

    public Integer getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(Integer soldOut) {
        this.soldOut = soldOut;
    }

    public Integer getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(Integer goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public Integer getDropShipping() {
        return dropShipping;
    }

    public void setDropShipping(Integer dropShipping) {
        this.dropShipping = dropShipping;
    }

    public Integer getTobepaid() {
        return tobepaid;
    }

    public void setTobepaid(Integer tobepaid) {
        this.tobepaid = tobepaid;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public Integer getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(Integer salesReturn) {
        this.salesReturn = salesReturn;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
