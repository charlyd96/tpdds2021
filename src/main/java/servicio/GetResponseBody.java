package servicio;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "total",
    "offset",
    "hogares"
})
public class GetResponseBody {

  @JsonProperty("total")
  private Integer total;
  @JsonProperty("offset")
  private String offset;
  @JsonProperty("hogares")
  private List<Hogar> hogares = null;

  public GetResponseBody() {
  }

   public GetResponseBody(Integer total, String offset, List<Hogar> hogares) {
    super();
    this.total = total;
    this.offset = offset;
    this.hogares = hogares;
  }

  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  @JsonProperty("offset")
  public String getOffset() {
    return offset;
  }

  @JsonProperty("offset")
  public void setOffset(String offset) {
    this.offset = offset;
  }

  @JsonProperty("hogares")
  public List<Hogar> getHogares() {
    return hogares;
  }

  @JsonProperty("hogares")
  public void setHogares(List<Hogar> hogares) {
    this.hogares = hogares;
  }

}