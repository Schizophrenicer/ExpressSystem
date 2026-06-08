package com.express.service;

import com.express.model.Express;
import com.express.model.repository.ExpressRepository;
import java.util.*;

/**
 * 快递业务层：处理所有业务逻辑，调用Repository操作数据
 */
public class ExpressService {
    private ExpressRepository expressRepository;

    public ExpressService() {
        expressRepository = new ExpressRepository();
    }

    /**
     * 快递入库业务
     * @param trackingNo 单号
     * @param company 快递公司
     * @param receiverName 收件人名
     * @param receiverPhone 手机号
     * @param shelfNo 货架号
     * @return 0成功，1单号重复，2仓库满
     */
    public int addExpress(String trackingNo, String company, String receiverName, String receiverPhone, String shelfNo) {
        Date now = new Date();
        Express express = new Express(trackingNo, company, receiverName, receiverPhone, shelfNo, now);
        return expressRepository.addExpress(express);
    }

    /**
     * 根据单号查询快递
     */
    public Express queryByTrackingNo(String trackingNo) {
        return expressRepository.findByTrackingNo(trackingNo);
    }

    /**
     * 根据姓名查询快递
     */
    public Express[] queryByName(String name) {
        return expressRepository.findByName(name);
    }

    /**
     * 获取全部快递
     */
    public Express[] findAll() {
        return expressRepository.findAll();
    }

    /**
     * 获取所有待取件快递
     */
    public Express[] findPending() {
        return expressRepository.findPending();
    }

    /**
     * 取件出库业务校验
     * @param trackingNo 单号
     * @param pickName 取件人姓名
     * @param pickCode 取件码
     * @return 0成功；1单号不存在；2已取件；3姓名不匹配；4取件码错误
     */
    public int pickExpress(String trackingNo, String pickName, String pickCode) {
        Express express = expressRepository.findByTrackingNo(trackingNo);
        // 1. 判断单号是否存在
        if (express == null) {
            return 1;
        }
        // 2. 判断是否已经取件
        if (express.getStatus().equals(Express.STATUS_FINISHED)) {
            return 2;
        }
        // 3. 核对姓名
        if (!express.getReceiverName().equals(pickName)) {
            return 3;
        }
        // 4. 核对取件码
        if (!express.getPickCode().equals(pickCode)) {
            return 4;
        }
        // 全部校验通过，更新状态和出库时间
        Date outTime = new Date();
        expressRepository.updateStatus(trackingNo, Express.STATUS_FINISHED, outTime);
        return 0;
    }

    /**
     * 查询超期未取快递（入库超过3天），按超期天数降序
     */
    public List<Express> getOverdueExpress() {
        Express[] pendingArr = expressRepository.findPending();
        List<Express> overdueList = new ArrayList<>();
        Date now = new Date();
        long threeDayMs = 3L * 24 * 60 * 60 * 1000; // 3天毫秒数

        for (Express exp : pendingArr) {
            Date createTime = exp.getCreateTime();
            long diffMs = now.getTime() - createTime.getTime();
            if (diffMs > threeDayMs) {
                overdueList.add(exp);
            }
        }

        // 按超期天数从高到低排序
        overdueList.sort((e1, e2) -> {
            long diff1 = now.getTime() - e1.getCreateTime().getTime();
            long diff2 = now.getTime() - e2.getCreateTime().getTime();
            return Long.compare(diff2, diff1);
        });
        return overdueList;
    }

    /**
     * 驿站数据统计
     * 返回数组：[总件数，已取件，待取件，超期未取，取件率] + 各公司数量Map
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> statMap = new HashMap<>();
        Express[] allExpress = expressRepository.findAll();

        int total = allExpress.length;
        int finished = 0;
        int pending = 0;
        List<Express> overdueList = getOverdueExpress();
        int overdue = overdueList.size();

        Map<String, Integer> companyCount = new HashMap<>();

        for (Express exp : allExpress) {
            // 统计状态
            if (exp.getStatus().equals(Express.STATUS_FINISHED)) {
                finished++;
            } else {
                pending++;
            }
            // 统计快递公司
            String company = exp.getCompany();
            companyCount.put(company, companyCount.getOrDefault(company, 0) + 1);
        }

        // 计算取件率
        double pickRate = total == 0 ? 0 : (double) finished / total;

        statMap.put("total", total);
        statMap.put("finished", finished);
        statMap.put("pending", pending);
        statMap.put("overdue", overdue);
        statMap.put("pickRate", pickRate);
        statMap.put("companyCount", companyCount);
        return statMap;
    }
}