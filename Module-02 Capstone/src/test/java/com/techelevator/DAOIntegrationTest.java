package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.model.campground.Campground;
import com.techelevator.model.campground.JdbcCampgroundDAO;
import com.techelevator.model.park.JdbcParkDAO;
import com.techelevator.model.park.Park;
import com.techelevator.model.reservation.JdbcReservationDAO;
import com.techelevator.model.reservation.Reservation;
import com.techelevator.model.site.JdbcSiteDAO;
import com.techelevator.model.site.Site;

public class DAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;

	private JdbcParkDAO parkDAO;
	private JdbcCampgroundDAO campgroundDAO;
	private JdbcSiteDAO siteDAO;
	private JdbcReservationDAO reservationDAO;
	
	
	private Park testPark;
	private Campground testCampground;
	private Site testSite;
	private Reservation testReservation;
	
	private static final long TEST_PARK_ID = 300;
	private static final String TEST_PARK_NAME = "Test Park";
	private static final String TEST_PARK_LOCATION = "The Moon";
	private static final LocalDate TEST_PARK_ESTABLISH_DATE = LocalDate.parse("2000-01-01");
	private static final int TEST_PARK_AREA = 400;
	private static final int TEST_PARK_VISITORS = 4;
	private static final String TEST_PARK_DESCRIPTION = "It's pretty remote.";
	
	private static final long TEST_CAMPGROUND_ID = 200;
	private static final String TEST_CAMPGROUND_NAME = "The Moon Landing";
	private static final String TEST_CAMPGROUND_OPEN_FROM_MM = "02";
	private static final String TEST_CAMPGROUND_OPEN_TO_MM = "10";
	private static final BigDecimal TEST_CAMPGROUND_DAILY_FEE = new BigDecimal("10.00");
	
	private static final long TEST_SITE_ID = 1500;
	private static final int TEST_SITE_NUMBER = 1;
	private static final int TEST_SITE_MAX_OCCUPANCY = 4;
	private static final boolean TEST_SITE_IS_ACCESSIBLE = true;
	private static final int TEST_SITE_MAX_RV_LENGTH = 100;
	private static final boolean TEST_SITE_HAS_UTILITIES = false;
	
	private static final long TEST_RESERVATION_ID = 1000;
	private static final String TEST_RESERVATION_NAME = "Neil Armstrong";
	private static final LocalDate TEST_RESERVATION_FROM_DATE = LocalDate.now().plusDays(1);
	private static final LocalDate TEST_RESERVATION_TO_DATE = LocalDate.now().plusDays(7);
	private static final LocalDate TEST_RESERVATION_CREATE_DATE = LocalDate.now();
	
	
	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}

	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	
	@Before
	public void setup() {
		String insertIntoParkSQL = "Insert into park ("
				              + "park_id, "
				              + "name, "
				              + "location, "
				              + "establish_date, "
				              + "area, "
				              + "visitors, "
				              + "description) "
				              + "Values (?, ?, ?, ?, ?, ?, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(insertIntoParkSQL, TEST_PARK_ID, 
				                               TEST_PARK_NAME, 
				                               TEST_PARK_LOCATION, 
				                               TEST_PARK_ESTABLISH_DATE, 
				                               TEST_PARK_AREA, 
				                               TEST_PARK_VISITORS, 
				                               TEST_PARK_DESCRIPTION);
		parkDAO = new JdbcParkDAO(dataSource);
		
		String insertIntoCampgroundSQL = "Insert into campground ("
				                       + "campground_id, "
				                       + "park_id, "
				                       + "name, "
				                       + "open_from_mm, "
				                       + "open_to_mm, "
				                       + "daily_fee) "
				                       + "Values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertIntoCampgroundSQL, TEST_CAMPGROUND_ID, 
													 TEST_PARK_ID, 
													 TEST_CAMPGROUND_NAME, 
													 TEST_CAMPGROUND_OPEN_FROM_MM, 
													 TEST_CAMPGROUND_OPEN_TO_MM,
													 TEST_CAMPGROUND_DAILY_FEE);
		campgroundDAO = new JdbcCampgroundDAO(dataSource);
		
		String insertIntoSiteSQL = "Insert into site ("
				                 + "site_id, "
				                 + "campground_id, "
				                 + "site_number, "
				                 + "max_occupancy, "
				                 + "accessible, "
				                 + "max_rv_length, "
				                 + "utilities) "
				                 + "Values (?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertIntoSiteSQL, TEST_SITE_ID, 
				                               TEST_CAMPGROUND_ID, 
				                               TEST_SITE_NUMBER, 
				                               TEST_SITE_MAX_OCCUPANCY, 
				                               TEST_SITE_IS_ACCESSIBLE, 
				                               TEST_SITE_MAX_RV_LENGTH,
				                               TEST_SITE_HAS_UTILITIES);
		siteDAO = new JdbcSiteDAO(dataSource);
		
		String insertIntoReservationSQL = "Insert into reservation ("
				                        + "reservation_id, "
				                        + "site_id, "
				                        + "name, "
				                        + "from_date, "
				                        + "to_date, "
				                        + "create_date) "
				                        + "Values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertIntoReservationSQL, TEST_RESERVATION_ID, 
				                                      TEST_SITE_ID, 
				                                      TEST_RESERVATION_NAME, 
				                                      TEST_RESERVATION_FROM_DATE, 
				                                      TEST_RESERVATION_TO_DATE, 
				                                      TEST_RESERVATION_CREATE_DATE);
		reservationDAO = new JdbcReservationDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	/* This method provides access to the DataSource for subclasses so that
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	@Test
	public void test_view_all_campgrounds() {
		testCampground = getTestCampground();
		List<Campground> testCampgroundList = new ArrayList<Campground>();
		testCampgroundList.add(testCampground);
		
		testPark = getTestPark();
		List<Campground> expectedCampgroundList = campgroundDAO.viewAllCampgrounds(testPark);
		
		assertNotEquals(null, expectedCampgroundList);
		assertEquals(testCampgroundList.size(), expectedCampgroundList.size());
	}
	
	@Test
	public void test_get_park_by_name_and_map_row_to_park() {
		testPark = getTestPark();
		Park retrievedPark = parkDAO.getParkByName(TEST_PARK_NAME);
		assertNotEquals(null, testPark.getName());
		assertEquals(testPark.getName(), retrievedPark.getName());
	}
	
	@Test
	public void test_get_campground_from_list_and_map_row_to_campground() {
		Campground testCampground = getTestCampground();
		List<Campground> campgroundList = new ArrayList<Campground>();
		campgroundList.add(testCampground);
		
		Campground retrievedCampground = campgroundDAO.getCampgroundFromList(campgroundList, 1);
		
		assertNotEquals(null, testCampground.getName());
		assertEquals(testCampground.getName(), retrievedCampground.getName());
	}
	
	@Test
	public void test_get_total_stay_price_and_map_row_to_stay_price() {
		testCampground = getTestCampground();
		testReservation = getTestReservation();
		LocalDate arrival = testReservation.getFromDate();
		LocalDate departure = testReservation.getToDate();
		long stayDuration = ChronoUnit.DAYS.between(arrival, departure);
		
		BigDecimal expectedStayPrice = (testCampground.getDailyFee().multiply(new BigDecimal(stayDuration)));
		BigDecimal retrievedStayPrice = campgroundDAO.getTotalStayPrice(testCampground, arrival, departure);
		
		assertNotEquals(null, retrievedStayPrice);
		assertEquals(expectedStayPrice, retrievedStayPrice);
	}
	
	@Test
	public void test_get_available_sites() {
		testSite = getTestSite();
		List<Site> testAvailableSites = new ArrayList<Site>();
		
		testCampground = getTestCampground();
		testReservation = getTestReservation();
		LocalDate arrival = testReservation.getFromDate();
		LocalDate departure = testReservation.getToDate();
		
		List<Site> expectedAvailableSites = siteDAO.getAvailableSites(testCampground, arrival, departure);
		
		assertEquals(testAvailableSites.size(), expectedAvailableSites.size());
	}
	
	@Test
	public void test_get_site_from_list_and_map_row_to_site() {
		testSite = getTestSite();
		List<Site> availableSites = new ArrayList<Site>();
		availableSites.add(testSite);
		
		Site retrievedSite = siteDAO.getSiteFromList(availableSites, 1);
		
		assertNotEquals(null, retrievedSite);
		assertEquals(testSite, retrievedSite);
	}
	 
	@Test
	public void test_add_reservation_and_get_reservation_by_name_and_map_row_to_reservation_id() {
		testReservation = getTestReservation();
		testSite = getTestSite();

		LocalDate arrival = testReservation.getFromDate();
		LocalDate departure = testReservation.getToDate();
		String reservationName = testReservation.getReservationName();
		
		reservationDAO.addReservation(testSite, reservationName, arrival, departure);
		long testReservationId = reservationDAO.getReservationByName(TEST_RESERVATION_NAME);
		long expectedReservationId = reservationDAO.getReservationByName(reservationName);
		
		assertNotEquals(null, expectedReservationId);
		assertEquals(testReservationId, expectedReservationId);
	}
	
	/* **************
	 * HELPER METHODS
	 * **************/
	
	private Park getTestPark() {
		Park testPark = new Park();
		testPark.setParkId(TEST_PARK_ID);
		testPark.setName(TEST_PARK_NAME);
		testPark.setLocation(TEST_PARK_LOCATION);
		testPark.setEstablishDate(TEST_PARK_ESTABLISH_DATE);
		testPark.setArea(TEST_PARK_AREA);
		testPark.setVisitors(TEST_PARK_VISITORS);
		testPark.setDescription(TEST_PARK_DESCRIPTION);
		return testPark;
	}
	
	private Campground getTestCampground() {
		Campground testCampground = new Campground();
		testCampground.setCampgroundId(TEST_CAMPGROUND_ID);
		testCampground.setParkId(TEST_PARK_ID);
		testCampground.setName(TEST_CAMPGROUND_NAME);
		testCampground.setOpenFromMm(TEST_CAMPGROUND_OPEN_FROM_MM);
		testCampground.setOpenToMm(TEST_CAMPGROUND_OPEN_TO_MM);
		testCampground.setDailyFee(TEST_CAMPGROUND_DAILY_FEE);
		return testCampground;
	}
	
	private Site getTestSite() {
		Site testSite = new Site();
		testSite.setSiteId(TEST_SITE_ID);
		testSite.setCampgroundId(TEST_CAMPGROUND_ID);
		testSite.setSiteNumber(TEST_SITE_NUMBER);
		testSite.setMaxOccupancy(TEST_SITE_MAX_OCCUPANCY);
		testSite.setAccessible(TEST_SITE_IS_ACCESSIBLE);
		testSite.setMaxRvLength(TEST_SITE_MAX_RV_LENGTH);
		testSite.setHasUtilities(TEST_SITE_HAS_UTILITIES);
		return testSite;
	}
	
	private Reservation getTestReservation() {
		Reservation testReservation = new Reservation();
		testReservation.setReservationId(TEST_RESERVATION_ID);
		testReservation.setSiteId(TEST_SITE_ID);
		testReservation.setReservationName(TEST_RESERVATION_NAME);
		testReservation.setFromDate(TEST_RESERVATION_FROM_DATE);
		testReservation.setToDate(TEST_RESERVATION_TO_DATE);
		testReservation.setCreateDate(TEST_RESERVATION_CREATE_DATE);
		return testReservation;
	}
}



