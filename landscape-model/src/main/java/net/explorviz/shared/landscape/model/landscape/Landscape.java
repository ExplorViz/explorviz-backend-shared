package net.explorviz.shared.landscape.model.landscape;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

import net.explorviz.shared.landscape.model.application.Application;
import net.explorviz.shared.landscape.model.application.ApplicationCommunication;
import net.explorviz.shared.landscape.model.event.EEventType;
import net.explorviz.shared.landscape.model.event.Event;
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

  @Relationship("events")
  private final List<Event> events = new ArrayList<>();

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

  public List<Event> getEvents() {
    return this.events;
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
    this.getEvents().clear();
    this.clearCommunication();
  }

  /**
   * Creates a new exception event to the list of events in the landscape
   *
   * @param landscape - related landscape
   * @param cause - cause of the exception
   */
  public void createNewException(final String cause) {
    long currentMillis = java.lang.System.currentTimeMillis();

    final List<Long> timestampsOfExceptionEvents =
        this.getEvents().stream().filter(e -> e.getEventType().equals(EEventType.EXCEPTION))
            .map(Event::getTimestamp).collect(Collectors.toList());

    while (timestampsOfExceptionEvents.contains(currentMillis)) {
      currentMillis++;
    }

    this.getEvents().add(new Event(currentMillis, EEventType.EXCEPTION, cause));
  }


  /**
   * Creates a new event to the list of events in the landscape
   *
   * @param landscape - related landscape
   * @param eventType - type of event
   * @param eventMesssage - message of the event
   */
  public void createNewEvent(final EEventType eventType, final String eventMesssage) {
    long currentMillis = java.lang.System.currentTimeMillis();

    final List<Long> timestampsOfEvents =
        this.getEvents().stream().filter(e -> !e.getEventType().equals(EEventType.EXCEPTION))
            .map(Event::getTimestamp).collect(Collectors.toList());

    while (timestampsOfEvents.contains(currentMillis)) {
      currentMillis++;
    }

    this.getEvents().add(new Event(currentMillis, eventType, eventMesssage));
  }

  /**
   * Creates outgoing communication between applications in this landscape
   */
  public void createOutgoingApplicationCommunication() {
    for (final ApplicationCommunication communication : this.getTotalApplicationCommunications()) {
      final Application sourceApp = communication.getSourceApplication();
      if (sourceApp != null) {
        sourceApp.getApplicationCommunications().add(communication);
      }
    }

  }

}
