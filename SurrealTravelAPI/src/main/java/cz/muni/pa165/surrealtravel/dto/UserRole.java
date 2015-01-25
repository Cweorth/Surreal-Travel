package cz.muni.pa165.surrealtravel.dto;

/**
 * User roles in the system.
 * @author Jan Klimeš [374259]
 */
public enum UserRole {
    /**
     * Can list trips and excursions, can create reservations
     */
    ROLE_USER,

    /**
     * Can create trips and excursions, can edit reservations
     */
    ROLE_STAFF,

    /**
     * Can do almost everything
     */
    ROLE_ADMIN,

    /**
     * Same as {@code ROLE_ADMIN}, but only one {@code Account} can have this role,
     * this account cannot be deleted nor its roles changed
     */
    ROLE_ROOT
}
