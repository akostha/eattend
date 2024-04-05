package com.ajayk.eattend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class QRCodeRequest {
	
	private int eventid;
	private LocalDateTime requestTime;
	private List<Integer> mobileNos;

}
