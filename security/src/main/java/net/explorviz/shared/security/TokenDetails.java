package net.explorviz.shared.security;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Model class for JSON web tokens.
 */
// cassiomolin - https://github.com/cassiomolin/jersey-jwt
public final class TokenDetails {

  private final String id;
  private final String username;
  private final List<String> roles;
  private final String userid;
  private final ZonedDateTime issuedDate;
  private final ZonedDateTime expirationDate;
  private final int refreshCount;
  private final int refreshLimit;

  private TokenDetails(final String id, final String username, final List<String> roles,
      final ZonedDateTime issuedDate, final ZonedDateTime expirationDate, final int refreshCount,
      final int refreshLimit, final String userid) {
    this.id = id;
    this.username = username;
    this.roles = roles;
    this.issuedDate = issuedDate;
    this.expirationDate = expirationDate;
    this.refreshCount = refreshCount;
    this.refreshLimit = refreshLimit;
    this.userid = userid;
  }

  public String getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public List<String> getRoles() {
    return this.roles;
  }

  public ZonedDateTime getIssuedDate() {
    return this.issuedDate;
  }

  public ZonedDateTime getExpirationDate() {
    return this.expirationDate;
  }

  public int getRefreshCount() {
    return this.refreshCount;
  }

  public int getRefreshLimit() {
    return this.refreshLimit;
  }


  public String getUserId() {
    return this.userid;
  }

  /**
   * Check if the authentication token is eligible for refreshment.
   *
   * @return if a token needs to be refreshed
   */
  public boolean isEligibleForRefreshment() {
    return this.refreshCount < this.refreshLimit;
  }

  /**
   * Builder for the {@link TokenDetails}.
   */
  public static class Builder {

    private String id;
    private String userid;
    private String username;
    private List<String> roles;
    private ZonedDateTime issuedDate;
    private ZonedDateTime expirationDate;
    private int refreshCount;
    private int refreshLimit;

    public Builder withId(final String id) {
      this.id = id;
      return this;
    }

    public Builder withUsername(final String username) {
      this.username = username;
      return this;
    }

    public Builder withAuthorities(final List<String> roles) {
      this.roles = roles;
      return this;
    }

    public Builder withIssuedDate(final ZonedDateTime issuedDate) {
      this.issuedDate = issuedDate;
      return this;
    }

    public Builder withExpirationDate(final ZonedDateTime expirationDate) {
      this.expirationDate = expirationDate;
      return this;
    }

    public Builder withRefreshCount(final int refreshCount) {
      this.refreshCount = refreshCount;
      return this;
    }

    public Builder withRefreshLimit(final int refreshLimit) {
      this.refreshLimit = refreshLimit;
      return this;
    }

    public Builder withUserId(final String userid) {
      this.userid = userid;
      return this;
    }

    public TokenDetails build() {
      return new TokenDetails(this.id, this.username, this.roles, this.issuedDate,
          this.expirationDate, // NOPMD
          this.refreshCount, this.refreshLimit, this.userid);
    }
  }
}
