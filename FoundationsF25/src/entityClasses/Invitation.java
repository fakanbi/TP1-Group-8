package entityClasses;

import java.time.Instant; // Time type that represents UTC time line with nanosecond precision.
import java.util.UUID; // Universal Unique ID Identifier for each Invite.

public class Invitation {
	public String id;
	public String email;
	public String role;
	public Instant createdAt;
	public Instant expiresAt;
	public String status;


public Invitation() {}

public Invitation(String id, String email) {   
    this.id = id;
    this.email = email;
}

public static Invitation newPending(String email, String role, long daysLeft) {
	Invitation i = new Invitation();
	i.id = UUID.randomUUID().toString();
	i.email = email.trim();
	i.role = role;
	i.createdAt = Instant.now();
	i.expiresAt = i.createdAt.plusSeconds(daysLeft * 24 * 3600);
	i.status = "PENDING INVITATION";
	return i;
	}

}

