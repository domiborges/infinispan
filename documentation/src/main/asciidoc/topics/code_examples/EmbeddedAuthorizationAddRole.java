MutablePermissionMapper mapper = (MutablePermissionMapper) cacheManager.getCacheManagerConfiguration().security().authorization().rolePermissionMapper();
//Add new authorization role with all write and listen permissions.
mapper.addRole(Role.newRole("roleNameOne", true, AuthorizationPermission.ALL_WRITE, AuthorizationPermission.LISTEN));
//Add new authorization role with all read and write permissions.
mapper.addRole(Role.newRole("roleNameTwo", true, AuthorizationPermission.READ, AuthorizationPermission.WRITE));
