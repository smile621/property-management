package com.smile.eam.controller;
import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.common.UserContext;
import com.smile.eam.service.TodoService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "代办相关接口")
@RestController
@CrossOrigin
public class TodoController {
    @Resource
    TodoService todoService;
    /**
     * 我的待办接口(创建)..
     * */
    @ApiOperation("我的待办列表接口(创建)")
    @GetMapping("/api/todo/getMyCreateTodo")
    public JsonResult<MyCreateToDoDto> myCreateToDo(String token, Pagination pagination){
        //获取userId
        int userId = todoService.findUserIdByToken(token);
        //获取列表总数
        int total = todoService.myCreateToDoCount(userId);
        pagination.setTotal(total);
        //获取我创建的待办列表
        List<ToDoResponse> myCreateToDoList = todoService.myCreateToDo(userId,pagination);
        MyCreateToDoDto data = MyCreateToDoDto.builder()
                .myCreateToDoList(myCreateToDoList)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 我的待办接口(处理)
     * */
    @ApiOperation("我的待办列表接口(处理)")
    @GetMapping("/api/todo/getMyHandleTodo")
    public JsonResult<MyHandleToDoDto> myHandleToDo(String token,Pagination pagination){
        //获取userId
        int userId = todoService.findUserIdByToken(token);
        //获取列表总数
        int total = todoService.myHandleToDoCount(userId);
        pagination.setTotal(total);
        //获取我处理的待办列表
        List<ToDoResponse> myHandleToDoList = todoService.myHandleToDo(userId,pagination);
        MyHandleToDoDto data = MyHandleToDoDto.builder()
                .myHandleToDoList(myHandleToDoList)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 获取全部待办接口
     * */
    @ApiOperation("获取全部待办列表接口")
    @GetMapping("/api/todo/getMyTodo")
    public JsonResult<MyToDoDto> myToDo(Pagination pagination){
        //获取列表总数
        int total = todoService.myToDoCount();
        pagination.setTotal(total);
        //获取全部待办列表
        List<ToDoResponse> myToDoList = todoService.myToDo(pagination);
        MyToDoDto data = MyToDoDto.builder()
                .myToDoList(myToDoList)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 待办列表批量删除
     * */
    @ApiOperation("待办列表批量删除接口")
    @PostMapping("/api/todo/deleteTodoList")
    public JsonResult<String> deleteToDoList(@Valid SoftwareListDeleteRequest softwareListDeleteRequest){
        todoService.deleteToDoList(softwareListDeleteRequest);
        return new JsonResult<>("批量删除成功");
    }

    /**
     * 新建待办接口
     * */
    @ApiOperation("新建待办接口")
    @PostMapping("/api/todo/createToto")
    public JsonResult<String> addToDo(@Valid CreateToDoRequest createToDoRequest, String token){
        //获取userId
        int userId = todoService.findUserIdByToken(token);
        //是否已存在未完成相同待办
        Boolean t = todoService.existToDo(createToDoRequest);
        if(t){
            return new JsonResult<>("ERROR","已存在该待办");
        }
        todoService.addToDo(createToDoRequest,userId);
        return new JsonResult<>("SUCCESS","新增成功");
    }
    /**
     * 传入id，获取待办具体信息
     * */
    @ApiOperation("传入id，获取待办具体信息接口")
    @GetMapping("/api/todo/getToDoByList")
    public JsonResult<List<ToDoResponse>> getToDoByList(@Valid DeleteToDoRequest deleteToDoRequest){
        //传入待办id，查看详情
        List<ToDoResponse> toDo = todoService.getToDoById(deleteToDoRequest.getId());
        return new JsonResult<>(toDo);
    }

    /**
     * 个人，删除待办接口，只能删除自己创建的待办，别人无权删除
     * */
    @ApiOperation("删除待办接口，只能删除自己创建的待办，别人无权删除")
    @GetMapping("/api/todo/deleteTodo")
    public JsonResult<String> deleteToDo(@Valid DeleteToDoRequest deleteToDoRequest,String token){
        //获取userId
        int userId = todoService.findUserIdByToken(token);
        //是否存在该待办
        Boolean t = todoService.existToDoById(deleteToDoRequest,userId);
        if(!t){
            return new JsonResult<>("ERROR","未找到或您没有权限删除该待办");
        }
        int i = todoService.deleteToDo(deleteToDoRequest);
        if(i<1){
            return new JsonResult<>("ERROR","删除失败");
        }
        return new JsonResult<>("删除成功");
    }

    /**
     * 本人，在待处理阶段，修改待办，待办只有在还未被领用才可以修改
     * */
    @ApiOperation("修改待办接口(待处理)")
    @PostMapping("/api/todo/editToto")
    public JsonResult<String> editToDo(@Valid EditToDoRequest editToDoRequest, String token){
        //根据token,获取userId;
        int userId = todoService.findUserIdByToken(token);
        //待办是否为本人创建，且为待处理
        Boolean t = todoService.existToDoByUserIdAndId(editToDoRequest,userId);
        if(!t){
            return new JsonResult<>("ERROR","未查到该待办或该待办无法被修改");
        }
        //是否修改
        Boolean to = todoService.updateToDoByUserIdAndId(editToDoRequest,userId);
        if(!to){
            return new JsonResult<>("ERROR","更新失败");
        }
        return new JsonResult<>("更新成功");
    }

    /**
     * 处理待办接口，就是添加结束描述和修改状态
     * */
    @ApiOperation("处理待办接口")
    @PostMapping("/api/todo/disposeTodo")
    public JsonResult<String> handleToDo(@Valid HandleToDoRequest handleToDoRequest, String token){
        //根据token,获取userId;
        int solveId = todoService.findUserIdByToken(token);
        //验证处理人id及待办id,是否存在需处理待办
        Boolean flag = todoService.existHandleToDo(handleToDoRequest, solveId);
        if(!flag){
            return new JsonResult<>("ERROR","没有找到或您没有权限处理该待办");
        }
        //是否处理待办成功
        Boolean h = todoService.isHandleToDo(handleToDoRequest, solveId);
        if(!h){
            return new JsonResult<>("ERROR","处理失败");
        }
        return new JsonResult<>("处理成功");
    }

    /**
     * 待办模糊查找列表[全部]
     * */
    @ApiOperation("待办模糊查找列表[全部]")
    @GetMapping("/api/todo/findAllByName")
    public JsonResult<FindAllByNameDto> findAllByName(String name,Pagination pagination){
        //获取模糊查找列表总个数
        int total = todoService.findByName(name);
        pagination.setTotal(total);
        //获取模糊查找列表
        List<ToDoResponse> countByName = todoService.findByName(name, pagination);
        FindAllByNameDto data = FindAllByNameDto.builder()
                .countByName(countByName)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 待办模糊查找[我的发送]
     * */
    @ApiOperation("待办模糊查找列表[我的发送]")
    @GetMapping("/api/todo/findMyByName")
    public JsonResult<FindMyByNameDto> findMyByName(String name,Pagination pagination){
        //获取userId
        int userId = UserContext.getUser().getId();
        //获取模糊查找列表总个数
        int total = todoService.findMyPostByNameListCount(name, userId);
        pagination.setTotal(total);
        //获取模糊查找列表
        List<ToDoResponse> countByName = todoService.findMyByName(name, pagination, userId);
        FindMyByNameDto data = FindMyByNameDto.builder()
                .countByName(countByName)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 待办模糊查找【我的处理】
     * */
    @ApiOperation("待办模糊查找列表[我的处理]")
    @GetMapping("/api/todo/findMyHandleByName")
    public JsonResult<FindMyHandleByNameDto> findMyHandleByName(String name,Pagination pagination){
        //获取userId
        int userId = UserContext.getUser().getId();
        //获取模糊查找列表总个数
        int total = todoService.findMyHandleByNameCount(name, userId);
        pagination.setTotal(total);
        //获取模糊查找列表
        List<ToDoResponse> myHandleByName = todoService.findMyHandleByName(name, pagination, userId);
        FindMyHandleByNameDto data = FindMyHandleByNameDto.builder()
                .myHandleByName(myHandleByName)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }
}
