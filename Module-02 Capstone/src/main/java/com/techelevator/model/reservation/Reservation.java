package com.techelevator.model.reservation;

import java.time.LocalDate;

public class Reservation {
	
	/* ************************************************************************
	 * Data Attributes
	 * ************************************************************************/
	
	private Long reservationId;
	private long siteId;
	private String reservationName;
	private LocalDate fromDate;
	private LocalDate toDate;
	private LocalDate createDate;
	
	/* ************************************************************************
	 * Getters
	 * ************************************************************************/
	
	/**
	 * @return the reservationId
	 */
	public Long getReservationId() {
		return reservationId;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @return the reservationName
	 */
	public String getReservationName() {
		return reservationName;
	}
	/**
	 * @return the fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}
	/**
	 * @return the toDate
	 */
	public LocalDate getToDate() {
		return toDate;
	}
	/**
	 * @return the createDate
	 */
	public LocalDate getCreateDate() {
		return createDate;
	}
	
	/* ************************************************************************
	 * Setters
	 * ************************************************************************/
	
	/**
	 * @param reservationId the reservationId to set
	 */
	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @param reservationName the reservationName to set
	 */
	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	
	/* ************************************************************************
	 * Overrides
	 * ************************************************************************/
	
	@Override
	public String toString() {
		return "The reservation has been made for " + reservationName + " and the confirmation id is " + reservationId.toString();
	}
	
	

}
