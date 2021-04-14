package com.yinni.tlk.dto;

import lombok.Data;

import java.util.List;

@Data
public class Block {
    private String timestamp;
    private Long block_num;
    private String id;
    private String producer;
    private Long confirmed;
    private String previous;
    private List<Transaction> transactions;
}
