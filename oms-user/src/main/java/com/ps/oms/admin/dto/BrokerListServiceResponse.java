package com.ps.oms.admin.dto;

import java.util.List;

import com.ps.oms.admin.entities.AdminBrokerDisableUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BrokerListServiceResponse {

private List<AdminBrokerDisableUser> response;

}