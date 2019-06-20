package com.blazemeter.jmeter.rte.recorder;

import com.blazemeter.jmeter.rte.sampler.gui.BlazemeterLabsLogo;

import com.helger.commons.annotation.VisibleForTesting;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Collections;

import org.apache.jmeter.control.gui.LogicControllerGui;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;

public class RTERecorderGui extends LogicControllerGui implements JMeterGUIComponent,
    UnsharedComponent, RecordingStateListener {

  private static final String TEMPLATE_NAME = "RTETemplate.jmx";
  private static final String TEMPLATE_XML_NAME = "Recording RTE";
  private static final String DESC_TEMPLATE_NAME = "RTETemplateDescription.xml";

  private RTERecorderPanel recordingPanel;
  private RTERecorder recorder;

  public RTERecorderGui() {
    recordingPanel = new RTERecorderPanel(this);
    setLayout(new BorderLayout(0, 5));
    setBorder(makeBorder());
    add(makeTitlePanel(), BorderLayout.NORTH);
    add(recordingPanel, BorderLayout.CENTER);
    add(new BlazemeterLabsLogo(), BorderLayout.AFTER_LAST_LINE);

    String jMeterBinDirPath = getJMeterBinDirPath();

    RTETemplateRepository templateRepository = new RTETemplateRepository(
        jMeterBinDirPath + "/templates/");

    templateRepository.addRTETemplate(
        TEMPLATE_NAME,
        "/templates/" + TEMPLATE_NAME,
        "/templates/" + DESC_TEMPLATE_NAME,
        TEMPLATE_XML_NAME);
  }

  @VisibleForTesting
  public RTERecorderGui(RTERecorderPanel panel) {
    recordingPanel = panel;
  }

  public String getJMeterBinDirPath() {
    String siebelPluginPath = getClass().getProtectionDomain().getCodeSource().getLocation()
        .getPath();

    /*This is done to obtain and remove the initial `/` from the path.
      i.e: In Windows the path would be something like `/C:`,
      so we check if the char at position 3 is ':' and if so, we remove the initial '/'.
    */
    char middleChar = siebelPluginPath.charAt(2);
    if (middleChar == ':') {
      siebelPluginPath = siebelPluginPath.substring(1);
    }
    int index = siebelPluginPath.indexOf("/lib/ext/");
    String binPath = siebelPluginPath.substring(0, index) + "/bin";
    return binPath;
  }

  @Override
  public String getStaticLabel() {
    return "bzm - RTE Recorder";
  }

  @Override
  public String getLabelResource() {
    throw new IllegalStateException("This shouldn't be called");
  }

  @Override
  public Collection<String> getMenuCategories() {
    return Collections.singletonList(MenuFactory.NON_TEST_ELEMENTS);
  }

  @Override
  public TestElement createTestElement() {
    RTERecorder rteRecorder = new RTERecorder();
    configureTestElement(rteRecorder);
    return rteRecorder;
  }

  @Override
  public void modifyTestElement(TestElement te) {
    configureTestElement(te);
    if (te instanceof RTERecorder) {
      recorder = (RTERecorder) te;
      recorder.setRecordingStateListener(recordingPanel);
      recorder.setServer(recordingPanel.getServer());
      recorder.setPort(recordingPanel.getPort());
      recorder.setProtocol(recordingPanel.getProtocol());
      recorder.setTerminalType(recordingPanel.getTerminalType());
      recorder.setSSLType(recordingPanel.getSSLType());
      recorder.setConnectionTimeout(recordingPanel.getConnectionTimeout());
      recorder.setTimeoutThresholdMillis(recordingPanel.getWaitConditionsTimeoutThresholdMillis());
    }
  }

  @Override
  public void configure(TestElement element) {
    super.configure(element);
    if (element instanceof RTERecorder) {
      RTERecorder recorder = (RTERecorder) element;
      recordingPanel.setServer(recorder.getServer());
      recordingPanel.setPort(String.valueOf(recorder.getPort()));
      recordingPanel.setProtocol(recorder.getProtocol());
      recordingPanel.setTerminalType(recorder.getTerminalType());
      recordingPanel.setSSLType(recorder.getSSLType());
      recordingPanel.setConnectionTimeout(String.valueOf(recorder.getConnectionTimeout()));
      recordingPanel.setWaitConditionsTimeoutThresholdMillis(String.valueOf(recorder
          .getTimeoutThresholdMillis()));
    }
  }

  @Override
  public void onRecordingStart() {
    modifyTestElement(recorder);
    recorder.onRecordingStart();
  }

  @Override
  public void onRecordingStop() {
    recorder.onRecordingStop();
  }

  @Override
  public void onRecordingException(Exception e) {

  }
}
