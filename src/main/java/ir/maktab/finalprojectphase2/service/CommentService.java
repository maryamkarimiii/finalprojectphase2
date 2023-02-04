package ir.maktab.finalprojectphase2.service;

import ir.maktab.finalprojectphase2.data.model.Comment;
import ir.maktab.finalprojectphase2.data.model.Expert;

public interface CommentService extends BaseService<Comment>{
    Double averageOfExpertScore(Expert expert);
    Comment findById(Long id);
}
