package com.techelevator.model.site;

public class Site {
	
	/* ************************************************************************
	 * Data Attributes
	 * ************************************************************************/
	
	private long siteId;
	private long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean isAccessible = true;
	private int maxRvLength;
	private boolean hasUtilities = true;
	
	/* ************************************************************************
	 * Getters
	 * ************************************************************************/
	
	/**
	 * @return the siteId
	 */
	public Long getSiteId() {
		return siteId;
	}
	/**
	 * @return the campgroundId
	 */
	public long getCampgroundId() {
		return campgroundId;
	}
	/**
	 * @return the siteNumber
	 */
	public int getSiteNumber() {
		return siteNumber;
	}
	/**
	 * @return the maxOccupancy
	 */
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	/**
	 * @return the isAccessible
	 */
	public boolean isAccessible() {
		return isAccessible;
	}
	/**
	 * @return the maxRvLength
	 */
	public int getMaxRvLength() {
		return maxRvLength;
	}
	/**
	 * @return the hasUtilities
	 */
	public boolean isHasUtilities() {
		return hasUtilities;
	}
	
	/* ************************************************************************
	 * Setters
	 * ************************************************************************/
	
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @param campgroundId the campgroundId to set
	 */
	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}
	/**
	 * @param siteNumber the siteNumber to set
	 */
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	/**
	 * @param maxOccupancy the maxOccupancy to set
	 */
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	/**
	 * @param isAccessible the isAccessible to set
	 */
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	/**
	 * @param maxRvLength the maxRvLength to set
	 */
	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	/**
	 * @param hasUtilities the hasUtilities to set
	 */
	public void setHasUtilities(boolean hasUtilities) {
		this.hasUtilities = hasUtilities;
	}
	
	/* ************************************************************************
	 * Overrides
	 * ************************************************************************/
	
	@Override
	public String toString() {
		return "Site Id = " + siteId + ", Campground Id = " + campgroundId + ", Site Number = " + siteNumber;
	}
}
