package com.sparta.cupeed.order.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.repository.OrderRepository;
import com.sparta.cupeed.order.infrastructure.persistence.entity.OrderEntity;
import com.sparta.cupeed.order.infrastructure.persistence.entity.QOrderEntity;
import com.sparta.cupeed.order.infrastructure.persistence.mapper.OrderMapper;
import com.sparta.cupeed.order.presentation.advice.OrderError;
import com.sparta.cupeed.order.presentation.advice.OrderException;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;  // Native Query 쓸 때
	private final OrderJpaRepository orderJpaRepository;
	private final OrderMapper orderMapper;

	@Override
	@Transactional
	public Order save(Order order) {
		OrderEntity orderEntity;
		if (order.getId() == null) {
			// 신규 주문 생성
			orderEntity = orderMapper.toEntity(order);
		} else {
			// 기존 주문 업데이트
			orderEntity = orderJpaRepository.findById(order.getId())
				.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
			orderMapper.applyDomain(order, orderEntity);
		}
		// 항상 save 호출 (신규 or 수정 둘 다 JPA merge/dirty check 트리거)
		OrderEntity saved = orderJpaRepository.save(orderEntity);
		return orderMapper.toDomain(saved);
	}

	@Override
	public Optional<Order> findById(UUID orderId) {
		return orderJpaRepository.findById(orderId)
			.map(orderMapper::toDomain);
	}

	@Override
	public Page<Order> findAllByDeletedAtIsNull(Pageable pageable) {
		return orderJpaRepository.findAllByDeletedAtIsNull(pageable)
			.map(orderMapper::toDomain);
	}

	// FTS + Trigram + LIKE 통합 검색 + 권한 처리
	@Override
	public Page<Order> searchOrders(UUID receiveCompanyId, String keyword, Pageable pageable) {
		QOrderEntity order = QOrderEntity.orderEntity;

		if (!StringUtils.hasText(keyword)) {
			// 키워드가 없는 경우
			List<OrderEntity> allOrders = queryFactory
				.selectFrom(order)
				.where(receiveCompanyId != null ? order.recieveCompanyId.eq(receiveCompanyId) : null)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

			Long total = queryFactory
				.select(order.count())
				.from(order)
				.where(receiveCompanyId != null ? order.recieveCompanyId.eq(receiveCompanyId) : null)
				.fetchOne();

			List<Order> domainResults = allOrders.stream()
				.map(orderMapper::toDomain)
				.collect(Collectors.toList());

			return new PageImpl<>(domainResults, pageable, total != null ? total : 0L);
		}

		double threshold = keyword.length() <= 2 ? 0.8 :
			keyword.length() <= 5 ? 0.5 : 0.3;

		// 기본 WHERE 절
		String baseWhere = """
        WHERE (o.order_number ILIKE :likeKeyword
           OR o.recieve_company_name ILIKE :likeKeyword
           OR similarity(o.order_number, :keyword) > :threshold
           OR similarity(o.recieve_company_name, :keyword) > :threshold
           OR to_tsvector('simple', unaccent(o.order_number)) @@ plainto_tsquery('simple', unaccent(:keyword))
           OR to_tsvector('simple', unaccent(o.recieve_company_name)) @@ plainto_tsquery('simple', unaccent(:keyword)))
    """;

		// COMPANY 권한이면 자기 회사 필터 추가
		if (receiveCompanyId != null) {
			baseWhere += " AND o.recieve_company_id = :receiveCompanyId";
		}

		String nativeSql = """
        SELECT o.*
        FROM p_order o
        LEFT JOIN p_order_item oi ON oi.order_id = o.id
        """ + baseWhere + """
        ORDER BY similarity(o.order_number, :keyword) DESC
        LIMIT :limit OFFSET :offset
    """;

		var query = entityManager.createNativeQuery(nativeSql, OrderEntity.class)
			.setParameter("likeKeyword", "%" + keyword + "%")
			.setParameter("keyword", keyword)
			.setParameter("threshold", threshold)
			.setParameter("limit", pageable.getPageSize())
			.setParameter("offset", pageable.getOffset());

		if (receiveCompanyId != null) {
			query.setParameter("receiveCompanyId", receiveCompanyId);
		}

		@SuppressWarnings("unchecked")
		List<OrderEntity> entityResults = query.getResultList();

		// count 쿼리
		String countSql = """
        SELECT COUNT(*)
        FROM p_order o
        """ + baseWhere;

		var countQuery = entityManager.createNativeQuery(countSql)
			.setParameter("likeKeyword", "%" + keyword + "%")
			.setParameter("keyword", keyword)
			.setParameter("threshold", threshold);

		if (receiveCompanyId != null) {
			countQuery.setParameter("receiveCompanyId", receiveCompanyId);
		}

		Long total = ((Number) countQuery.getSingleResult()).longValue();

		List<Order> domainResults = entityResults.stream()
			.map(orderMapper::toDomain)
			.collect(Collectors.toList());

		return new PageImpl<>(domainResults, pageable, total);
	}

	// @Override
	// public Page<Order> searchOrders(String keyword, Pageable pageable) {
	// 	if (!StringUtils.hasText(keyword)) {
	// 		// 키워드 없으면 그냥 페이징
	// 		List<OrderEntity> allOrders = queryFactory
	// 			.selectFrom(QOrderEntity.orderEntity)
	// 			.offset(pageable.getOffset())
	// 			.limit(pageable.getPageSize())
	// 			.fetch();
	//
	// 		Long total = queryFactory
	// 			.select(QOrderEntity.orderEntity.count())
	// 			.from(QOrderEntity.orderEntity)
	// 			.fetchOne();
	//
	// 		List<Order> domainResults = allOrders.stream()
	// 			.map(orderMapper::toDomain)
	// 			.collect(Collectors.toList());
	//
	// 		return new PageImpl<>(domainResults, pageable, total != null ? total : 0L);
	// 	}
	//
	// 	double threshold = keyword.length() <= 2 ? 0.8 :
	// 		keyword.length() <= 5 ? 0.5 : 0.3;
	//
	// 	String nativeSql = """
	// 		SELECT o.*
	// 		FROM p_order o
	// 		LEFT JOIN p_order_item oi ON oi.order_id = o.id
	// 		WHERE o.order_number ILIKE :likeKeyword
	// 		   OR o.recieve_company_name ILIKE :likeKeyword
	// 		   OR similarity(o.order_number, :keyword) > :threshold
	// 		   OR similarity(o.recieve_company_name, :keyword) > :threshold
	// 		   OR to_tsvector('simple', unaccent(o.order_number)) @@ plainto_tsquery('simple', unaccent(:keyword))
	// 		   OR to_tsvector('simple', unaccent(o.recieve_company_name)) @@ plainto_tsquery('simple', unaccent(:keyword))
	// 		ORDER BY similarity(o.order_number, :keyword) DESC
	// 		LIMIT :limit OFFSET :offset
	// 	""";
	// 	@SuppressWarnings("unchecked")
	// 	List<OrderEntity> entityResults = entityManager.createNativeQuery(nativeSql, OrderEntity.class)
	// 		.setParameter("likeKeyword", "%" + keyword + "%")
	// 		.setParameter("keyword", keyword)
	// 		.setParameter("threshold", threshold)
	// 		.setParameter("limit", pageable.getPageSize())
	// 		.setParameter("offset", pageable.getOffset())
	// 		.getResultList();
	//
	// 	// count 쿼리
	// 	String countSql = """
	// 		SELECT COUNT(*)
	// 		FROM p_order o
	// 		WHERE o.order_number ILIKE :likeKeyword
	// 		   OR o.recieve_company_name ILIKE :likeKeyword
	// 		   OR similarity(o.order_number, :keyword) > :threshold
	// 		   OR similarity(o.recieve_company_name, :keyword) > :threshold
	// 		   OR to_tsvector('simple', unaccent(o.order_number)) @@ plainto_tsquery('simple', unaccent(:keyword))
	// 		   OR to_tsvector('simple', unaccent(o.recieve_company_name)) @@ plainto_tsquery('simple', unaccent(:keyword))
	// 	""";
	// 	Long total = ((Number) entityManager.createNativeQuery(countSql)
	// 		.setParameter("likeKeyword", "%" + keyword + "%")
	// 		.setParameter("keyword", keyword)
	// 		.setParameter("threshold", threshold)
	// 		.getSingleResult()).longValue();
	//
	// 	List<Order> domainResults = entityResults.stream()
	// 		.map(orderMapper::toDomain)
	// 		.collect(Collectors.toList());
	//
	// 	return new PageImpl<>(domainResults, pageable, total != null ? total.longValue() : 0L);
	// }
}
