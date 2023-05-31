package com.ccoins.bff.utils;

import com.ccoins.bff.configuration.properties.WinVotingProperties;
import com.ccoins.bff.dto.ListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageTextUtils {

    private final WinVotingProperties winVotingCoins;

    @Autowired
    public MessageTextUtils(WinVotingProperties winVotingCoins) {
        this.winVotingCoins = winVotingCoins;
    }

    public ListDTO winVotingMsg(long points){

        List<String> list = new ArrayList<>();

        list.add(String.format(winVotingCoins.getWinCoins(), points));
        list.add(winVotingCoins.getWinMessage());

        return ListDTO.builder()
                .list(list)
                .build();
    }
}
