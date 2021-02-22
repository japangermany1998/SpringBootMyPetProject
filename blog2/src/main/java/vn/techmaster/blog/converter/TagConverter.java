package vn.techmaster.blog.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.techmaster.blog.model.Tag;
import vn.techmaster.blog.repository.TagRepository;

@Component
public class TagConverter implements Converter<String, Tag> {
    @Autowired
    TagRepository tagRepository;

    @Override
    public Tag convert(String s) {
        return tagRepository.findByName(s).get();
    }

}
