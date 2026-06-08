package com.express.model.repository;

import com.express.model.Express;
import java.util.Date;

/**
 * 快递数据仓库：固定长度数组存储快递，维护有效数量
 */
public class ExpressRepository {
    // 仓库最大容量200件
    public static final int MAX_CAPACITY = 200;
    // 存储快递的数组
    private Express[] expressArray;
    // 当前有效快递总数（静态统计）
    public static int totalExpresses = 0;

    public ExpressRepository() {
        expressArray = new Express[MAX_CAPACITY];
    }

    /**
     * 添加快递入库
     * @param express 待入库快递对象
     * @return 0=成功，1=单号重复，2=仓库已满
     */
    public int addExpress(Express express) {
        // 判断仓库是否满
        if (totalExpresses >= MAX_CAPACITY) {
            return 2;
        }
        // 遍历判断单号是否重复
        for (int i = 0; i < totalExpresses; i++) {
            if (expressArray[i].getTrackingNo().equals(express.getTrackingNo())) {
                return 1;
            }
        }
        // 存入数组，总数+1
        expressArray[totalExpresses] = express;
        totalExpresses++;
        return 0;
    }

    /**
     * 根据单号精确查询快递
     * @param trackingNo 快递单号
     * @return 找到返回Express对象，没找到返回null
     */
    public Express findByTrackingNo(String trackingNo) {
        for (int i = 0; i < totalExpresses; i++) {
            if (expressArray[i].getTrackingNo().equals(trackingNo)) {
                return expressArray[i];
            }
        }
        return null;
    }

    /**
     * 根据收件人姓名模糊匹配查询
     * @param name 收件人姓名关键词
     * @return 匹配的快递数组
     */
    public Express[] findByName(String name) {
        // 先统计匹配数量
        int matchCount = 0;
        for (int i = 0; i < totalExpresses; i++) {
            if (expressArray[i].getReceiverName().contains(name)) {
                matchCount++;
            }
        }
        Express[] result = new Express[matchCount];
        int index = 0;
        for (int i = 0; i < totalExpresses; i++) {
            if (expressArray[i].getReceiverName().contains(name)) {
                result[index++] = expressArray[i];
            }
        }
        return result;
    }

    /**
     * 获取全部快递数组
     */
    public Express[] findAll() {
        Express[] all = new Express[totalExpresses];
        System.arraycopy(expressArray, 0, all, 0, totalExpresses);
        return all;
    }

    /**
     * 获取所有待取件快递
     */
    public Express[] findPending() {
        int pendingCount = 0;
        for (int i = 0; i < totalExpresses; i++) {
            if (expressArray[i].getStatus().equals(Express.STATUS_PENDING)) {
                pendingCount++;
            }
        }
        Express[] pendingArr = new Express[pendingCount];
        int idx = 0;
        for (int i = 0; i < totalExpresses; i++) {
            if (expressArray[i].getStatus().equals(Express.STATUS_PENDING)) {
                pendingArr[idx++] = expressArray[i];
            }
        }
        return pendingArr;
    }

    /**
     * 更新快递状态为已取件、更新出库时间
     * @param trackingNo 快递单号
     * @param newStatus 新状态
     * @param outTime 出库时间
     * @return true更新成功，false失败（单号不存在）
     */
    public boolean updateStatus(String trackingNo, String newStatus, Date outTime) {
        Express target = findByTrackingNo(trackingNo);
        if (target == null) {
            return false;
        }
        target.setStatus(newStatus);
        target.setUpdateTime(outTime);
        return true;
    }
}