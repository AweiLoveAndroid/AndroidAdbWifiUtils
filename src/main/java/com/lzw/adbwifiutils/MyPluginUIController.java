package com.lzw.adbwifiutils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MyPluginUIController {

    private String result;
    private String ipDatas;
    private String portDatas;

    private MyPluginUIController() {
    }

    private static class SingletonInstance {
        private static final MyPluginUIController INSTANCE = new MyPluginUIController();
    }

    public static MyPluginUIController getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public String checkConnectedDeviceList(TextArea connectResult){
        connectResult.clear();
        new Thread(){
            @Override
            public void run() {
                Runtime r = Runtime.getRuntime();
                try {
                    Process proc = r.exec("adb devices"); // 假设该操作为造成大量内容输出
                    // 采用字符流读取缓冲池内容，腾出空间
                    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));

                    while ((result = reader.readLine()) != null) {
                        result = result + "\r\n";
                        System.out.print(result);
                        connectResult.appendText(result);
                    }
                /* 或采用字节流读取缓冲池内容，腾出空间
                 ByteArrayOutputStream pool = new ByteArrayOutputStream();
                 byte[] buffer = new byte[1024];
                 int count = -1;
                 while ((count = proc.getInputStream().read(buffer)) != -1){
                   pool.write(buffer, 0, count);
                   buffer = new byte[1024];
                 }
                 System.out.println(pool.toString("gbk"));
                 */
//                int exitVal = proc.waitFor();
//                System.out.println(exitVal == 0 ? "成功" : "失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return result;
    }

    /**
     * 使用Platform.runLater来解决高频调用的时候，UI刷新频率过快导致包空指针异常的问题
     * @param area
     * @param content
     */
    private void justifyTextArea(TextArea area, String content){
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                //用Platform.runLater来运行需要高频调用的方法
                area.appendText(content);
            }
        });
    }



    /**
     * 校验端口
     * @param portNameTextField
     * @return
     */
    public boolean validPort(TextField portNameTextField) {
        String curPort = portNameTextField.getText().toString();
        String regex = "^[0-9]|[1-9][0-9][0-9]{0,2}|[1-5][0-9]{4}|6([0-4][0-9]{3}|5([0-4][0-9]{2}|5([0-2][0-9]|3[0-5])))$";

        if(!textIsNull(curPort)){
            if (curPort.matches(regex)) {
                showTipDialog("校验端口号", curPort, true);
                return true;
            } else {
                showTipDialog("校验端口号", curPort, false);
                return false;
            }
        } else {
            showTipDialog("校验端口号", curPort, false);
        }
        return false;
    }

    /**
     * 点击绑定端口按钮
     * @param portTextField 输入框控件用于输入端口号
     * @param connectResultTextArea 显示结果
     */
    public void bindPort(TextField portTextField, TextArea connectResultTextArea) {
        try {
            portDatas = portTextField.getText();
            if(validPort(portTextField)){
                Runtime r = Runtime.getRuntime();
                Process proc = r.exec("adb tcpip " + portDatas);
                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));

                while ((result = reader.readLine()) != null) {
                    System.out.print(result);
                    justifyTextArea(connectResultTextArea, result);
                }
                showTipDialog("绑定端口", portDatas, true);
            } else {
                showTipDialog("绑定端口", portDatas, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验ip地址合法性
     * @param ipAddressTextField
     * @return
     */
    public boolean validIPAddress(TextField ipAddressTextField) {
        String ip = ipAddressTextField.getText().toString();
        if (!textIsNull(ip)) {
            //定义正则表达式。
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])"
                    + "(\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)){3}$";
            // 判断IP地址是否与正则表达式匹配。
            if (ip.matches(regex)) {
                ipDatas = ip;
                return true;
            } else {
                showTipDialog("校验IP地址", ip, true);
                return false;
            }
        } else {
            showTipDialog("校验IP地址", ip, false);
        }
        return false;
    }

    public void connectWifi(TextArea connectResultTextArea,TextField portTextField,TextField ipAddressTextField) {
        Runtime r = Runtime.getRuntime();
        try {
            if(validPort(portTextField) && validIPAddress(ipAddressTextField)){
                Process proc = r.exec("adb connect " + ipDatas + ":" + portDatas);
                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));

                while ((result = reader.readLine()) != null) {
                    System.out.print(result);
                    justifyTextArea(connectResultTextArea, result);
                }
            }
            if(!validPort(portTextField) && !validIPAddress(ipAddressTextField)){
                showErrorDialog("端口",portDatas,"IP地址", ipDatas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showTipDialog(String type, String contents, boolean isErrorTip){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getScene().getWindow();
        alert.setTitle(type);
        alert.setHeaderText(type);
        if(isErrorTip){
            alert.setContentText("输入的内容是:[" + contents + "], " + type +  "成功！");
        } else {
            alert.setContentText("输入的内容是:[" + contents + "], " + type +  "失败！");
        }
        alert.showAndWait();
    }

    /**
     * 错误提醒的弹窗
     */
    private void showErrorDialog(String type, String inputs, String type2, String inputs2){
        String results = "";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getScene().getWindow();
        alert.setTitle("校验输入内容");
        alert.setHeaderText("校验" + type);

//        if(type != null && inputs == null){
//            results = "您输入的" + type + "[" + inputs + "]属于无效值，请检查之后重新输入!";
//        }
//        if(type2 != null && inputs2 == null){
//            results = "您输入的" + type + "[" + inputs + "]属于无效值，请检查之后重新输入!";
//        }
        if(type != null && inputs == null && type2!=null && inputs2 == null){
            results = "您输入的 " + type + "[" + inputs + "],以及 " + type2 + "[" + inputs2 + "] 属于无效值，请检查之后重新输入!";
        }
        alert.setContentText(results);
        alert.showAndWait();
    }

    private boolean textIsNull(String text){
        return text==null || text.isEmpty() || text.equals("");
    }
}
