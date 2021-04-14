package com.yinni.tlk.dto;

import lombok.Data;

@Data
public class BlockInfo {
    Long head_block_num;
    Long last_irreversible_block_num;
}
