package ru.ifmo.soa.uddi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.soa.uddi.exception.UddiErrorCodes;
import ru.ifmo.soa.uddi.exception.UddiFaultException;
import ru.ifmo.soa.uddi.service.AuthService;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private static final Duration TOKEN_TTL = Duration.ofHours(1);
  private final Map<String, AuthTokenRecord> tokenStore = new ConcurrentHashMap<>();

  @Override
  public String createToken(String userId) {
    if (userId == null || userId.isBlank()) {
      throw new UddiFaultException(
        UddiErrorCodes.E_UNKNOWN_USER_ERRNO,
        UddiErrorCodes.E_UNKNOWN_USER,
        "User ID must not be empty"
      );
    }

    String token = Base64.getEncoder().encodeToString((userId + ":" + Instant.now()).getBytes());
    tokenStore.put(token, new AuthTokenRecord(userId, Instant.now().plus(TOKEN_TTL)));
    return token;
  }

  @Override
  public boolean validateToken(String token) {
    AuthTokenRecord record = tokenStore.get(token);
    if (record == null) return false;
    if (Instant.now().isAfter(record.expiry)) {
      tokenStore.remove(token);
      return false;
    }
    return true;
  }

  @Override
  public void invalidateToken(String token) {
    tokenStore.remove(token);
  }

  @Override
  @Transactional(readOnly = true)
  public String getUserIdByToken(String token) {
    if (!validateToken(token)) {
      throw new UddiFaultException(
        UddiErrorCodes.E_AUTH_TOKEN_EXPIRED_ERRNO,
        UddiErrorCodes.E_AUTH_TOKEN_EXPIRED,
        "Token is invalid or expired"
      );
    }
    return tokenStore.get(token).userId;
  }

  private record AuthTokenRecord(String userId, Instant expiry) {}
}
