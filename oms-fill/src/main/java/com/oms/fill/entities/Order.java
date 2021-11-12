package com.oms.fill.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "oms_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "createdAt", "modifiedAt" })
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;
	@NotNull
	@Column(name = "client_id")
	private Long clientId;

	@NotNull
	@Column(name = "quantity")
	private Integer quantity;

	@NotNull
	@Column(name = "stock")
	private String stock;

	@Column(name = "stock_price")
	private BigDecimal stockPrice;

	@Column(name = "target_price")
	private BigDecimal targetPrice;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "modified_by")
	private Long modifiedBy;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "executed_at")
	private LocalDateTime executeAt;

	@Column(name = "quantity_filled")
	private Integer quantityFilled;

	@Column(name = "executed_price")
	private BigDecimal executedPrice;

	@Column(name = "exchange_id")
	private Long exchangeId;

	public enum Status {
		CREATED, ACCEPTED, REJECTED, CANCELLED;
	}

	public enum Side {
		BUY, SELL;
	}

	public enum Type {
		MARKET, LIMIT;
	}

	@Column(name = "status")
	private Status status;

	@NotNull
	@Column(name = "side")
	private Side side;

	@NotNull
	@Column(name = "type")
	private Type type;

	public void setSide(String side) {
		this.side = Side.valueOf(side.toUpperCase());
	}

	public void setType(String type) {
		this.type = Type.valueOf(type.toUpperCase());
	}

	// Automatically adds creation date, status on order creation
	@PrePersist
	public void onCreation() {
		this.createdAt = LocalDateTime.now();
		this.modifiedAt = this.createdAt;
		this.status = Status.CREATED;
		this.isActive = true;
	}

	public Order(@NotNull Long clientId, @NotNull Integer quantity, @NotNull String stock, BigDecimal stockPrice,
			BigDecimal targetPrice, LocalDateTime createdAt, LocalDateTime modifiedAt, Long createdBy, Long modifiedBy,
			Boolean isActive, LocalDateTime executeAt, Integer quantityFilled, BigDecimal executedPrice,
			Long exchangeId, Status status, @NotNull Side side, @NotNull Type type) {
		super();
		this.clientId = clientId;
		this.quantity = quantity;
		this.stock = stock;
		this.stockPrice = stockPrice;
		this.targetPrice = targetPrice;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.isActive = isActive;
		this.executeAt = executeAt;
		this.quantityFilled = quantityFilled;
		this.executedPrice = executedPrice;
		this.exchangeId = exchangeId;
		this.status = status;
		this.side = side;
		this.type = type;
	}

}