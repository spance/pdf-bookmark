package com.ifnoelse.pdf;

import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XAlert {

    public static final XAlert OK = new XAlert("添加目录成功！", "文件存储在【%s】", Alert.AlertType.INFORMATION);
    public static final XAlert BAD_PDF = new XAlert("PDF失败", "PDF已加密，无法完成修改", Alert.AlertType.ERROR);
    public static final XAlert MISS_PDF = new XAlert("PDF文件", "文件找不到或不存在", Alert.AlertType.ERROR);
    public static final XAlert UNKNOWN_PDF = new XAlert("PDF文件", "添加目录错误 %s", Alert.AlertType.ERROR);
    public static final XAlert MISS_CONTENT = new XAlert("文本", "目录能容不能为空,请填写pdf书籍目录url或者填入目录文本", Alert.AlertType.ERROR);
    public static final XAlert BAD_OFFSET = new XAlert("偏移量", "页码偏移量只能为整数", Alert.AlertType.ERROR);

    String type;
    String message;
    Alert.AlertType alertType;


    public XAlert apply(String... args) {
        String m = String.format(message, args);
        return new XAlert(type, m, alertType);
    }

    public void show() {
        Alert alert = new Alert(alertType);
        alert.setContentText(type);
        alert.setHeaderText(message);

        String title;
        switch (alertType) {
            case INFORMATION:
                title = "通知";
                break;
            case ERROR:
                title = "错误";
                break;
            default:
                title = alertType.name();
        }
        alert.setTitle(title);
        alert.show();
    }

}
