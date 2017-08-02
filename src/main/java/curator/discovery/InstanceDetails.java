package curator.discovery;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("details")
public class InstanceDetails {

  private String description;

  public InstanceDetails() {
    this("");
  }

  public InstanceDetails(String description) {
    this.description = description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
