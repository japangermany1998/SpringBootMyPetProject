package vn.techmaster.blog.controller.search;

import lombok.Data;
import vn.techmaster.blog.model.Tag;

import java.util.List;

@Data
public class SearchByKeywordAndTag {
    private String keyword;
    private List<Tag> tag;
}
