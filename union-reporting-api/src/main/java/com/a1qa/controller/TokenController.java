package com.a1qa.controller;

import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.model.domain.Token;
import com.a1qa.model.domain.Variant;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TokenController extends ABaseController {

    @RequestMapping(value = "/token/get", method = RequestMethod.POST)
    public @ResponseBody String generateToken(@RequestParam(value = "variant") int variantID ) {
        Token token = new Token();
        token.setValue(UUID.randomUUID().toString().replace("-", ""));
        token.setVariant(HibernateUtil.getEntityById(Variant.class, variantID));
        HibernateUtil.save(token);
        return token.getValue();
    }
    }
