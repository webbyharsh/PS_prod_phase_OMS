package com.oms.order.repository;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.oms.order.dto.SearchOrdersRequest;
import com.oms.order.entities.Order;

@Repository
public class SearchOrdersRepositoryImpl implements SearchOrdersRepositoryCustom{

	@PersistenceContext
	EntityManager entityManager; 
	
	private static final String WHERE = " WHERE ";
	private static final String AND = " AND ";
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<Order> getSearchOrders(SearchOrdersRequest searchOrdersRequest, 
			Long brokerId, Pageable pageable) {
		
		String queryStatement = "SELECT * FROM oms_order o";
		int flag = 0;
		
		if (Objects.nonNull(searchOrdersRequest.getClientName())) {
			queryStatement += " LEFT JOIN oms_client c ON o.client_id = c.client_id WHERE ";
			queryStatement += "(c.name LIKE '" + searchOrdersRequest.getClientName() + "')";
			flag = 1;
		}
		
		if (Objects.nonNull(searchOrdersRequest.getClientEmail())) {
			if (flag == 0)
				queryStatement += " LEFT JOIN oms_client c ON o.client_id = c.client_id WHERE ";
			else
				queryStatement += AND;
			queryStatement += "(c.email_id LIKE '" + searchOrdersRequest.getClientEmail() + "')";
			flag = 1;
		}
		
		if (Objects.nonNull(searchOrdersRequest.getStock())) {
			if (flag == 0)
				queryStatement += WHERE;
			else
				queryStatement += AND;
			queryStatement += "(o.stock LIKE '" + searchOrdersRequest.getStock() + "')";
			flag = 1;
		}
		
		if (Objects.nonNull(searchOrdersRequest.getType())) {
			int type;
			if (searchOrdersRequest.getType().toString().equals("MARKET"))
				type = 0;
			else
				type = 1;
			
			if (flag == 0)
				queryStatement += WHERE;
			else
				queryStatement += AND;
			queryStatement += "(o.type = " + type + ")";
			flag = 1;
		}
		
		if (Objects.nonNull(searchOrdersRequest.getStartDate()) && 
				Objects.nonNull(searchOrdersRequest.getEndDate())) {
			if (flag == 0)
				queryStatement += WHERE;
			else
				queryStatement += AND;
			queryStatement += "(o.created_at BETWEEN '" + searchOrdersRequest.getStartDate().toString() + "' AND '" +
					searchOrdersRequest.getEndDate().toString() + "')";
			flag = 1;
		}
		
		if (flag == 0)
			queryStatement += WHERE;
		else 
			queryStatement += AND;
		queryStatement += "(o.created_by = " + brokerId + ")";
		Query query = entityManager.createNativeQuery(queryStatement, Order.class);

		List<Order> orders = query.getResultList();
		int count = pageable.getPageSize();
		int page = pageable.getPageNumber();
        int max = (count*(page+1)>orders.size())? orders.size(): count*(page+1);
		return new PageImpl<>(orders.subList(page*count, max), pageable, orders.size());
	}

}