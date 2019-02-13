package net.explorviz.shared.landscape.model.application;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;
import net.explorviz.shared.landscape.model.helper.BaseEntity;

/**
 * Model representing a trace containing severals {@link TraceStep} between two
 * {@link net.explorviz.shared.landscape.model.application.Clazz}.
 */
@SuppressWarnings("serial")
@Type("trace")
public class Trace extends BaseEntity {

  private String traceId;

  private int totalRequests;

  private float totalTraceDuration;

  private float averageResponseTime;

  @Relationship("traceSteps")
  private List<TraceStep> traceSteps = new ArrayList<>();


  public Trace(final String traceId) {
    this.setTraceId(traceId);
  }

  /**
   * Adds a new call within a trace as a {@link TraceStep}
   *
   * @param tracePosition - position within the trace
   * @param requests - total number of requests
   * @param averageResponseTime - average response time of the trace
   * @param currentTraceDuration - current duration of the trace
   * @param clazzCommunication - starting clazzCommunication
   * @return a new TraceStep
   */
  public TraceStep addTraceStep(final int tracePosition, final int requests,
      final float averageResponseTime, final float currentTraceDuration,
      final ClazzCommunication clazzCommunication) {

    final TraceStep newTraceStep = new TraceStep(this, clazzCommunication, tracePosition, requests,
        averageResponseTime, currentTraceDuration);

    final float beforeSum = this.getTotalRequests() * averageResponseTime;
    final float currentSum = requests * averageResponseTime;

    this.setAverageResponseTime((beforeSum + currentSum) / (this.getTotalRequests() + requests));
    this.setTotalTraceDuration(newTraceStep.getCurrentTraceDuration());
    this.setTotalRequests(this.getTotalRequests() + requests);

    this.getTraceSteps().add(newTraceStep);

    return newTraceStep;
  }

  public String getTraceId() {
    return this.traceId;
  }

  public void setTraceId(final String traceId) {
    this.traceId = traceId;
  }

  public List<TraceStep> getTraceSteps() {
    return this.traceSteps;
  }

  public void setTraceSteps(final List<TraceStep> traceSteps) {
    this.traceSteps = traceSteps;
  }

  public float getTotalTraceDuration() {
    return this.totalTraceDuration;
  }

  public void setTotalTraceDuration(final float totalTraceDuration) {
    this.totalTraceDuration = totalTraceDuration;
  }

  public int getTotalRequests() {
    return this.totalRequests;
  }

  public void setTotalRequests(final int totalRequests) {
    this.totalRequests = totalRequests;
  }

  public float getAverageResponseTime() {
    return this.averageResponseTime;
  }

  public void setAverageResponseTime(final float averageResponseTime) {
    this.averageResponseTime = averageResponseTime;
  }

}
