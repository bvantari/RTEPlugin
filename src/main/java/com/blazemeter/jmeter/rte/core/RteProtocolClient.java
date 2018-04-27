package com.blazemeter.jmeter.rte.core;

import com.blazemeter.jmeter.rte.core.ssl.SSLType;
import com.blazemeter.jmeter.rte.core.wait.WaitCondition;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface RteProtocolClient {

  /**
   * Get the list of supported terminal types.
   *
   * @return The list of supported terminal types. First element in the list is used as default
   * value.
   */
  List<TerminalType> getSupportedTerminalTypes();

  default TerminalType getTerminalTypeById(String id) {
    return getSupportedTerminalTypes().stream()
        .filter(t -> id.equals(t.getId()))
        .findAny()
        .orElse(null);
  }

  default TerminalType getDefaultTerminalType() {
    return getSupportedTerminalTypes().get(0);
  }

  void connect(String server, int port, SSLType sslType,
      TerminalType terminalType, long timeoutMillis, long stableTimeout)
      throws RteIOException, TimeoutException, InterruptedException;

  void await(List<WaitCondition> waitConditions)
      throws InterruptedException, TimeoutException, RteIOException;

  RequestListener buildRequestListener();

  void send(List<CoordInput> input, Action action) throws RteIOException;

  String getScreen();

  Dimension getScreenSize();

  boolean isInputInhibited();

  /**
   * Gets the position of the cursor in the screen.
   *
   * @return the position of the cursor in the screen or null if is not visible
   */
  Position getCursorPosition();

  void disconnect() throws RteIOException;

}
