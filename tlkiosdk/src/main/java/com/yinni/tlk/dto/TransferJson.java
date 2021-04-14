package com.yinni.tlk.dto;

public class TransferJson {
    private String from;
    private String to;
    private String quantity;
    private String memo;

    public TransferJson(String from,
                        String to,
                        String quantity,
                        String memo){
        this.from = from;
        this.to = to;
        this.quantity = quantity;
        this.memo = memo;

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}