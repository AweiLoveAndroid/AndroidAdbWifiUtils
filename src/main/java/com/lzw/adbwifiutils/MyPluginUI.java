package com.lzw.adbwifiutils;

import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;

/**
 * 在IDEA Plugin中使用javafx组件(注意：不支持fxml读取，但是可以使用javafx组件)
 */
public class MyPluginUI {

    static MyPluginUIController myPluginUIController = MyPluginUIController.getInstance();

    public static void initUI(ToolWindow toolWindow){
        final JFXPanel fxPanel = new JFXPanel();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
//            fxPanel.setToolTipText("TestToolWindowFactory");
            fxPanel.setScene(initScene(540,800));
        });
        fxPanel.setPreferredSize(new Dimension(540,800));
        toolWindow.getComponent().getParent().add(fxPanel);
    }

    private static Scene initScene(double width, double height) {
        Group root  =  new Group();
        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.valueOf("#ECF3FF"));

        scene.getStylesheets().add(MyPluginUI.class.getResource("/css/Login.css").toExternalForm());

        // 添加子节点
//        root.getChildren().add(initChildsEasy());
        root.getChildren().add(initChildsHard(width, height));
        return scene;
    }

    /**
     * 子节点具体内容(先用一个Text试试效果)
     * @return
     */
    private static Node initChildsEasy() {
        Text text  =  new Text();
        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("卢本伟做的IDEA插件就是牛逼！");
        return text;
    }


    /**
     * 子节点具体内容(复杂的布局，相当于sample.fxml的布局内容，这里用纯javafx组件写出来的)
     * @return
     */
    private static Node initChildsHard(double width, double height) {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(width);
        anchorPane.setPrefHeight(height);


        // 检查连接按钮
        Button checkConnectButton = new Button("检查连接状态");
        initNodeAttrs(checkConnectButton, 23.0,49.0,200.0,50.0);
//        checkConnectButton.setFont(new Font(16.0));
//        checkConnectButton.setStyle("-fx-background-color: #539DFF; -fx-text-fill: #FFFFFF;");

        // 显示连接结果的文本框
        TextArea connectResultTextArea = new TextArea();
        initNodeAttrs(connectResultTextArea,23.0,115.0,200.0,300.0);
        connectResultTextArea.setEditable(false);
        connectResultTextArea.setId("connectResultTextArea");


        // 绑定端口号按钮
        Button bindPortButton = new Button("绑定端口号");
        initNodeAttrs(bindPortButton,242.0,227.0,212.0,50.0);
        bindPortButton.setMnemonicParsing(false);
//        bindPortButton.setFont(new Font(16.0));
//        bindPortButton.setStyle("-fx-background-color: #539DFF; -fx-text-fill: #FFFFFF;");

        // 连接Wifi按钮
        Button connectWiFiButton = new Button("连接Wifi");
        initNodeAttrs(connectWiFiButton,242.0,364.0,212.0,50.0);
        connectWiFiButton.setMnemonicParsing(false);
//        connectWiFiButton.setFont(new Font(16.0));
//        connectWiFiButton.setStyle("-fx-background-color: #539DFF; -fx-text-fill: #FFFFFF;");

        // 标题
        Label titleLabel = new Label("AdbWifi连接工具");
        initNodeAttrs(titleLabel,201.0,11.0,-1,-1);
//        titleLabel.setFont(new Font(20.0));
//        titleLabel.setTextFill(Color.valueOf("#6d9eeb"));
        titleLabel.setId("titleLabel");

        // 校验端口合法性
        Button portValidButton = new Button("校验端口合法性");
        initNodeAttrs(portValidButton,242.0,156.0,212.0,50.0);
        portValidButton.setMnemonicParsing(false);
//        portValidButton.setFont(new Font(16.0));
//        portValidButton.setStyle("-fx-background-color: #539DFF; -fx-text-fill: #FFFFFF;");

        // 校验ip地址合法性
        Button ipValidButton = new Button("校验IP地址合法性");
        initNodeAttrs(ipValidButton,242.0,296.0,212.0,50.0);
        ipValidButton.setMnemonicParsing(false);
//        ipValidButton.setFont(new Font(16.0));
//        ipValidButton.setStyle("-fx-background-color: #539DFF; -fx-text-fill: #FFFFFF;");


        /////////////////////////////// 端口号和IP地址输入框：///////////////////////////

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(242.0);
        gridPane.setLayoutY(49.0);
        gridPane.setPrefWidth(212.0);
        gridPane.setPrefHeight(100.0);

        Label portLabel = new Label("端口:");
        portLabel.setAlignment(Pos.BASELINE_RIGHT);
        portLabel.setFont(new Font(16.0));
        portLabel.setPrefWidth(60.0);


        ValidTextField portTextField = new ValidTextField(15,"","8888");
        portTextField.setPrefWidth(136.0);
        portTextField.setPrefHeight(30.0);
        portTextField.setAlignment(Pos.CENTER);
//        portTextField.setStyle("-fx-border-color:#8DD4FF; -fx-border-radius:5; -fx-border-width:1;");
//        portTextField.setId("portTextField");
        GridPane.setMargin(portTextField, new Insets(0,0,0,20.0));

        Label ipAddressLabel = new Label("IP地址:");
        ipAddressLabel.setAlignment(Pos.BASELINE_RIGHT);
        ipAddressLabel.setFont(new Font(16.0));
        ipAddressLabel.setPrefWidth(60.0);
        GridPane.setMargin(ipAddressLabel, new Insets(20.0,0,0,0));


        ValidTextField ipAddressTextField = new ValidTextField(15,"","192.168.111.111");
        ipAddressTextField.setPrefWidth(136.0);
        ipAddressTextField.setPrefHeight(30.0);
        ipAddressTextField.setAlignment(Pos.CENTER);
//        ipAddressTextField.setStyle("-fx-border-color:#8DD4FF; -fx-border-radius:5; -fx-border-width:1;");
//        ipAddressTextField.setId("ipAddressTextField");
        GridPane.setMargin(ipAddressTextField, new Insets(20.0,0,0,20.0));


        GridPane.setHalignment(portLabel, HPos.RIGHT);
        GridPane.setHalignment(ipAddressLabel, HPos.RIGHT);


        gridPane.addRow(0, portLabel, portTextField);
        gridPane.addRow(1, ipAddressLabel, ipAddressTextField);



        // 点击事件和校验逻辑操作
        checkConnectButton.setOnAction(event -> {
            myPluginUIController.checkConnectedDeviceList(connectResultTextArea);
        });

        // 校验端口
        portValidButton.setOnAction(event -> {
            myPluginUIController.validPort(portTextField);
        });

        // 绑定端口
        bindPortButton.setOnAction(event -> {
            myPluginUIController.bindPort(portTextField,connectResultTextArea);
        });

        // 校验ip地址合法性
        ipValidButton.setOnAction(event -> {
            myPluginUIController.validIPAddress(ipAddressTextField);
        });

        // 连接Wifi
        connectWiFiButton.setOnAction(event -> {
            myPluginUIController.connectWifi(connectResultTextArea, portTextField, ipAddressTextField);
        });


        anchorPane.getChildren().addAll(
                checkConnectButton,
                connectResultTextArea,
//                portLabel,
//                ipLabel,
//                portTextField,
//                ipAddressTextField,
                gridPane,
                bindPortButton,
                connectWiFiButton,
                titleLabel,
                portValidButton,
                ipValidButton
        );

        return anchorPane;
    }

    private static void initNodeAttrs(Region region, double x, double y, double width, double height){
        region.setLayoutX(x);
        region.setLayoutY(y);
        region.setPrefWidth(width);
        region.setPrefHeight(height);
    }
}
