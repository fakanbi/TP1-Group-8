package database;

import entityClasses.Invitation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitationDatabase {
	private final Connection conn;
	
	public InvitationDatabase(Connection conn) {
		this.conn = conn;	// Connects Database to Java
		ensureTable();		// Checks database exists, if not we create a new one.
	}
	
	private void ensureTable() {	// Creates first time table when running the app.
		try (Statement st = conn.createStatement()){	
			st.execute("""
					CREATE TABLE IF NOT EXISTS invitations (	
					id VARCHAR(64) PRIMARY KEY,
					email VARCHAR(255) NOT NULL
					)
					""");
		} catch (SQLException e) {	// Throws Random Exception if Failed.
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Invitation> listAll(){
		List<Invitation> out =  new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT ID, email FROM invitations ORDER BY email ASC");
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				out.add(new Invitation (rs.getString("id"), rs.getString("email")));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return out;
		}
	}

