package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.CreateToDoRequest;
import com.smile.eam.dto.DeleteToDoRequest;
import com.smile.eam.dto.EditToDoRequest;
import com.smile.eam.dto.HandleToDoRequest;
import com.smile.eam.entity.ToDoRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ToDoRecordMapper {


    /**
     * 根据传入内容判断待办是否已经存在solveId,title,description,7已完成待办
     */
    @Select("select * from todo_record where `solve_id`=#{solveId} and `title`=#{title} and " +
            "`description`=#{description} and `status` in (" + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + ")")
    List<ToDoRecord> existToDo(CreateToDoRequest createToDoRequest);

    /**
     * 根据传入id判断待办是否已经存在,100已删除，不可查找
     */
    @Select("select * from todo_record where `id`=#{delete.id} and `user_id`=#{user_id} and `status` in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT + ")")
    ToDoRecord existToDoById(@Param("delete") DeleteToDoRequest deleteToDoRequest, @Param("user_id") Integer userId);

    /**
     * 新增待办
     */
    @Insert("insert into `todo_record` (`user_id`,`solve_id`,`title`,`description`,`priority`,`start_at`,`tag`,`status`) " +
            "values (#{user_id},#{add.solveId},#{add.title},#{add.description}," +
            "#{add.priority},#{add.startAt},#{add.tag},#{add.status})")
    void addToDo(@Param("add") CreateToDoRequest createToDoRequest, @Param("user_id") Integer userId);

    /**
     * 删除待办
     */
    @Update("update  `todo_record` set `status`=" + ToDoRecord.STATUS_DELETE + " where id=#{id}")
    int deleteToDo(DeleteToDoRequest deleteToDoRequest);

    /**
     * 传入userId,id,判断是否有对应的待办,且状态必须为5：待处理
     */
    @Select("select * from `todo_record` where `user_id`=#{user_id} and `id`=#{edit.id} and `status`=" + ToDoRecord.STATUS_PENDING)
    ToDoRecord existToDoByUserIdAndId(@Param("edit") EditToDoRequest editToDoRequest, @Param("user_id") Integer userId);

    /**
     * 传入更新信息，修改该待办
     */
    @Update("update `todo_record` set `solve_id`=#{edit.solveId},`title`=#{edit.title}," +
            "`description`=#{edit.description},`priority`=#{edit.priority}," +
            "`start_at`=#{edit.startAt},`tag`=#{edit.tag} where `user_id`=#{user_id} and `id`=#{edit.id} ")
    int updateToDoByUserIdAndId(@Param("edit") EditToDoRequest editToDoRequest, @Param("user_id") Integer userId);

    /**
     * 验证处理人id与待办id,且状态为5：待处理 6：处理中
     */
    @Select("select * from `todo_record` where `solve_id`=#{solve_id} and `id`=#{handle.id} and `status` in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + ")")
    ToDoRecord existHandleToDo(@Param("handle") HandleToDoRequest handleToDoRequest, @Param("solve_id") Integer solveId);

    /**
     * 处理待办，添加处理描述以及修改待办状态
     */
    @Update("update  `todo_record` set `handle_description`=#{handle.handleDescription},`status`=#{handle.status}" +
            " where `solve_id`=#{solve_id} and `id`=#{handle.id} and `status` in (" + ToDoRecord.STATUS_PENDING + ","
            + ToDoRecord.STATUS_BEING_PROCESSED + ")")
    int isHandleToDo(@Param("handle") HandleToDoRequest handleToDoRequest, @Param("solve_id") Integer solveId);

    /**
     * 获取我创建的待办列表
     */
    @Select("select * from `todo_record` where `user_id`=#{user_id} and status in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") order by id desc limit #{pagination.offset}, #{pagination.limit}")
    List<ToDoRecord> myCreateToDo(@Param("user_id") Integer userId, @Param("pagination") Pagination pagination);

    /**
     * 获取我创建的待办列表总数
     */
    @Select("select count(*) from `todo_record` where `user_id`=#{user_id} and status in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT + ")")
    int myCreateToDoCount(@Param("user_id") Integer userId);

    /**
     * 获取我处理的待办列表
     */
    @Select("select * from `todo_record` where `solve_id`=#{solve_id} and status in (" +
            +ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") order by id desc " +
            "limit #{pagination.offset}, #{pagination.limit}")
    List<ToDoRecord> myHandleToDo(@Param("solve_id") Integer solveId, @Param("pagination") Pagination pagination);

    /**
     * 获取我处理的待办列表总数
     */
    @Select("select count(*) from `todo_record` where `solve_id`=#{solve_id} and status in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT + ")")
    int myHandleToDoCount(@Param("solve_id") Integer solveId);

    /**
     * 获取全部待办列表
     */
    @Select("select * from `todo_record` where status in (" +
            +ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") order by id desc limit #{pagination.offset}, #{pagination.limit}")
    List<ToDoRecord> myToDo(@Param("pagination") Pagination pagination);

    /**
     * 获取全部待办列表总数
     */
    @Select("select count(*) from `todo_record` where status in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT + ")")
    int myToDoCount();

    /**
     * 待办模糊查找..
     */
    @Select("SELECT * FROM `todo_record` where concat (id,title,description,handle_description,created_at,tag) like '%' #{title} '%' " +
            " and status in ("
            + ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT + ")" +
            "ORDER BY id DESC" +
            " LIMIT #{pagination.offset}, #{pagination.limit}")
    List<ToDoRecord> findByNameList(@Param("title") String title, @Param("pagination") Pagination pagination);

    /**
     * 待办模糊查找总个数
     */
    @Select("SELECT count(*) FROM `todo_record` where concat (id,title,description,handle_description,created_at) like '%' #{title} '%' " +
            " and status in (5,6,7) order by id desc")
    int findByNameListCount(@Param("title") String title);

    /**
     * 待办模糊查找[我的发送]
     */
    @Select("SELECT * FROM `todo_record` where concat (id,title,description,handle_description,created_at) like '%' #{title} '%' " +
            " and status in (" +
            +ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") and user_id=#{user_id} ORDER BY id DESC " +
            " LIMIT #{pagination.offset}, #{pagination.limit}")
    List<ToDoRecord> findMyPostByNameList(@Param("title") String title, @Param("pagination") Pagination pagination, @Param("user_id") Integer userId);

    /**
     * 待办模糊查找[我的处理]
     */
    @Select("SELECT * FROM `todo_record` where concat (id,title,description,handle_description,created_at) like " +
            "'%' #{title} '%'  and status in (" +
            +ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") and solve_id=#{user_id} ORDER BY id DESC LIMIT #{pagination.offset}, #{pagination.limit}")
    List<ToDoRecord> findMyHandleByName(@Param("title") String title, @Param("pagination") Pagination pagination, @Param("user_id") Integer userId);


    /**
     * 待办模糊查找总个数[我的发送]
     */
    @Select("SELECT count(*) FROM `todo_record` where concat (id,title,description,handle_description,created_at) like" +
            " '%' #{title} '%'  and status in (" +
            +ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") and user_id=#{user_id}")
    int findMyPostByNameListCount(@Param("title") String title, @Param("user_id") Integer userId);

    /**
     * 待办模糊查找总个数[我的处理]
     */
    @Select("SELECT count(*) FROM `todo_record` where concat (id,title,description,handle_description,created_at) like" +
            " '%' #{title} '%'  and status in (" +
            +ToDoRecord.STATUS_PENDING + "," + ToDoRecord.STATUS_BEING_PROCESSED + "," + ToDoRecord.STATUS_FIGURE_OUT +
            ") and solve_id=#{user_id}")
    int findMyHandleByNameCount(@Param("title") String title, @Param("user_id") Integer userId);

    /**
     * 待办列表批量删除
     */
    @Select("<script>" +
            "update todo_record set status=" + ToDoRecord.STATUS_DELETE + " WHERE id in " +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'>#{item} </foreach>" +
            "</script>")
    void deleteToDoList(@Param("ids") List<Integer> ids);

    /**
     * 传入待办id，获取详情
     */
    @Select("select * from todo_record where id=#{id} and status !=" + ToDoRecord.STATUS_DELETE)
    List<ToDoRecord> getToDoById(@Param("id") Integer id);
}
