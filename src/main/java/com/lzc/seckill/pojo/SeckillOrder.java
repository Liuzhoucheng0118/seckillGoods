package com.lzc.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_seckill_order")
@ApiModel(value="SeckillOrder对象", description="")
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀订单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;


}
