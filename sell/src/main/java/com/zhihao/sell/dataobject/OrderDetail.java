package com.zhihao.sell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Entity Class OrderDetail, which contains all the information of the details of an order.
 */
// Entity class, mapping to db table. Map to order_detail by default
@Entity

// Lombok, creating getters and setters by default.
@Data
public class OrderDetail {

  /**
   * ID of the detail. Map to column detail_id by default. The result is the same
   */
  @Id
  private String detailId;

  /**
   * ID of the order.
   */
  private String orderId;

  /**
   * ID of the product.
   */
  private String productId;

  /**
   * Name of the product.
   */
  private String productName;

  /**
   * Price of the product.
   */
  private BigDecimal productPrice;

  /**
   * Quantity of the product.
   */
  private Integer productQuantity;

  /**
   * Icon of the product.
   */
  private String productIcon;
}
