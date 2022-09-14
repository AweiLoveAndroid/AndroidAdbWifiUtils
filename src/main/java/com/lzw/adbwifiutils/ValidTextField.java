package com.lzw.adbwifiutils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * 具有校验功能的输入框
 */
public class ValidTextField extends TextField {

    private IntegerProperty maxLength = new SimpleIntegerProperty();

    public ValidTextField() {
     this(-1, null);
    }

    public ValidTextField(Integer maxLength) {
        this(maxLength, null);
    }

    public ValidTextField(Integer maxLength, String defaultValue){
        this(maxLength, defaultValue, "");
    }

    public ValidTextField(Integer maxLength, String defaultValue, String text){

        setEditable(true);

        if(isNotBlank(text)){
            setPromptText(text);
        }

        // 限制最大长度
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!checkValueLengthValid(newValue)){
                    setText(oldValue);
                }
            }
        });

        if(maxLength == null){
            maxLength = -1;
        }
        setMaxLength(maxLength);

        if(isNotBlank(defaultValue)){
            setText(defaultValue);
        }

    }

    protected boolean checkValueLengthValid(final String value) {
        if(getMaxLength() != null
                && getMaxLength() > 0
                && isNotBlank(value)
                && lenOfChineseString(value) > getMaxLength()){
            return false;
        }
        return true;
    }

    public IntegerProperty getMaxLengthProperty(){
        return maxLength;
    }

    public Integer getMaxLength() {
        return maxLength.get();
    }

    public void setMaxLength(Integer max) {
        this.maxLength.set(max);
    }

    public boolean isNotBlank(final String value) {
        return (value != null && "".equalsIgnoreCase(value));
    }

    /**
     * 计算中文字符串长度
     * @param value
     * @return
     */
    protected int lenOfChineseString(final String value) {
       int len = 0;
       for(int i = 0; i < value.length(); i++){
           char c = value.charAt(i);
           if(c > '\u4e00' && c < '\u9fa5'){
               len += 2;
           } else {
               len++;
           }
       }
       return len;
    }
}
