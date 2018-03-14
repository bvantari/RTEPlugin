package com.blazemeter.jmeter.rte.protocols.tn5250.listeners;

import com.blazemeter.jmeter.rte.core.wait.TextWaitCondition;
import com.blazemeter.jmeter.rte.protocols.tn5250.Tn5250Client;
import java.util.concurrent.ScheduledExecutorService;
import net.infordata.em.tn5250.XI5250Emulator;
import net.infordata.em.tn5250.XI5250EmulatorEvent;

public class ScreenTextListener extends Tn5250ConditionWaiter<TextWaitCondition> {

  private boolean matched;

  public ScreenTextListener(TextWaitCondition condition, Tn5250Client client,
      ScheduledExecutorService stableTimeoutExecutor) {
    super(condition, client, stableTimeoutExecutor);
  }

  @Override
  public void connecting(XI5250EmulatorEvent event) {
    if (matched) {
      startStablePeriod();
    }
  }

  @Override
  public void connected(XI5250EmulatorEvent event) {
    if (matched) {
      startStablePeriod();
    }
  }

  @Override
  public void disconnected(XI5250EmulatorEvent event) {
    if (matched) {
      startStablePeriod();
    }
  }

  @Override
  public void stateChanged(XI5250EmulatorEvent event) {
    if (client.hasPendingError()) {
      cancelWait();
    } else if (matched) {
      startStablePeriod();
    }
  }

  @Override
  public void newPanelReceived(XI5250EmulatorEvent event) {
    XI5250Emulator emulator = event.get5250Emulator();
    if (condition.matchesScreen(emulator.getString(), emulator.getCrtSize())) {
      matched = true;
    }
    if (matched) {
      startStablePeriod();
    }
  }

  @Override
  public void fieldsRemoved(XI5250EmulatorEvent event) {
    if (matched) {
      startStablePeriod();
    }
  }

  @Override
  public void dataSended(XI5250EmulatorEvent event) {
    if (matched) {
      startStablePeriod();
    }
  }

}
