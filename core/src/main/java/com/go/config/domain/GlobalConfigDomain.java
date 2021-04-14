package com.go.config.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "globalconfigdomain")
public class GlobalConfigDomain {
    @Id
    @Column(name = "keyx", length = 200)
    String key;

    @Column(name = "valuex", length = 500)
    String value;

    @Column(name = "comment", length = 500)
    String comment;

    @Column(name = "typex")
    Integer type;//0 暴露给移动端 1后台私密配置.
    @Column(name = "created")
    private Date created;

    @Column(name = "updated")
    private Date updated;

    @PrePersist
    protected void onCreate() {
        created = new Date();
        updated = new Date();
    }
}
