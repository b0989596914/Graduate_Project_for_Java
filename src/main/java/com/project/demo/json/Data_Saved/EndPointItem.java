package com.project.demo.json.Data_Saved;

public class EndPointItem {

  private final String className;
  private final String methodName;
  private final String httpMethod;
  private final String endpoint;

  public EndPointItem(String className, String methodName, String httpMethod, String endpoint) {
    this.className = className;
    this.methodName = methodName;
    this.httpMethod = httpMethod;
    this.endpoint = endpoint;
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public String getEndpoint() {
    return endpoint;
  }
}
