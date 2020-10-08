package com.soft1851.user.center.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    @Column(name = "wx_id")
    private String wxId;
    @Column(name = "wx_nickname")
    private String wxNickname;
    @Column(name = "roles")
    private String roles;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Column(name = "bonus")
    private Integer bonus;
    @Column(name = "account")
    private String account;
    @Column(name = "password")
    private String password;
}