package com.lzc.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-04
 */
@Data
@TableName("t_user")
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;

    private String nickname;

    private String password;

    private String slat;

    private String head;

    private Date registerTime;

    private Date lastLoginTime;

    private Integer loginCount;


}
