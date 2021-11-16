package chatProject.model.user;

import java.util.Objects;

/**
 * This class models the account of a user.
 * A user has only a username.
 * There is no need for an ID.
 */
public class UserAccount {

    //region Private Properties

    /**
     * The ID of a user (not needed).
     */
    private final int id;
    /**
     * The name of a user.
     */
    private final String username;

    //endregion

    /**
     * Constructor
     * @param id user id
     * @param username user name
     */
    public UserAccount(int id, String username) {
        this.id = id;
        this.username = username;
    }

    //region Public Getters

    /**
     * Gets the ID of a user (not needed).
     * @return the user unique ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username of a user.
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    //endregion

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final UserAccount that = (UserAccount) o;
        return Objects.equals(this.getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUsername());
    }

    @Override
    public String toString() {
        return username;
    }
}
