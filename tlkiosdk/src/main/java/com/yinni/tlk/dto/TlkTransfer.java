package com.yinni.tlk.dto;

import com.google.gson.annotations.Expose;
import com.yinni.tlk.crypto.util.HexUtils;
import com.yinni.tlk.tlktypes.TLKByteWriter;
import com.yinni.tlk.tlktypes.TLKType;
import com.yinni.tlk.tlktypes.TypeAccountName;
import com.yinni.tlk.tlktypes.TypeAsset;


public class TlkTransfer implements TLKType.Packer {
    @Expose
    private TypeAccountName from;

    @Expose
    private TypeAccountName to;

    @Expose
    private TypeAsset quantity;

    @Expose
    private String memo;

    public TlkTransfer(String from, String to, long quantity, String memo ) {
        this( new TypeAccountName(from), new TypeAccountName(to), quantity, memo );
    }

    public TlkTransfer(TypeAccountName from, TypeAccountName to, long quantity, String memo ) {
        this.from = from;
        this.to = to;
        this.quantity = new TypeAsset(quantity);
        this.memo = memo != null ? memo : "";
    }

    public String getActionName() {
        return "transfer";
    }


    @Override
    public void pack(TLKType.Writer writer) {

        from.pack(writer);
        to.pack(writer);

        writer.putLongLE(quantity.getAmount());

        writer.putString(memo);
    }

    public String getAsHex() {
        TLKType.Writer writer = new TLKByteWriter(128);
        pack(writer);

        return HexUtils.toHex( writer.toBytes() );
    }
}
