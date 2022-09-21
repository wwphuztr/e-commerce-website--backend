package com.nails.api.controller;

import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.group.GroupAdminDto;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.group.CreateGroupForm;
import com.nails.api.form.group.UpdateGroupForm;
import com.nails.api.mapper.GroupMapper;
import com.nails.api.storage.criteria.GroupCriteria;
import com.nails.api.storage.model.Group;
import com.nails.api.storage.model.Permission;
import com.nails.api.storage.repository.GroupRepository;
import com.nails.api.storage.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class GroupController extends ABasicController{
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    GroupMapper groupMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<GroupAdminDto>> getList(GroupCriteria groupCriteria){
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed to get");
        }

        ApiMessageDto<ResponseListObj<GroupAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Group> groupPage = groupRepository.findAll(groupCriteria.getSpecification(),Pageable.unpaged());

        ResponseListObj<GroupAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(groupMapper.fromEntityListToAdminDtoList(groupPage.getContent()));

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List group success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list_combobox", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<GroupAdminDto>> getListCombox(){
        GroupCriteria groupCriteria = new GroupCriteria();
        ApiMessageDto<ResponseListObj<GroupAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Group> groupPage = groupRepository.findAll(groupCriteria.getSpecification(),Pageable.unpaged());

        ResponseListObj<GroupAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(groupMapper.fromEntityListToAdminDtoList(groupPage.getContent()));

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List group combobox success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateGroupForm createGroupForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Group group = groupRepository.findFirstByName(createGroupForm.getName());
        if(group != null){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Group name is exist");
            return apiMessageDto;
        }
        group = new Group();
        group.setName(createGroupForm.getName());
        group.setDescription(createGroupForm.getDescription());
        group.setKind(createGroupForm.getKind());
        List<Permission> permissions = new ArrayList<>();
        for(int i=0;i< createGroupForm.getPermissions().length;i++){
            Permission permission = permissionRepository.findById(createGroupForm.getPermissions()[i]).orElse(null);
            if(permission != null){
                permissions.add(permission);
            }
        }
        group.setStatus(1);
        group.setPermissions(permissions);
        groupRepository.save(group);
        apiMessageDto.setMessage("Create group success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateGroupForm updateGroupForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Group group = groupRepository.findById(updateGroupForm.getId()).orElse(null);
        if(group == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Group name doesnt exist");
            return apiMessageDto;
        }
        //check su ton tai cua group name khac khi dat ten.
        Group otherGroup = groupRepository.findFirstByName(updateGroupForm.getName());
        if(otherGroup != null && !Objects.equals( updateGroupForm.getId(),otherGroup.getId()) ){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Cant update this group name because it is exist");
            return apiMessageDto;
        }
        group.setName(updateGroupForm.getName());
        group.setDescription(updateGroupForm.getDescription());
        List<Permission> permissions = new ArrayList<>();
        for(int i=0;i< updateGroupForm.getPermissions().length;i++){
            Permission permission = permissionRepository.findById(updateGroupForm.getPermissions()[i]).orElse(null);
            if(permission != null){
                permissions.add(permission);
            }
        }
        group.setPermissions(permissions);
        groupRepository.save(group);
        apiMessageDto.setMessage("Update group success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<GroupAdminDto> get(@PathVariable("id")  Long id) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed to get.");
        }
        ApiMessageDto<GroupAdminDto> apiMessageDto = new ApiMessageDto<>();
        Group group =groupRepository.findById(id).orElse(null);
        apiMessageDto.setData(groupMapper.fromEntityToGroupAdminDto(group));
        apiMessageDto.setMessage("Get group success");
        return apiMessageDto;
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id){
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed to delete");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Group group = groupRepository.findById(id).orElse(null);
        if(group == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Group does not exist to delete");
            return apiMessageDto;
        }
        if(group.getKind() == 1){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("This group kind can not be deleted");
            return apiMessageDto;
        }
        List<Permission> permissions = new ArrayList<>();
        group.setPermissions(permissions);
        groupRepository.save(group);
        groupRepository.deleteById(id);
        apiMessageDto.setMessage("Delete group success");
        return apiMessageDto;
    }
}
