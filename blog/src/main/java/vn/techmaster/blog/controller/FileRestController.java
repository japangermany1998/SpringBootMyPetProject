package vn.techmaster.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import vn.techmaster.blog.service.IBugService;

@RestController
public class FileRestController {
    @Autowired
    IBugService bugService;
}
