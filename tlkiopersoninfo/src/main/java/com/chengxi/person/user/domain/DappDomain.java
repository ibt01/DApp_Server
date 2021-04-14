package com.chengxi.person.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Slf4j
@Data
@Entity
@Table(name = "dapp")
public class DappDomain {
    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "url")
    String url;

    @Column(name = "pic_url")
    String pic_url;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;
}
