package com.enis.gateways.repository;

import com.enis.gateways.model.Device;
import com.enis.gateways.model.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, String> {

}
