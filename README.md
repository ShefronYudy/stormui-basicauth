# Storm UI http basic auth

Storm UI http basic auth

## configuration

#### storm.yaml
```
ui.filter: "org.shefron.storm.ui.http.HTTPBasicAuthFilter"
ui.filter.params:
  "http.basic.enabled" : "true"
  "http.basic.username" : "admin"
  "http.basic.password" : "admin"
```  
## dependency

`mvn clean package` and add `stormui-basicauth-0.0.1.jar` to ${STORM_HOME}/lib or ${STORM_HOME}/extlib

## restart storm ui process


