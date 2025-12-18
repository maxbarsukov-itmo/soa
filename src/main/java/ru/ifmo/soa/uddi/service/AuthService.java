package ru.ifmo.soa.uddi.service;

public interface AuthService {
  /**
   * Creates a new authentication token for the given user ID and validate credentials.
   *
   * @param userId non-empty user identifier
   * @return valid auth token (e.g., base64 string)
   */
  String createToken(String userId);

  /**
   * Validates whether the token is present and not expired.
   */
  boolean validateToken(String token);

  /**
   * Immediately invalidates the token (e.g., on discardAuthToken).
   */
  void invalidateToken(String token);

  /**
   * Returns the user ID associated with the token.
   * Throws UddiFaultException if token is invalid/expired.
   */
  String getUserIdByToken(String token);
}
