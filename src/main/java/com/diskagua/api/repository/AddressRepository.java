package com.diskagua.api.repository;

import com.diskagua.api.models.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> listAllUserAddressesById(Long userId);

    Optional<Address> findUserAddressById(Long userId, Long addressId);
}
