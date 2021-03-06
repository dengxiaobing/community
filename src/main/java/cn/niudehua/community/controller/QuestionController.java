package cn.niudehua.community.controller;

import cn.niudehua.community.dto.CommentDTO;
import cn.niudehua.community.dto.QuestionDTO;
import cn.niudehua.community.enums.CommentTypeEnum;
import cn.niudehua.community.service.CommentService;
import cn.niudehua.community.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author: deng
 * @datetime: 2020/4/30 11:36 下午
 * @desc: 问题模型控制
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionController {
    private final QuestionService questionService;
    private final CommentService commentService;

    /**
     * 通过questionId 获取questionDTO 展示到问题详情页
     *
     * @param id    questionId
     * @param model 视图模型
     * @return 跳转到问题详情页
     */
    @GetMapping(value = "/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //每次访问问题页面就添加浏览数
        questionService.incViewCount(id);
        //查询相关问题
        List<QuestionDTO> relatedQuestions = questionService.relatedQuestion(questionDTO);

        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTOS", commentDTOS);
        model.addAttribute("relatedQuestions", relatedQuestions);

        return "question";
    }
}
