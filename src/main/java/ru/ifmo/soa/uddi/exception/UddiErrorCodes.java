package ru.ifmo.soa.uddi.exception;

/**
 * UDDI v3.0.2 error codes as defined in
 * <a href="https://www.oasis-open.org/committees/uddi-spec/doc/spec/v3/uddi_v3.htm#_Toc85908376">Chapter 12 of the specification</a>
 * of the specification.
 * Each error has:
 * - symbolic name (errCode, String)
 * - numeric code (errno, int)
 */
public final class UddiErrorCodes {

  // === Authentication & Security ===
  public static final int E_AUTH_TOKEN_REQUIRED_ERRNO = 10120;
  public static final String E_AUTH_TOKEN_REQUIRED = "E_authTokenRequired";

  public static final int E_AUTH_TOKEN_EXPIRED_ERRNO = 10110;
  public static final String E_AUTH_TOKEN_EXPIRED = "E_authTokenExpired";

  public static final int E_UNKNOWN_USER_ERRNO = 10150;
  public static final String E_UNKNOWN_USER = "E_unknownUser";

  public static final int E_USER_MISMATCH_ERRNO = 10140;
  public static final String E_USER_MISMATCH = "E_userMismatch";

  // === Validation & Input Errors ===
  public static final int E_INVALID_KEY_PASSED_ERRNO = 10210;
  public static final String E_INVALID_KEY_PASSED = "E_invalidKeyPassed";

  public static final int E_INVALID_VALUE_ERRNO = 20200;
  public static final String E_INVALID_VALUE = "E_invalidValue";

  public static final int E_MESSAGE_TOO_LARGE_ERRNO = 30110;
  public static final String E_MESSAGE_TOO_LARGE = "E_messageTooLarge";

  public static final int E_UNRECOGNIZED_VERSION_ERRNO = 10040;
  public static final String E_UNRECOGNIZED_VERSION = "E_unrecognizedVersion";

  public static final int E_UNSUPPORTED_ERRNO = 10050;
  public static final String E_UNSUPPORTED = "E_unsupported";

  // === Business Logic Errors ===
  public static final int E_ACCOUNT_LIMIT_EXCEEDED_ERRNO = 10160;
  public static final String E_ACCOUNT_LIMIT_EXCEEDED = "E_accountLimitExceeded";

  public static final int E_ASSERTION_NOT_FOUND_ERRNO = 30000;
  public static final String E_ASSERTION_NOT_FOUND = "E_assertionNotFound";

  public static final int E_BUSY_ERRNO = 10400;
  public static final String E_BUSY = "E_busy";

  public static final int E_FATAL_ERROR_ERRNO = 10500;
  public static final String E_FATAL_ERROR = "E_fatalError";

  public static final int E_HISTORY_DATA_NOT_AVAILABLE_ERRNO = 40010;
  public static final String E_HISTORY_DATA_NOT_AVAILABLE = "E_historyDataNotAvailable";

  public static final int E_INVALID_COMBINATION_ERRNO = 40500;
  public static final String E_INVALID_COMBINATION = "E_invalidCombination";

  public static final int E_INVALID_COMPLETION_STATUS_ERRNO = 30100;
  public static final String E_INVALID_COMPLETION_STATUS = "E_invalidCompletionStatus";

  public static final int E_INVALID_PROJECTION_ERRNO = 20230;
  public static final String E_INVALID_PROJECTION = "E_invalidProjection";

  public static final int E_INVALID_TIME_ERRNO = 40030;
  public static final String E_INVALID_TIME = "E_invalidTime";

  public static final int E_KEY_UNAVAILABLE_ERRNO = 40100;
  public static final String E_KEY_UNAVAILABLE = "E_keyUnavailable";

  public static final int E_NO_VALUES_AVAILABLE_ERRNO = 40200;
  public static final String E_NO_VALUES_AVAILABLE = "E_noValuesAvailable";

  public static final int E_REQUEST_DENIED_ERRNO = 20250;
  public static final String E_REQUEST_DENIED = "E_requestDenied";

  public static final int E_REQUEST_TIMEOUT_ERRNO = 20240;
  public static final String E_REQUEST_TIMEOUT = "E_requestTimeout";

  public static final int E_RESULT_SET_TOO_LARGE_ERRNO = 40300;
  public static final String E_RESULT_SET_TOO_LARGE = "E_resultSetTooLarge";

  public static final int E_TOKEN_ALREADY_EXISTS_ERRNO = 40070;
  public static final String E_TOKEN_ALREADY_EXISTS = "E_tokenAlreadyExists";

  public static final int E_TRANSFER_NOT_ALLOWED_ERRNO = 40600;
  public static final String E_TRANSFER_NOT_ALLOWED = "E_transferNotAllowed";

  public static final int E_UNACCEPTABLE_SIGNATURE_ERRNO = 40400;
  public static final String E_UNACCEPTABLE_SIGNATURE = "E_unacceptableSignature";

  public static final int E_UNVALIDATABLE_ERRNO = 20220;
  public static final String E_UNVALIDATABLE = "E_unvalidatable";

  public static final int E_VALUE_NOT_ALLOWED_ERRNO = 20210;
  public static final String E_VALUE_NOT_ALLOWED = "E_valueNotAllowed";

  // === Deprecated (should not be used in v3, but defined for completeness) ===
  public static final int E_TOO_MANY_OPTIONS_ERRNO = 10030;
  public static final String E_TOO_MANY_OPTIONS = "E_tooManyOptions";

  private UddiErrorCodes() {}
}
