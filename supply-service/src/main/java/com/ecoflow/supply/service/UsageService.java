package com.ecoflow.supply.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecoflow.supply.dto.UsageRequest;
import com.ecoflow.supply.model.UsageRecord;
import com.ecoflow.supply.repository.UsageRecordRepository;

// Water Usage Management: consumption per zone, month and category.
@Service
@Transactional
public class UsageService {

	private final UsageRecordRepository usage;
	private final ZoneService zoneService;

	public UsageService(UsageRecordRepository usage, ZoneService zoneService) {
		this.usage = usage;
		this.zoneService = zoneService;
	}

	@Transactional(readOnly = true)
	public List<UsageRecord> list(Long zoneId) {
		if (zoneId == null) {
			return usage.findTop50ByOrderByIdDesc();
		}
		return usage.findTop12ByZone_IdOrderByIdDesc(zoneId);
	}

	public UsageRecord add(UsageRequest request) {
		return usage.save(new UsageRecord(zoneService.get(request.zoneId()), request.usageMonth().withDayOfMonth(1),
				request.category(), request.volumeMl()));
	}

}
