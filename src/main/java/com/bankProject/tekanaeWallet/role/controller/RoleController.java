package com.bankProject.tekanaeWallet.role.controller;

import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.role.dto.ResponseDto;
import com.bankProject.tekanaeWallet.role.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/role")
@Api(tags = "Role")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(
            value = "User role permission",
            notes = "Give user role to access different specified resources (only accessible to admins)"
    )
    public ResponseDto assignRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) throws NotFoundException {
        return roleService.addRolePermissions(userId, roleId);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(
            value = "User role permission",
            notes = "Remove user role to access different specified resources (only accessible to admins)"
    )
    public ResponseDto removeRolePermission(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) throws NotFoundException {
        return roleService.removeRolePermission(userId, roleId);
    }
}
