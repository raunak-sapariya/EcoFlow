package com.ecoflow.operations.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.operations.dto.AssetRequest;
import com.ecoflow.operations.model.Asset;
import com.ecoflow.operations.repository.AssetRepository;

//infrastructure register
@Service
@Transactional
public class AssetService {

	private final AssetRepository assets;

	public AssetService(AssetRepository assets) {
		this.assets = assets;
	}

	@Transactional(readOnly = true)
	public List<Asset> list(Long zoneId) {
		if (zoneId == null) {
			return assets.findAllByOrderByIdDesc();
		}
		return assets.findByZoneIdOrderByIdDesc(zoneId);
	}

	@Transactional(readOnly = true)
	public Asset get(Long id) {
		return assets.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset " + id + " not found"));
	}

	public Asset create(AssetRequest r) {
		return assets.save(new Asset(r.name(), r.type(), r.zoneId(), r.sourceId(), r.installDate(), r.condition(), r.status()));
	}

}
