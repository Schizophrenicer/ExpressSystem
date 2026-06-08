package com.express.controller;

import com.express.model.Express;
import com.express.service.ExpressService;
import com.express.view.ExpressView;
import java.util.List;
import java.util.Map;

/**
 * 控制器：接收用户输入，调用Service处理业务，调用View展示界面
 */
public class ExpressController {
    private ExpressService expressService;
    private ExpressView expressView;

    public ExpressController() {
        expressService = new ExpressService();
        expressView = new ExpressView();
    }

    /**
     * 系统主循环：菜单驱动
     */
    public void start() {
        while (true) {
            // 展示菜单
            expressView.showMenu();
            // 读取用户选择
            int choice = expressView.readChoice();
            if (choice == -1) {
                expressView.showMessage("输入有误，请输入0-8之间的数字！");
                expressView.pause();
                continue;
            }
            // 分支处理各个功能
            switch (choice) {
                case 1:
                    handleAddExpress();
                    break;
                case 2:
                    handlePickExpress();
                    break;
                case 3:
                    handleShowAll();
                    break;
                case 4:
                    handleQueryByNo();
                    break;
                case 5:
                    handleQueryByName();
                    break;
                case 6:
                    handleShowPending();
                    break;
                case 7:
                    handleOverdueRemind();
                    break;
                case 8:
                    handleStatistics();
                    break;
                case 0:
                    expressView.showMessage("感谢使用快递驿站管理系统，再见！");
                    System.exit(0);
                    break;
            }
            expressView.pause();
        }
    }

    // 1. 快递入库
    private void handleAddExpress() {
        System.out.println("========== 快递入库登记 ==========");
        String no = expressView.inputTrackingNo();
        String company = expressView.inputCompany();
        String name = expressView.inputReceiverName();
        String phone = expressView.inputReceiverPhone();
        String shelf = expressView.inputShelfNo();

        int result = expressService.addExpress(no, company, name, phone, shelf);
        switch (result) {
            case 0:
                expressView.showMessage("快递入库成功！");
                break;
            case 1:
                expressView.showMessage("快递单号已存在，入库失败！");
                break;
            case 2:
                expressView.showMessage("快递库已满，无法入库！");
                break;
        }
    }

    // 2. 取件出库
    private void handlePickExpress() {
        System.out.println("========== 取件出库 ==========");
        String no = expressView.inputTrackingNo();
        Express target = expressService.queryByTrackingNo(no);
        if (target == null) {
            expressView.showMessage("未找到该快递！");
            return;
        }
        String pickName = expressView.inputPickName();
        String pickCode = expressView.inputPickCode();

        int res = expressService.pickExpress(no, pickName, pickCode);
        switch (res) {
            case 0:
                expressView.showMessage("取件成功！出库时间：" + new java.util.Date());
                break;
            case 1:
                expressView.showMessage("未找到该快递！");
                break;
            case 2:
                expressView.showMessage("该快递早已被取走！");
                break;
            case 3:
                expressView.showMessage("取件人姓名有误！");
                break;
            case 4:
                expressView.showMessage("取件码错误！");
                break;
        }
    }

    // 3. 显示全部快递
    private void handleShowAll() {
        Express[] all = expressService.findAll();
        expressView.displayAllExpress(all);
    }

    // 4. 单号查询
    private void handleQueryByNo() {
        System.out.println("========== 按快递单号查询 ==========");
        String no = expressView.inputTrackingNo();
        Express express = expressService.queryByTrackingNo(no);
        if (express == null) {
            expressView.showMessage("未找到该快递！");
        } else {
            expressView.displayExpress(express);
        }
    }

    // 5. 姓名查询
    private void handleQueryByName() {
        System.out.println("========== 按收件人姓名查询 ==========");
        String name = expressView.inputReceiverName();
        Express[] matchArr = expressService.queryByName(name);
        expressView.displayNameSearchResult(matchArr);
    }

    // 6. 待取件列表
    private void handleShowPending() {
        Express[] pending = expressService.findPending();
        expressView.displayPendingExpress(pending);
    }

    // 7. 超期提醒
    private void handleOverdueRemind() {
        List<Express> overdueList = expressService.getOverdueExpress();
        expressView.displayOverdue(overdueList);
    }

    // 8. 数据统计
    private void handleStatistics() {
        Map<String, Object> statData = expressService.getStatistics();
        expressView.displayStatistics(statData);
    }
}