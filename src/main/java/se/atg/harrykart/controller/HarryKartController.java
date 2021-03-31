package se.atg.harrykart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atg.harrykart.dto.Response;
import se.atg.harrykart.schemas.HarryKartType;
import se.atg.harrykart.service.HarryKartService;
import se.atg.harrykart.utils.HarryKartRequestUtil;

@RestController
@RequestMapping("/java/api")
public class HarryKartController {

    @Autowired
    HarryKartService harryKartService;

    @PostMapping(path = "/play", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response playHarryKart(@RequestBody String request) throws Exception {
        return harryKartService.calculateRanking(request);
    }

}
