package vn.techmaster.blog.controller.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.techmaster.blog.model.Tag;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchByKeywordAndTag {
    private String keyword;
    private List<Tag> tag;
}
