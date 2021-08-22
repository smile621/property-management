package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.ToDoRecord;
import com.smile.eam.mapper.StatusMapper;
import com.smile.eam.mapper.ToDoRecordMapper;
import com.smile.eam.mapper.TokenMapper;
import com.smile.eam.mapper.UserMapper;
import com.smile.eam.dto.*;
import com.smile.eam.entity.AdminUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TodoService {
    @Resource
    ToDoRecordMapper toDoRecordMapper;
    @Resource
    TokenMapper tokenMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StatusMapper statusMapper;
    /**
     * 判断待办是否已经存在
     * */
    public Boolean existToDo(CreateToDoRequest createToDoRequest){
        List<ToDoRecord> t = toDoRecordMapper.existToDo(createToDoRequest);
        if(t.size()==0){
            return false;
        }
        return true;
    }
    /**
     * 根据传入token,匹配userId
     * */
    public int findUserIdByToken(String token){
        return tokenMapper.findUserIdByToken(token).getUserId();
    }

    /**
     * 新增代办
     * */
    public void addToDo( CreateToDoRequest createToDoRequest,Integer userId){
        toDoRecordMapper.addToDo(createToDoRequest,userId);
    }

    /**
     * 根据传入id，判断待办是否存在，存在就可以编辑，已删除，已完成不可编辑
     * */
    public Boolean existToDoById(DeleteToDoRequest deleteToDoRequest, Integer UserId){
        ToDoRecord t = toDoRecordMapper.existToDoById(deleteToDoRequest,UserId);
        if(t==null){
            return false;
        }
        return true;
    }

    /**
     * 根据传入id,删除该待办
     * */
    public int deleteToDo(DeleteToDoRequest deleteToDoRequest){
        return toDoRecordMapper.deleteToDo(deleteToDoRequest);
    }

    /**
     * 传入userId,id,判断是否有对应的待办
     * */
    public Boolean existToDoByUserIdAndId(EditToDoRequest editToDoRequest, Integer userId){
        ToDoRecord toDoRecord = toDoRecordMapper.existToDoByUserIdAndId(editToDoRequest,userId);
        if(toDoRecord==null){
            return false;
        }
        return true;
    }

    /**
     * 传入更新信息，修改该待办
     * */
    public Boolean updateToDoByUserIdAndId(EditToDoRequest editToDoRequest,Integer userId){
        int t = toDoRecordMapper.updateToDoByUserIdAndId(editToDoRequest,userId);
        if(t<1){
            return false;
        }
        return true;
    }

    /**
     * 验证处理人id与待办id,且状态为5：待处理 6：处理中..
     * */
    public Boolean existHandleToDo(HandleToDoRequest handleToDoRequest, Integer solveId){
        ToDoRecord record = toDoRecordMapper.existHandleToDo(handleToDoRequest, solveId);
        if(record==null){
            return false;
        }
        return true;
    }

    /**
     * 处理待办，添加处理描述以及修改待办状态
     * */
    public Boolean isHandleToDo(HandleToDoRequest handleToDoRequest,Integer SolveId){
        int h = toDoRecordMapper.isHandleToDo(handleToDoRequest, SolveId);
        if(h<1){
            return false;
        }
        return true;
    }

    /**
     * 传入一个list,获得ToDoResponse列表
     * */
    public List<ToDoResponse> getToDoResponseList(List<ToDoRecord> list){
        //加载用户列表信息
        List<AdminUser> allUsersList = userMapper.findAllUsersList();
        //组装map
        Map<Integer,String> userMap = new HashMap<>();
        for (AdminUser user : allUsersList) {
            userMap.put(user.getId(),user.getUsername());
        }
        List<ToDoResponse> toDoResponseList = new ArrayList<>();
        for (ToDoRecord li : list) {
            ToDoResponse toDoResponse = ToDoResponse.builder()
                    .id(li.getId())
                    .userId(userMap.get(li.getUserId()))
                    .solveId(userMap.get(li.getSolveId()))
                    .title(li.getTitle())
                    .description(li.getDescription())
                    .priority(ToDoRecord.builder().priority(li.getPriority()).build().getPriorityAlias())
                    .handleDescription(li.getHandleDescription())
                    .tag(li.getTag())
                    .status(ToDoRecord.builder().status(li.getStatus()).build().getStatusAlias())
                    .createdAt(li.getCreatedAt())
                    .startAt(li.getStartAt())
                    .updatedAt(li.getUpdatedAt())
                    .build();
            toDoResponseList.add(toDoResponse);

        }
        return toDoResponseList;
    }

    /**
     * 获取我创建的待办列表
     * */
    public List<ToDoResponse> myCreateToDo(Integer userId, Pagination pagination){
        List<ToDoRecord> myCreateToDoList = toDoRecordMapper.myCreateToDo(userId, pagination);
        return getToDoResponseList(myCreateToDoList);
    }

    /**
     * 获取我创建的待办列表总数
     * */
    public int myCreateToDoCount(Integer userId){
        return toDoRecordMapper.myCreateToDoCount(userId);
    }

    /**
     * 获取我处理的待办列表
     * */
    public List<ToDoResponse> myHandleToDo(Integer solveId, Pagination pagination){
        List<ToDoRecord> myHandleToDoList = toDoRecordMapper.myHandleToDo(solveId, pagination);
        return getToDoResponseList(myHandleToDoList);
    }

    /**
     * 获取我处理的待办列表总数
     * */
    public int myHandleToDoCount(Integer userId){
        return toDoRecordMapper.myHandleToDoCount(userId);
    }


    /**
     * 获取全部待办列表
     * */
    public List<ToDoResponse> myToDo(Pagination pagination){
        List<ToDoRecord> myToDoList = toDoRecordMapper.myToDo(pagination);
        return getToDoResponseList(myToDoList);
    }

    /**
     * 获取全部待办列表总数
     * */
    public int myToDoCount(){
        return toDoRecordMapper.myToDoCount();
    }

    /**
     * 待办模糊查找列表
     * */
    public List<ToDoResponse> findByName(String name,Pagination pagination){
        List<ToDoRecord> byNameList = toDoRecordMapper.findByNameList(name, pagination);
        return  getToDoResponseList(byNameList);
    }

    /**
     * 待办模糊查找[我的发送]
     * */
    public List<ToDoResponse> findMyByName(String name,Pagination pagination,Integer UserId){
        List<ToDoRecord> myPostByNameList = toDoRecordMapper.findMyPostByNameList(name, pagination, UserId);
        return getToDoResponseList(myPostByNameList);
    }

    /**
     * 待办模糊查找[我的处理]
     * */
    public List<ToDoResponse> findMyHandleByName(String name,Pagination pagination,Integer UserId){
        List<ToDoRecord> myHandleByName = toDoRecordMapper.findMyHandleByName(name, pagination, UserId);
        return getToDoResponseList(myHandleByName);
    }


    /**
     * 待办模糊查找个数
     * */
    public int findByName(String name){
        return  toDoRecordMapper.findByNameListCount(name);
    }

    /**
     * 待办模糊查找个数[我的发送]
     * */
    public int findMyPostByNameListCount(String name,Integer UserId){
        return  toDoRecordMapper.findMyPostByNameListCount(name,UserId);
    }

    /**
     * 待办模糊查找个数[我的处理]
     * */
    public int findMyHandleByNameCount(String name,Integer UserId){
        return toDoRecordMapper.findMyHandleByNameCount(name,UserId);
    }

    /**
     * 待办列表批量删除
     * */
    public void deleteToDoList(SoftwareListDeleteRequest softwareListDeleteRequest){
        toDoRecordMapper.deleteToDoList(softwareListDeleteRequest.getList());
    }

    /**
     * 传入待办id，获取详情
     * */
    public  List<ToDoResponse> getToDoById(Integer id){
        List<ToDoRecord> toDoById = toDoRecordMapper.getToDoById(id);
        return getToDoResponseList(toDoById);
    }
}
