package cn.niudehua.community.controller;

import cn.niudehua.community.dto.CommentCreateDTO;
import cn.niudehua.community.dto.CommentDTO;
import cn.niudehua.community.dto.ResultDTO;
import cn.niudehua.community.enums.CommentTypeEnum;
import cn.niudehua.community.exception.CustomizeErrorCode;
import cn.niudehua.community.model.Comment;
import cn.niudehua.community.model.User;
import cn.niudehua.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: deng
 * @datetime: 2020/5/3 12:09 上午
 * @desc: 评论控制
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {
    private final CommentService commentService;

    /**
     * 提交回复评论
     *
     * @param commentCreateDTO 评论回复DTO
     * @param request          Http Servlet请求
     * @return ResultDTO
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("gitHubUser");
        if (ObjectUtils.isEmpty(user)) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (ObjectUtils.isEmpty(commentCreateDTO) || StringUtils.isEmpty(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO comment(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
