package com.lunarpatriots.dungeoncraft.common.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ClientConfig {
  private String username;
  private String password;

//  public String getUsername() {
//    return StringUtils.isNotBlank(username)
//      ? this.username
//      : StringUtils.EMPTY;
//  }
//
//  public String getPassword() {
//    return StringUtils.isNotBlank(password)
//      ? this.password
//      : StringUtils.EMPTY;
//  }

  public Map<String, Object> toMap() {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put("username", this.getUsername());
    map.put("password", this.getPassword());

    return map;
  }
}
