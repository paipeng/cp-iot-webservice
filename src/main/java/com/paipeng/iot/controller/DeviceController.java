package com.paipeng.iot.controller;

import com.paipeng.iot.entity.Device;
import com.paipeng.iot.mqtt.model.CPIOMessageBoard;
import com.paipeng.iot.service.DeviceService;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @GetMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public List<Device> get(HttpServletResponse httpServletResponse) throws Exception {
        return deviceService.get();
    }
    @GetMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
    public Device get(@NotNull @PathVariable("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        return deviceService.getById(id);
    }
    @PostMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public Device save(@RequestBody Device device, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return deviceService.save(device);
    }
    @PutMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public Device update(@RequestBody Device device, HttpServletResponse httpServletResponse) throws Exception {
        return deviceService.update(device);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable("id") Long id) throws Exception {
        deviceService.delete(id);
    }

    @GetMapping(value = "/{id}/led/{state}", produces = {"application/json;charset=UTF-8"})
    public Device updateLedState(@NotNull @PathVariable("id") Long id, @NotNull @PathVariable("state") int state, HttpServletResponse httpServletResponse) throws Exception {
        return deviceService.updateLedState(id, state);
    }


    @PostMapping(value = "/{id}/messageboard", produces = {"application/json;charset=UTF-8"})
    public Device updateMessageBoard(@NotNull @PathVariable("id") Long id, @RequestBody CPIOMessageBoard messageBoard, HttpServletResponse httpServletResponse) throws Exception {
        return deviceService.updateMessageBoard(id, messageBoard);
    }
}
