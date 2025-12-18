package ru.ifmo.soa.uddi.exception;

import lombok.Getter;
import org.uddi.api_v3.DispositionReport;
import org.uddi.api_v3.ErrInfo;
import org.uddi.api_v3.Result;

/**
 * Represents a UDDI application error.
 * Will be converted to a SOAP Fault with DispositionReport in <detail>.
 */
@Getter
public class UddiFaultException extends RuntimeException {

  private final DispositionReport dispositionReport;

  public UddiFaultException(int errno, String errCode, String message) {
    super(message);
    this.dispositionReport = createDispositionReport(errno, errCode, message);
  }

  public UddiFaultException(int errno, String errCode, String message, Throwable cause) {
    super(message, cause);
    this.dispositionReport = createDispositionReport(errno, errCode, message);
  }

  private DispositionReport createDispositionReport(int errno, String errCode, String message) {
    DispositionReport report = new DispositionReport();
    Result result = new Result();
    result.setErrno(errno);
    ErrInfo errInfo = new ErrInfo();
    errInfo.setErrCode(errCode);
    errInfo.setValue(message);
    result.setErrInfo(errInfo);
    report.getResult().add(result);
    return report;
  }
}
