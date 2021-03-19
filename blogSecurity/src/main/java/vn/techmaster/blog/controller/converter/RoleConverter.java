package vn.techmaster.blog.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import vn.techmaster.blog.model.Role;
import vn.techmaster.blog.repository.RoleRepository;

public class RoleConverter implements Converter<String,Role> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String s) {
        return roleRepository.findByName(s).get();
    }
}
