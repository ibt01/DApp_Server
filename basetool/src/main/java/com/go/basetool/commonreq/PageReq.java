package com.go.basetool.commonreq;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

import javax.persistence.criteria.CriteriaBuilder;

@Data
public class PageReq {
    Integer page;
    Integer pageSize;

    public static PageRequest of(PageReq pageReq) {
        return PageRequest.of(pageReq.page, pageReq.pageSize);
    }
}
