package com.yinni.tlk.tlktypes;

public class TLKBuyRam {

    private String payer;

    private String receiver;

    private String quant;

    public String getActionName() {
        return "buyram";
    }

    public TLKBuyRam(String payer, String receiver, String quant ) {
//        this.payer = new TypeAccountName(payer);
//        this.receiver = new TypeAccountName(receiver);
        this.payer = payer;
        this.receiver = receiver;
        this.quant = quant;
    }
}
