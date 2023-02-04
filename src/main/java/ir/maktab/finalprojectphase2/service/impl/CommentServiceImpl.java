package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.CommentDao;
import ir.maktab.finalprojectphase2.data.model.Comment;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.CommentService;
import ir.maktab.finalprojectphase2.service.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final ExpertService expertService;

    @Override
    public void save(Comment comment) {
        commentDao.save(comment);
        expertService.calculateAndUpdateExpertScore(comment.getExpert());
    }

    @Override
    public void update(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public Double averageOfExpertScore(Expert expert) {
        return commentDao.averageOfExpertScore(expert);
    }

    @Override
    public Comment findById(Long id) {
        return commentDao.findById(id).orElseThrow(() -> new NotFoundException("dose not exist"));
    }
}
