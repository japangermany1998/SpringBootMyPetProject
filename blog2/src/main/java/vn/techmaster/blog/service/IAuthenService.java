package vn.techmaster.blog.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.model.Role;

public interface IAuthenService {
  public UserInfo login(LoginRequest loginRequest) throws AuthenException;

    void addRoleSupporter();

    Role getRoleuser(String role);

    public boolean isLogined(HttpServletRequest request);

  public UserInfo getLoginedUser(HttpServletRequest request);

  public void setLoginedCookie(HttpServletResponse response, UserInfo user);

  public void clearLoginedCookie(HttpServletResponse response);
} 
