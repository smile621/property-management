package com.smile.eam.service;

import com.smile.eam.common.MD5Util;
import com.smile.eam.common.Pagination;
import com.smile.eam.common.UserContext;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import com.smile.eam.mapper.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    TokenMapper tokenMapper;
    @Resource
    MD5Util getMD5String;
    @Resource
    OrganizationMapper organizationMapper;
    @Resource
    DepartmentMapper departmentMapper;
    @Resource
    RoleMapper roleMapper;


    /**
     * 我是谁
     */
    @ApiOperation("whoami我是谁")
    public whoamiReturnDto whoami() {

        List<Integer> permissionIds = new ArrayList<>();
        AdminUser myUser = userMapper.findUserById(UserContext.getUser().getId());
        //通过userId查找所属部门
        Integer departmentId = userMapper.findDepartmentIdByUserId(UserContext.getUser().getId(), Asset.STATUS_DELETE);

        //通过userId查找对应的角色Id
        List<Integer> RoleIds = userMapper.findRoleListByUserId(UserContext.getUser().getId(), Asset.STATUS_DELETE);

        //通过角色Id查找所有的角色信息
        List<RoleIdNameDto> roleIdNameDtoList = new ArrayList<>();
        if (RoleIds.size() != 0) {
            roleIdNameDtoList = userMapper.findRoleNameById(RoleIds);
        }

        //通过角色id查找对应的权限
        if (RoleIds.size() != 0) {
            permissionIds = userMapper.findPermissionIdByRoleId(RoleIds, Asset.STATUS_DELETE);
        }


        //组装一个部门id名字Map
        Map<Integer, String> departmentMap = new HashMap<>();
        List<AdminDepartment> departments = departmentMapper.findDepartmentAll();
        for (AdminDepartment department : departments) {
            departmentMap.put(department.getId(), department.getName());
        }

        whoamiReturnDto whoamiReturnDto = new whoamiReturnDto();
        whoamiReturnDto.setAvatar(myUser.getAvatar());
        whoamiReturnDto.setNickname(myUser.getNickname());
        whoamiReturnDto.setDepartmentId(departmentId);
        whoamiReturnDto.setDepartment(departmentMap.get(departmentId));
        whoamiReturnDto.setRoleIdNameDtoList(roleIdNameDtoList);
        whoamiReturnDto.setCreatedAt(myUser.getCreatedAt());
        whoamiReturnDto.setEmail(myUser.getEmail());
        whoamiReturnDto.setId(myUser.getId());
        whoamiReturnDto.setJob(myUser.getJob());
        whoamiReturnDto.setPermission(permissionIds);
        whoamiReturnDto.setStatus(myUser.getStatus());
        whoamiReturnDto.setUsername(myUser.getUsername());
        whoamiReturnDto.setSex(myUser.getSex());
        whoamiReturnDto.setPhone(myUser.getPhone());
        whoamiReturnDto.setUpdatedAt(myUser.getUpdatedAt());

        return whoamiReturnDto;
    }

    /**
     * 更改当前用户
     */
    public boolean updateMe(NowUserUpdateDto nowUserUpdateDto) {

        AdminUser origin = userMapper.findByUsername(nowUserUpdateDto.getUsername());
        nowUserUpdateDto.setOldPassword(nowUserUpdateDto.getOldPassword(), origin.getPasswordHash());
        if (origin.getPasswordHash().equals(nowUserUpdateDto.getOldPassword())) {
            nowUserUpdateDto.setNewPassword(nowUserUpdateDto.getNewPassword(), origin.getPasswordHash());
        }else{
            return false;
        }
        userMapper.updateNowUser(nowUserUpdateDto);
        return true;
    }

    /**
     * 判断用户名、密码是否存在
     */
    public Boolean findUserByUsernameAndPassword(String username, String passwordHash) {
        //加密后的md5
        String passHash = getMD5String.getMD5String(passwordHash);
        System.out.println("passHash:" + passHash);
        UserIdResponse userId = userMapper.findUserByUsernameAndPassword(username, passHash);
        System.out.println("userId:" + userId);
        if (userId == null) {
            return false;
        }
        return true;
    }

    /**
     * 验证完用户名、密码，向admin_user_token传入用户名、随机token,返回添加的token
     */
    public String addToken(String username, String passwordHash) {
        //加密后的md5
        String passHash = getMD5String.getMD5String(passwordHash);
        System.out.println("passHash:" + passHash);
        //获取userId
        UserIdResponse userId = userMapper.findUserByUsernameAndPassword(username, passHash);
        //随机token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //传入token,userId到SQL
        tokenMapper.addToken(userId.getId(), token);
        return token;
    }

    /**
     * 用户列表数据组装
     */
    public List<UserResponseDto> packageData(List<AdminUser> userList) {

        //筛选出用户ID
        List<Integer> userIds = userList.stream().map(AdminUser::getId).collect(Collectors.toList());

        //根据用户id列表查找到部门id列表
        List<AdminUserDepartment> adminUserDepartments = new ArrayList<>();

        //根据用户id列查找到对应的用户——角色列表（一个用户对应多种角色）
        List<AdminUserRole> userRoles = new ArrayList<>();
        if (!userIds.isEmpty()) {
            adminUserDepartments = organizationMapper.findUserDepartmentByUserIds(userIds);
            userRoles = roleMapper.findUserRolesByUserIds(userIds);
        }

        //userId-departmentId  Map
        Map<Integer, Integer> userDepartmentIdMap = adminUserDepartments.stream()
                .collect(Collectors.toMap(AdminUserDepartment::getUserId, AdminUserDepartment::getDepartmentId));

        //获取部门id列表
        List<Integer> departmentIds = adminUserDepartments.stream().map(AdminUserDepartment::getDepartmentId).collect(Collectors.toList());

        //根据部门ID获取部门名
        List<AdminDepartment> departments = new ArrayList<>();
        if (!departmentIds.isEmpty()) {
            departments = departmentMapper.findDepartmentByDepartmentIds(departmentIds);
        }

        //组装部门ID和部门名Map<departmentId,departmentName>
        Map<Integer, String> departmentIdNameMap = departments.stream().collect(Collectors.toMap(AdminDepartment::getId, AdminDepartment::getName));

        //把userId和部门名组装
        Map<Integer, String> map1 = new HashMap<>();
        for (Integer id : userIds) {
            if (userDepartmentIdMap.get(id) != null) {
                map1.put(id, departmentIdNameMap.get(userDepartmentIdMap.get(id)));
            } else {
                map1.put(id, null);
            }
        }

        //筛选角色id列表
        Set<Integer> roleIdsSet = userRoles.stream().map(AdminUserRole::getRoleId).collect(Collectors.toSet());

        //根据用户id分组 Map<userId,List<UserRole>>
        Map<Integer, List<AdminUserRole>> userRoleGroup = userRoles.stream().collect(Collectors.groupingBy(AdminUserRole::getUserId));

        //Map<Integer,List<Integer>>  用户Id和角色id列表
        Map<Integer, List<Integer>> userIdRoleIdsMap = new HashMap<>();
        for (Integer id : userIds) {
            if (userRoleGroup.get(id) != null) {
                List<Integer> tempList = userRoleGroup.get(id)
                        .stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
                userIdRoleIdsMap.put(id, tempList);
            }
        }

        //根据角色Id获取角色名  组装Map<Integer,String>
        Map<Integer, String> roleIdNameMap = new HashMap<>();
        if (!roleIdsSet.isEmpty()) {
            roleIdNameMap = roleMapper.findRoleByRoleIds(roleIdsSet)
                    .stream().collect(Collectors.toMap(AdminRole::getId, AdminRole::getName));
        }

        //Map<Integer,List<String>>	  用户ID和角色名列表
        Map<Integer, List<String>> userIdRoleNamesMap = new HashMap<>();
        for (Integer userId : userIds) {
            List<Integer> temp = new ArrayList<>();
            if (userIdRoleIdsMap.containsKey(userId)) {
                temp = userIdRoleIdsMap.get(userId).stream().collect(Collectors.toList());
            }
            List<String> temp2 = new ArrayList<>();
            for (Integer roleId : temp) {
                temp2.add(roleIdNameMap.get(roleId));
            }
            userIdRoleNamesMap.put(userId, temp2);
        }

        //通过角色ID对权限进行分组   Map<roleId, List<RolePermission>>
        Map<Integer, List<AdminRolePermission>> rolePermissionGroup = new HashMap<>();
        if (!roleIdsSet.isEmpty()) {
            rolePermissionGroup = organizationMapper.searchPermissions(roleIdsSet)
                    .stream().collect(Collectors.groupingBy(AdminRolePermission::getRoleId));
        }

        Map<Integer, List<Integer>> permissionsMap = new HashMap<>();
        for (Integer id : userIds) {
            List<Integer> allPermissions = new ArrayList<>();

            if (userDepartmentIdMap.get(id) == null) {
                continue;
            }
            if (userRoleGroup.get(id) == null) {
                continue;
            }
            List<AdminUserRole> roles = userRoleGroup.get(id);
            for (AdminUserRole role : roles) {
                List<Integer> rolePermissionIds = new ArrayList<>();
                if (rolePermissionGroup.containsKey(role.getRoleId())) {
                    rolePermissionIds = rolePermissionGroup.get(role.getRoleId()).stream()
                            .map(AdminRolePermission::getPermissionId).collect(Collectors.toList());
                }
                allPermissions.addAll(rolePermissionIds);
            }
            permissionsMap.put(id, allPermissions);
        }

        // 组装数据
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (AdminUser user : userList) {
            Set<Integer> allPermissions = new HashSet<>();
            Set<Integer> roleListSet = new HashSet<>();
            //设置权限
            if (permissionsMap.get(user.getId()) != null) {
                allPermissions = permissionsMap.get(user.getId()).stream().collect(Collectors.toSet());
            }
            if (userIdRoleIdsMap.get(user.getId()) != null) {
                roleListSet = userIdRoleIdsMap.get(user.getId()).stream().collect(Collectors.toSet());
            }
            Integer integer = userDepartmentIdMap.get(user.getId());
            if (!userDepartmentIdMap.containsKey(user.getId())) {
                integer = 0;
            }

            UserResponseDto dto = UserResponseDto.builder()
                    .id(user.getId())
                    .departmentId(integer)
                    .department(map1.get(user.getId()))
                    .email(user.getEmail())
                    .job(user.getJob())
                    .nickname(user.getNickname())
                    .permissionList(allPermissions)
                    .phone(user.getPhone())
                    .roleNameList(userIdRoleNamesMap.get(user.getId()))
                    .roleList(roleListSet)
                    .sex(user.getSex())
                    .username(user.getUsername())
                    .build();
            userResponseDtoList.add(dto);
        }
        return userResponseDtoList;
    }

    /**
     * 获取用户列表
     */
    public List<UserResponseDto> getUserResponse(Pagination pagination) {

        pagination.setTotal(userMapper.userCount());
        List<AdminUser> userList = userMapper.findAllUsers(pagination);
        List<UserResponseDto> userResponseDtoList = this.packageData(userList);
        return userResponseDtoList;
    }

    /**
     * 模糊搜索
     */
    public List<UserResponseDto> getUserResponse(UserSearchDto userSearchDto, Pagination pagination) {
        pagination.setTotal(userMapper.userSearchCount(userSearchDto));
        //根据搜索条件获取用户列表
        List<AdminUser> userList = userMapper.findSearchUsers(userSearchDto, pagination);
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        if (!userList.isEmpty()) {
            userResponseDtoList = this.packageData(userList);
        }
        return userResponseDtoList;
    }

    /**
     * 新增用户
     */
    public int createUser(UserCreateDto user) {
        user.setPasswordHash(user.getPassword());
        if (userMapper.findIdByUsername(user.getUsername()) != null) {
            return -1;
        }
        int row = userMapper.createUser(user);
        int userId = Integer.parseInt(userMapper.findIdByUsername(user.getUsername()));
        organizationMapper.createUserDepartment(userId, user.getDepartmentId());
        organizationMapper.createUserRoles(userId, user.getRoleList());
        return row;
    }

    /**
     * 编辑用户
     */
    public int updateUser(UserUpdateDto userUpdateDto) {
        AdminUser origin = userMapper.findUserById(userUpdateDto.getUserId());

        // 改用户名  先判断用户名存在否
        if (userMapper.findIdByUsername(userUpdateDto.getUsername()) != null &&
                Integer.parseInt(userMapper.findIdByUsername(userUpdateDto.getUsername())) != userUpdateDto.getUserId()) {
            return -1;
        }
        userUpdateDto.setPasswordHash(userUpdateDto.getPassword(), origin.getPasswordHash());
        int row = userMapper.updateUser(userUpdateDto);
        organizationMapper.deleteUserRole(userUpdateDto.getUserId());
        if (!userUpdateDto.getRoleList().isEmpty()) {
            organizationMapper.createUserRoles(userUpdateDto.getUserId(), userUpdateDto.getRoleList());
        }
        if (userUpdateDto.getDepartmentId() != 0) {
            departmentMapper.updateUserDepartment1(userUpdateDto.getDepartmentId(), userUpdateDto.getUserId());
            departmentMapper.updateUserDepartment2(userUpdateDto.getDepartmentId(), userUpdateDto.getUserId());
        }
        return row;
    }

    /**
     * 用户详情
     */
    public UserResponseDto detailUser(int userId) {

        AdminUser user = userMapper.findUserById(userId);

        //根据用户ID查找部门ID  admin_user_department
        int departmentId = 0;
        if (organizationMapper.findDepartmentIdByUserId(userId) != null) {
            departmentId = Integer.parseInt(organizationMapper.findDepartmentIdByUserId(userId));
        }

        //根据部门ID查找部门名admin_department
        String departmentName = departmentMapper.findDepartmentById(departmentId);

        //根据用户ID查找角色Ids  admin_user_role
        Set<Integer> roleIdsSet = new HashSet<>();
        List<Integer> roleIdsByUserId = organizationMapper.findRoleIdsByUserId(userId);
        if (!roleIdsByUserId.isEmpty()) {
            roleIdsSet = roleIdsByUserId.stream().collect(Collectors.toSet());
        }

        //根据角色IDS查找角色名列表
        List<String> roleNames = new ArrayList<>();

        //根据角色idsSet查找permissionIds    admin_role_permission
        Set<Integer> permissionSet = new HashSet<>();
        if (!roleIdsSet.isEmpty()) {
            roleNames = roleMapper.findRoleByRoleIds(roleIdsSet).stream().map(AdminRole::getName).collect(Collectors.toList());
            permissionSet = organizationMapper.searchPermissions(roleIdsSet).stream()
                    .map(AdminRolePermission::getPermissionId).collect(Collectors.toSet());
        }

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .roleList(roleIdsSet)
                .username(user.getUsername())
                .sex(user.getSex())
                .phone(user.getPhone())
                .permissionList(permissionSet)
                .nickname(user.getNickname())
                .job(user.getJob())
                .email(user.getEmail())
                .department(departmentName)
                .roleNameList(roleNames)
                .departmentId(departmentId)
                .build();
        return userResponseDto;
    }

    /**
     * 删除用户
     */
    public void deleteUser(int userId) {
        userMapper.deleteUser(userId);
        tokenMapper.deleteAllToken(userId);
    }

    /**
     * 批量删除用户
     */
    public void deleteUsers(@RequestBody UserDeleteQueryDto userDeleteQueryDto) {
        userMapper.deleteUsers(userDeleteQueryDto.getIds());
        tokenMapper.deleteAllTokens(userDeleteQueryDto.getIds());
    }

    /**
     * 更新头像
     */
    public int updateUserAvatar(int id, String avatar) {
        return userMapper.updateUserAvatar(id, avatar);
    }

}
