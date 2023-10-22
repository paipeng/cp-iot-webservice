package com.paipeng.iot.controller;



import com.paipeng.iot.entity.Record;
import com.paipeng.iot.service.RecordService;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/records")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @GetMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public List<Record> query(HttpServletResponse httpServletResponse) throws Exception {
        return recordService.query();
    }
    @GetMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
    public Record query(@NotNull @PathVariable("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        return recordService.query(id);
    }

    @GetMapping(value = "/devices/{id}", produces = {"application/json;charset=UTF-8"})
    public List<Record> queryByDevice(@NotNull @PathVariable("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        return recordService.queryByDevice(id);
    }
}
