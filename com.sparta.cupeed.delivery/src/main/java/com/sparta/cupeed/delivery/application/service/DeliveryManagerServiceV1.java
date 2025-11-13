package com.sparta.cupeed.delivery.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;
import com.sparta.cupeed.delivery.domain.repository.DeliveryManagerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryManagerServiceV1 {

	private final DeliveryManagerRepository deliveryManagerRepository;

	// 배송 담당자 생성
	@Transactional
	public DeliveryManager createManager(String userId, UUID hubId,
		DeliveryType deliveryType, String createdBy) {
		// 중복체크
		deliveryManagerRepository.findByUserId(userId)
			.ifPresent(manager -> {
				throw new IllegalArgumentException("이미 등록된 배송 담당자입니다: " + userId);
			});

		DeliveryManager manager = DeliveryManager.builder()
			.userId(userId)
			.hubId(hubId)
			.deliveryType(DeliveryManager.DeliveryType.valueOf(deliveryType.name()))
			.deliverySequence(0)
			.createdBy(createdBy)
			.createdAt(Instant.now())
			.build();

		return deliveryManagerRepository.save(manager);
	}

	//배송 담당자 전체 조회(페이지)
	public List<DeliveryManager> getAllActiveManagers() {
		return deliveryManagerRepository.findAllActive();
	}

	// 배송 담당자 조회
	public DeliveryManager getManagerById(UUID managerId) {
		return deliveryManagerRepository.findById(managerId)
			.orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다: " + managerId));
	}

	// 사용자 ID로 배송 담당자 조회
	public DeliveryManager getManagerByUserId(String userId) {
		return deliveryManagerRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다: " + userId));
	}

	// 허브별 배송 담당자 조회
	public List<DeliveryManager> getManagersByHubId(UUID hubId) {
		return deliveryManagerRepository.findByHubId(hubId);
	}

	//타입별 배송 담당자 조회
	public List<DeliveryManager> getManagersByDeliveryType(DeliveryType deliveryType) {
		return deliveryManagerRepository.findByDeliveryType(deliveryType);
	}

	//허브+타입별 배송 담당자 조회
	public List<DeliveryManager> getManagersByHubIdAndType(UUID hubId, DeliveryType deliveryType) {
		return deliveryManagerRepository.findByHubIdAndDeliveryType(hubId, deliveryType);
	}

	// 배송 담당자 수정
	@Transactional
	public DeliveryManager updateManager(UUID managerId, UUID newHubId,
		DeliveryType newDeliveryType,
		Integer newSequence, String updatedBy) {
		DeliveryManager manager = deliveryManagerRepository.findById(managerId)
			.orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다: " + managerId));

		if (newHubId != null) {
			manager.changeHub(newHubId);
		}
		if (newDeliveryType != null) {
			manager.changeDeliveryType(DeliveryManager.DeliveryType.valueOf(newDeliveryType.name()));
		}
		if (newSequence != null) {
			manager.updateDeliverySequence(newSequence);
		}

		return deliveryManagerRepository.save(manager);
	}

	// 배송 담당자 삭제
	@Transactional
	public void deleteManager(UUID managerId, String deletedBy) {
		DeliveryManager manager = deliveryManagerRepository.findById(managerId)
			.orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다: " + managerId));

		manager.markDeleted(deletedBy);
		deliveryManagerRepository.save(manager);
	}
}