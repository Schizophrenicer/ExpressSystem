package com.express.view;

import com.express.model.Express;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;

/**
 * 视图层：处理控制台输入、打印所有界面、提示信息
 */
public class ExpressView {
    private Scanner scanner;

    public ExpressView() {
        scanner = new Scanner(System.in);
    }

    /**
     * 打印主菜单
     */
    public void showMenu() {
        System.out.println("========== 快递驿站管理系统 ==========");
        System.out.println("1. 快递入库登记");
        System.out.println("2. 取件出库");
        System.out.println("3. 显示所有快递");
        System.out.println("4. 按快递单号查询");
        System.out.println("5. 按收件人姓名查询");
        System.out.println("6. 待取快递列表");
        System.out.println("7. 超期未取提醒");
        System.out.println("8. 驿站数据统计");
        System.out.println("0. 退出系统");
        System.out.println("======================================");
        System.out.print("请选择：");
    }

    /**
     * 读取菜单选择，校验0-8数字
     * @return 合法菜单数字；输入错误返回-1
     */
    public int readChoice() {
        String input = scanner.nextLine();
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
        if (choice < 0 || choice > 8) {
            return -1;
        }
        return choice;
    }

    // ========== 入库相关输入 ==========
    public String inputTrackingNo() {
        while (true) {
            System.out.print("请输入快递单号：");
            String no = scanner.nextLine().trim();
            if (no.isEmpty()) {
                showMessage("快递单号不能为空！");
                continue;
            }
            return no;
        }
    }

    public String inputCompany() {
        while (true) {
            System.out.print("请输入快递公司：");
            String company = scanner.nextLine().trim();
            if (company.isEmpty()) {
                showMessage("快递公司不能为空！");
                continue;
            }
            return company;
        }
    }

