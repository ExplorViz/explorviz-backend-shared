package net.explorviz.shared.landscape.model.landscape;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.explorviz.shared.landscape.model.application.Application;
import net.explorviz.shared.landscape.model.application.ApplicationCommunication;
import net.explorviz.shared.landscape.model.helper.BaseEntity;
import net.explorviz.shared.landscape.model.store.Timestamp;

/**
 * Model representing a software landscape.
 */
@SuppressWarnings("serial")
@Type("landscape")
public class Landscape extends BaseEntity {

  @Relationship("timestamp")
  private Timestamp timestamp;

  @Relationship("systems")
  private final List<System> systems = new ArrayList<>();

  private final Map<Long, String> events = new TreeMap<>();
  private final Map<Long, String> exceptions = new TreeMap<>();

  @Relationship("totalApplicationCommunications")
  private List<ApplicationCommunication> totalApplicationCommunications = new ArrayList<>();


  public Landscape() {
    super();
    this.timestamp = new Timestamp();
  }

  public Timestamp getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(final Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public List<System> getSystems() {
    return this.systems;
  }

  public Map<Long, String> getEvents() {
    return this.events;
  }

  public Map<Long, String> getExceptions() {
    return this.exceptions;
  }

  public List<ApplicationCommunication> getTotalApplicationCommunications() {
    return this.totalApplicationCommunications;
  }

  public void setTotalApplicationCommunications(
      final List<ApplicationCommunication> totalApplicationCommunications) {
    this.totalApplicationCommunications = totalApplicationCommunications;
  }

  /**
   * Clears all existing communication within the landscape.
   */
  private void clearCommunication() {

    // keeps applicationCommunication, but sets it to zero requests
    for (final ApplicationCommunication commu : this.getTotalApplicationCommunications()) {
      commu.reset();
    }

    for (final System system : this.getSystems()) {
      for (final NodeGroup nodegroup : system.getNodeGroups()) {
        for (final Node node : nodegroup.getNodes()) {
          for (final Application application : node.getApplications()) {
            application.clearCommunication();
          }
        }
      }
    }
  }

  /**
   * Resets the landscape.
   */
  public void reset() {
    this.getExceptions().clear();
    this.getEvents().clear();
    this.clearCommunication();
  }

}
