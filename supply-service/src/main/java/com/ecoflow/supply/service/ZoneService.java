package com.ecoflow.supply.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.supply.client.WaterClient;
import com.ecoflow.supply.dto.SupplyPlan;
import com.ecoflow.supply.dto.ZoneRequest;
import com.ecoflow.supply.model.SupplyZone;
import com.ecoflow.supply.repository.SupplyZoneRepository;

/*Contains the business logic for managing supply zones
and calculating how much water each zone should receive
based on current availability. */

@Service
@Transactional
public class ZoneService {

	public static final double LITRES_PER_PERSON_PER_DAY = 135;

	private final SupplyZoneRepository zones;
	private final WaterClient waterClient;

	public ZoneService(SupplyZoneRepository zones, WaterClient waterClient) {
		this.zones = zones;
		this.waterClient = waterClient;
	}

// Business rule: the emptier the source, the smaller the share of demand we supply.
// NORMAL (>= 50 % full) -> 100 %, WATCH (30-50 %) -> 80 %, CRITICAL (< 30 %) -> 60 %.
	 
	public static int allocationPercent(String availability) {
		return switch (availability) {
			case "CRITICAL" -> 60;
			case "WATCH" -> 80;
			default -> 100;
		};
	}

	public static double demandFor(int population) {
		return Math.round(population * LITRES_PER_PERSON_PER_DAY / 1_000_000 * 10) / 10.0; // litres -> ML, 1 decimal
	}

	@Transactional(readOnly = true)
	public List<SupplyZone> list() {
		return zones.findAllByOrderByIdDesc();
	}

	@Transactional(readOnly = true)
	public SupplyZone get(Long id) {
		return zones.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zone " + id + " not found"));
	}

	public SupplyZone create(ZoneRequest request) {
		waterClient.getSource(request.sourceId()); // REST call: fails with 400 if the source does not exist
		double demand = request.dailyDemandMl() != null ? request.dailyDemandMl() : demandFor(request.population());
		return zones.save(new SupplyZone(request.name(), request.city(), request.population(), demand, request.sourceId()));
	}

	@Transactional(readOnly = true)
	public SupplyPlan plan(Long zoneId) {
		SupplyZone zone = get(zoneId);
		WaterClient.Source source = waterClient.getSource(zone.getSourceId());
		int percent = allocationPercent(source.availability());
		double suggested = Math.round(zone.getDailyDemandMl() * percent / 100 * 10) / 10.0;
		String rule = source.storagePercent() == null
				? source.name() + " has no recent reading: supply full demand and record a reading soon"
				: source.name() + " is " + source.availability() + " (" + source.storagePercent() + " % full): supply "
						+ percent + " % of the daily demand";
		return new SupplyPlan(zone.getId(), zone.getName(), source.id(), source.name(), source.availability(),
				source.storagePercent(), zone.getDailyDemandMl(), percent, suggested, rule);
	}

}