    public String inputReceiverName() {
        while (true) {
            System.out.print("请输入收件人姓名：");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                showMessage("收件人姓名不能为空！");
                continue;
            }
            return name;
        }
    }

    public String inputReceiverPhone() {
        while (true) {
            System.out.print("请输入收件人电话：");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                showMessage("收件人电话不能为空！");
                continue;
            }
            if (phone.length() != 11 || !phone.matches("\\d{11}")) {
                showMessage("收件人电话必须为11位数字！");
                continue;
            }
            return phone;
        }
    }

    public String inputShelfNo() {
        while (true) {
            System.out.print("请输入存放货架编号：");
            String shelf = scanner.nextLine().trim();
            if (shelf.isEmpty()) {
                showMessage("存放货架编号不能为空！");
                continue;
            }
            return shelf;
        }
    }

    // ========== 取件相关输入 ==========
    public String inputPickName() {
        while (true) {
            System.out.print("请确认取件人姓名：");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                showMessage("取件人姓名不能为空！");
                continue;
            }
            return name;
        }
    }

    public String inputPickCode() {
        while (true) {
            System.out.print("请输入取件码（短信发送）：");
            String code = scanner.nextLine().trim();
            if (code.isEmpty()) {
                showMessage("取件码不能为空！");
                continue;
            }
            if (code.length() != 4 || !code.matches("\\d{4}")) {
                showMessage("取件码必须为4位数字！");
                continue;
            }
            return code;
        }
    }

    // ========== 打印各类界面 ==========
    /**
     * 打印单个快递详情
     */
    public void displayExpress(Express express) {
        express.displayInfo();
    }

    /**
     * 表格打印全部快递
     */
    public void displayAllExpress(Express[] allExpress) {
        System.out.println("==================== 所有快递列表 ====================");
        System.out.printf("%-15s %-8s %-8s %-13s %-6s %-20s %s%n",
                "单号", "快递公司", "收件人", "电话", "货架", "入库时间", "状态");
        System.out.println("------------------------------------------------------------------------------------------------");
        if (allExpress.length == 0) {
            System.out.println("暂无快递记录！");
        } else {
            for (Express exp : allExpress) {
                System.out.printf("%-15s %-8s %-8s %-13s %-6s %-20s %s%n",
                        exp.getTrackingNo(),
                        exp.getCompany(),
                        exp.getReceiverName(),
                        exp.getReceiverPhone(),
                        exp.getShelfNo(),
                        exp.getCreateTime(),
                        exp.getStatus());
            }
            System.out.println("共 " + allExpress.length + " 件快递，其中待取件 " + countPending(allExpress) + " 件");
        }
        System.out.println("==================================================================================================");
    }

    // 统计数组里待取件数量
    private int countPending(Express[] arr) {
        int cnt = 0;
        for (Express e : arr) {
            if (e.getStatus().equals(Express.STATUS_PENDING)) cnt++;
        }
        return cnt;
    }

    /**
     * 打印待取件快递列表
     */
    public void displayPendingExpress(Express[] pendingArr) {
        System.out.println("==================== 待取快递列表 ====================");
        System.out.printf("%-15s %-8s %-8s %-13s %-6s %-20s%n",
                "单号", "快递公司", "收件人", "电话", "货架", "入库时间");
        System.out.println("----------------------------------------------------------------------------------------");
        if (pendingArr.length == 0) {
            System.out.println("暂无待取件快递！");
        } else {
            for (Express exp : pendingArr) {
                System.out.printf("%-15s %-8s %-8s %-13s %-6s %-20s%n",
                        exp.getTrackingNo(),
                        exp.getCompany(),
                        exp.getReceiverName(),
                        exp.getReceiverPhone(),
                        exp.getShelfNo(),
                        exp.getCreateTime());
            }
            System.out.println("共 " + pendingArr.length + " 件快递待取件");
        }
        System.out.println("========================================================================================");
    }

    /**
     * 打印姓名查询结果列表
     */
    public void displayNameSearchResult(Express[] matchArr) {
        System.out.println("==================== 查询结果 ====================");
        System.out.printf("%-15s %-8s %-6s %-20s %s%n",
                "单号", "快递公司", "货架", "入库时间", "状态");
        System.out.println("--------------------------------------------------------");
        if (matchArr.length == 0) {
            System.out.println("未找到该收件人的快递！");
        } else {
            for (Express exp : matchArr) {
                System.out.printf("%-15s %-8s %-6s %-20s %s%n",
                        exp.getTrackingNo(),
                        exp.getCompany(),
                        exp.getShelfNo(),
                        exp.getCreateTime(),
                        exp.getStatus());
            }
            System.out.println("共找到 " + matchArr.length + " 件快递");
        }
        System.out.println("========================================================");
    }

    /**
     * 打印超期未取列表
     */
    public void displayOverdue(List<Express> overdueList) {
        System.out.println("==================== 超期未取提醒（超期标准：3天） ====================");
        System.out.printf("%-6s %-15s %-8s %-8s %-13s %-20s%n",
                "超期天数", "单号", "快递公司", "收件人", "电话", "入库时间");
        System.out.println("----------------------------------------------------------------------------------------");
        if (overdueList.isEmpty()) {
            System.out.println("暂无超期未取快递！");
        } else {
            Date now = new Date();
            for (Express exp : overdueList) {
                long diffMs = now.getTime() - exp.getCreateTime().getTime();
                long diffDay = diffMs / (24L * 60 * 60 * 1000);
                System.out.printf("%-6d %-15s %-8s %-8s %-13s %-20s%n",
                        diffDay,
                        exp.getTrackingNo(),
                        exp.getCompany(),
                        exp.getReceiverName(),
                        exp.getReceiverPhone(),
                        exp.getCreateTime());
            }
            System.out.println("共 " + overdueList.size() + " 件快递超期未取");
        }
        System.out.println("========================================================================================");
    }

    /**
     * 打印驿站统计数据
     */
    public void displayStatistics(Map<String, Object> statMap) {
        int total = (Integer) statMap.get("total");
        int finished = (Integer) statMap.get("finished");
        int pending = (Integer) statMap.get("pending");
        int overdue = (Integer) statMap.get("overdue");
        double pickRate = (Double) statMap.get("pickRate");
        Map<String, Integer> companyCount = (Map<String, Integer>) statMap.get("companyCount");

        System.out.println("==================== 驿站数据统计 ====================");
        System.out.println("快递总数：" + total + " 件");
        System.out.println("已取件：" + finished + " 件");
        System.out.println("待取件：" + pending + " 件");
        System.out.println("超期未取：" + overdue + " 件");
        System.out.printf("取件率：%.2f%%%n", pickRate * 100);
        System.out.println("------------------------------------------------------");
        System.out.println("各快递公司数量：");
        if (companyCount.isEmpty()) {
            System.out.println("暂无快递数据");
        } else {
            for (Map.Entry<String, Integer> entry : companyCount.entrySet()) {
                System.out.println(entry.getKey() + "：" + entry.getValue() + " 件");
            }
        }
        System.out.println("======================================================");
    }

    /**
     * 统一打印提示消息
     */
    public void showMessage(String msg) {
        System.out.println("【提示】" + msg);
    }

    /**
     * 暂停，按任意键返回菜单
     */
    public void pause() {
        System.out.println("按回车键返回主菜单...");
        scanner.nextLine();
    }
}