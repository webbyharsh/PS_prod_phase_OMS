package com.ps.oms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ps.oms.user.entities.Role;
import com.ps.oms.user.repository.RoleRepository;

@Component
public class DBSetup {
	
    @Autowired
    private RoleRepository roleRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

        roleRepository.save(new Role("User", "Authorized to use all Broker operations"));
        roleRepository.save(new Role("Admin", "Authorized to use all Admin operations"));
    }

}
