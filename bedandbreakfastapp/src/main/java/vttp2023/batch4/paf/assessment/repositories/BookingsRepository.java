package vttp2023.batch4.paf.assessment.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.models.User;

@Repository
public class BookingsRepository {
	
	// You may add additional dependency injections

	public static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email like %";

	@Autowired
	private JdbcTemplate template;

	// You may use this method in your task
	public Optional<User> userExists(String email) {
		SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
		if (!rs.next())
			return Optional.empty();

		return Optional.of(new User(rs.getString("email"), rs.getString("name")));
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newUser(User user) throws Exception {
		
		String SQL_ADD_USER = """
			INSERT INTO users(email, name) VALUES(?, ?)
		""";

		int userAdded = template.update(SQL_ADD_USER, user.email(), user.name());

		if (!(userAdded > 0)) {
			throw new Exception("Unsuccessful adding user");
		}
	}
	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newBookings(Bookings bookings) throws Exception{

		String SQL_ADD_BOOKING = """
				INSERT INTO bookings(booking_id, listing_id, duration, email)
				VALUES (?, ?, ?, ?)
		""";

		int bookingAdded = template.update(SQL_ADD_BOOKING, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail());

		if(!(bookingAdded > 0)) {
			throw new Exception("Unsuccessful adding booking");
		}

	}

	public boolean isUserExists(String email) {
		
		String SQL_USER_EXIST = """
			SELECT * FROM users WHERE email = ?		
		""";

		SqlRowSet rs = template.queryForRowSet(SQL_USER_EXIST, email);

		if(!rs.next()) {
			return false;
		}

		return true;
	}
}
