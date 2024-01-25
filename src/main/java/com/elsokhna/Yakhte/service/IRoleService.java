package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.exception.RoleAlreadyExistsException;
import com.elsokhna.Yakhte.exception.UserAlreadyExistsException;
import com.elsokhna.Yakhte.model.Role;
import com.elsokhna.Yakhte.model.User;

import java.util.List;

public interface IRoleService {

    List<Role> getRoles();
    Role createRole(Role theRole) throws RoleAlreadyExistsException;
    void deleteRole(Long id);
    Role findByName(String name);
    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId) throws UserAlreadyExistsException;
    Role removeAllUsersFromRole(Long roleId);
}
