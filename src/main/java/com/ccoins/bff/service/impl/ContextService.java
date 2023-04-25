package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.security.authentication.JwtAuthentication;
import com.ccoins.bff.dto.bars.BarListDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.IContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContextService implements IContextService {

    protected final BarsFeign barsFeign;

    @Autowired
    public ContextService(BarsFeign barsFeign) {
        this.barsFeign = barsFeign;
    }

    @Override
    public Long getLoggedUserId() {
        return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getId();
    }

    @Override
    public Long findBarIdByOwner() {

        Long ownerId = this.getLoggedUserId();

        try{
            ResponseEntity<BarListDTO> bars = this.barsFeign.findAllBarsByOwner(ownerId);

            if(bars.hasBody() && bars.getBody() != null){
                BarListDTO list = bars.getBody();
                if (list.getList() != null && !list.getList().isEmpty())
                    return list.getList().get(0).getId();
            }
            return null;
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_OWNER_ERROR);
        }
    }
}
