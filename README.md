VolleyJacksonRequest
====================
Library containing Volley and Jackson and JsonRequest implementation to make a network request and map the response to a POJO.

[Download the jar](https://drive.google.com/file/d/0B8SKWiDDCzx4a19qczlQcW9iWFk/view?usp=sharing)

Usage
=====

- Assume the endpoint "my_endpoint" returns

```json5
{ Property1: "1", Property2: 2 }
```

- Define your POJO

```java
public class MyPOJO {
  private String property1;
  private int property2;
  
  @JsonProperty("Property1")
  public void setProperty1(String property1) {
    this.property1 = property1;
  }
  
  @JsonProperty("Property2")
  public void setProperty2(int property2) {
    this.property2 = property2;
  }
  
  public String getProperty1() {
    return property1;
  }
  
  public int getProperty2() {
    return property2;
  }
}
```

- Make the request

```java
...

public void makeRequest() {
  VJRequest<MyPOJO> pojoRequest = new VJRequest<MyPOJO>(
    Method.GET,
    "my_endpoint",
    new TypeReference<MyPOJO>() {},
    new Response.Listener<MyPOJO>() {
      
      @Override
      public void onResponse(MyPOJO response) {
        processResponse(response.getProperty1(), response.getProperty2());
      }
      
    },
    new Response.ErrorListener() {
    
      @Override
      public void onErrorResponse(VolleyError error) {
        processError(error);
      }
      
    });
}

...
```
