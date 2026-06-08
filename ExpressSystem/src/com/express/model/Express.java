package com.express.model;

import java.util.Date;
import java.util.Random;

/**
 * 快递实体类，继承BaseEntity
 */
public class Express extends BaseEntity {
    // 快递状态常量
    public static final String STATUS_PENDING = "待取件";
    public static final String STATUS_FINISHED = "已取件";

    private String trackingNo;      // 快递单号
    private String company;         // 快递公司
    private String receiverName;    // 收件人姓名
    private String receiverPhone;   // 收件人电话（11位）
    private String shelfNo;         // 存放货架编号
    private String pickCode;        // 4位取件码
    private String status;          // 状态：待取件/已取件

    // 无参构造
    public Express() {
    }

    /**
     * 入库构造方法：自动生成4位取件码、设置状态待取件、入库时间
     */
    public Express(String trackingNo, String company, String receiverName, String receiverPhone, String shelfNo, Date createTime) {
        super(createTime, null);
        this.trackingNo = trackingNo;
        this.company = company;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.shelfNo = shelfNo;
        this.status = STATUS_PENDING;
        // 随机生成4位取件码
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        this.pickCode = String.valueOf(code);
    }

    // Getter & Setter
    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getShelfNo() {
        return shelfNo;
    }

    public void setShelfNo(String shelfNo) {
        this.shelfNo = shelfNo;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 实现抽象方法：打印快递完整信息
     */
    @Override
    public void displayInfo() {
        System.out.println("----------快递信息----------");
        System.out.println("单号：" + this.trackingNo);
        System.out.println("快递公司：" + this.company);
        System.out.println("收件人：" + this.receiverName);
        System.out.println("电话：" + this.receiverPhone);
        System.out.println("存放货架：" + this.shelfNo);
        System.out.println("取件码：" + this.pickCode);
        System.out.println("入库时间：" + this.getCreateTime());
        System.out.println("出库时间：" + (this.getUpdateTime() == null ? "未出库" : this.getUpdateTime()));
        System.out.println("状态：" + this.status);
        System.out.println("----------------------------");
    }

    @Override
    public String toString() {
        return trackingNo + "\t" + company + "\t" + receiverName + "\t" + receiverPhone + "\t" + shelfNo + "\t" + getCreateTime() + "\t" + status;
    }
}